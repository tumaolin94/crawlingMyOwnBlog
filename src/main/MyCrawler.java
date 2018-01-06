package main;

import java.io.File;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ch.qos.logback.classic.Logger;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import stat.CrawlStat;
import util.SaveImage;
import util.WriteCSV;

/**
 * @author Maolin Tu
 */
public class MyCrawler extends WebCrawler {

	private static final Pattern IMAGE_EXTENSIONS = Pattern.compile(".*\\.(bmp|gif|jpeg|jpg|png)$");
	private static final Pattern FILTERS = Pattern.compile(
			".*(\\.(css|js|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v" + "|rm|smil|wmv|swf|wma|zip|rar|gz" + "" + "))$");

	private static final String START = "https://www.maolintu.com/";
	// "https://www.maolintu.com/"
	/*
	 * some statistic variables
	 */
	int fetch_attempted = 0;

	/**
	 * You should implement this function to specify whether the given url should be
	 * crawled or not (based on your crawling logic).
	 */
	CrawlStat myCrawlStat; // Crawler status object

	/**
	 * constructor method
	 */
	public MyCrawler() {
		myCrawlStat = new CrawlStat();
	}
	
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL();
		
		// write urls_blog.csv
		if(href.startsWith(START)) {
			WriteCSV.write(href, "OK", "urls_blog");
		}else {
			WriteCSV.write(href, "N_OK", "urls_blog");
		}
		// Image files are always distributed by CDN, so ignore the url limitation
		if (IMAGE_EXTENSIONS.matcher(href).matches()) {
			return true;
		}
		// Ignore the url if it has an extension that matches our defined set of image
		// extensions.
		if (FILTERS.matcher(href).matches()) {
			return false;
		}

		// Only accept the url if it is in the "www.maolintu.com" domain and protocol is
		// "https".
		
		return href.startsWith(START);
	}
	/**
	 * This function is called before visit()
	 */
	@Override
	protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
		myCrawlStat.addFetchAttempted();
		// write fetch_blog.csv
		WriteCSV.write(webUrl.getURL(), String.valueOf(statusCode), "fetch_blog");

		if (statusCode >= 200 && statusCode < 300) {
			myCrawlStat.addFetchSucceed();
		} else if (statusCode >= 300 && statusCode < 400) {
			myCrawlStat.addFetchAborted();
		} else if (statusCode >= 400) {
			myCrawlStat.addFetchFailed();
		}

	}

	/**
	 * This function is called when a page is fetched and ready to be processed by
	 * your program.
	 */
	@Override
	public void visit(Page page) {
		int fileSize = page.getContentData().length;
		String url = page.getWebURL().getURL();
		String contentType = page.getContentType();
		if (contentType.startsWith("text/html")) {
			contentType = "text/html";
		}

		String[] contents = new String[4];
		contents[0] = url;
		contents[1] = String.valueOf(fileSize);
		contents[2] = "0";
		contents[3] = contentType;
		logger.info("Visit URL: {}", url);
		logger.info("Visit size: {}", fileSize);
		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			Set<WebURL> links = htmlParseData.getOutgoingUrls();
			logger.debug("Number of outgoing links: {}", links.size());
			contents[2] = String.valueOf(links.size());
		} else {
			logger.debug("Number of outgoing links: {}", 0);
		}
		logger.info("Visit type: {}", contentType);
		// write visit_blog.csv
		WriteCSV.write(contents, "visit_blog");
		
		if(IMAGE_EXTENSIONS.matcher(url).matches()) {
			SaveImage.saveImage(page, IMAGE_EXTENSIONS, "images");
		}
	}

	@Override
	public void onBeforeExit() {
		logger.info("Fetch Attempted {}: ", myCrawlStat.getFetchAttempted());
		logger.info("Fetch Succeed {}: ", myCrawlStat.getFetchSucceed());
		logger.info("Fetch Aborted {}: ", myCrawlStat.getFetchAborted());
		logger.info("Fetch Failed {}: ", myCrawlStat.getFetchFailed());
	}

	@Override
	public Object getMyLocalData() {
		return myCrawlStat;
	}
}
