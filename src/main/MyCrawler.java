package main;

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
import util.WriteCSV;

/**
 * @author Maolin Tu
 */
public class MyCrawler extends WebCrawler {

	private static final Pattern IMAGE_EXTENSIONS = Pattern.compile(".*\\.(bmp|gif|jpg|png)$");
	private static final Pattern FILTERS = Pattern.compile(".*(\\.(css|js|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf"
			+ "|rm|smil|wmv|swf|wma|zip|rar|gz" + "bmp|gif|jpg|png" + "))$");
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
		int statusCode = referringPage.getStatusCode();
		logger.info("shouldVisit URL: {}", href);
		logger.info("shouldVisit StatusCode: {}", statusCode);
		myCrawlStat.addFetchAttempted();
		WriteCSV.write(href, String.valueOf(statusCode), "fetch_blog");
		// Ignore the url if it has an extension that matches our defined set of image
		// extensions.
		if (FILTERS.matcher(href).matches()) {
			return false;
		}

		// Only accept the url if it is in the "www.maolintu.com" domain and protocol is
		// "https".
		return href.startsWith("https://www.maolintu.com/");
	}

	/**
	 * This function is called when a page is fetched and ready to be processed by
	 * your program.
	 */
	@Override
	public void visit(Page page) {
		int docid = page.getWebURL().getDocid();
		String url = page.getWebURL().getURL();
		String domain = page.getWebURL().getDomain();
		String path = page.getWebURL().getPath();
		String subDomain = page.getWebURL().getSubDomain();
		String parentUrl = page.getWebURL().getParentUrl();
		String anchor = page.getWebURL().getAnchor();
		// logger.debug("Docid: {}", docid);
		// logger.info("Visit URL: {}", url);
		// logger.debug("Domain: '{}'", domain);
		// logger.debug("Sub-domain: '{}'", subDomain);
		// logger.debug("Path: '{}'", path);
		// logger.debug("Parent page: {}", parentUrl);
		// logger.debug("Anchor text: {}", anchor);

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String text = htmlParseData.getText();
			String html = htmlParseData.getHtml();
			Document doc = Jsoup.parse(html);
			Elements articles = doc.getElementsByTag("article");
			if (articles.size() == 0) {
				return;
			}
			for (Element item : articles) {
				// logger.debug("id : {}", item.id());
				Elements headers = item.getElementsByClass("entry-header"); // get header of article
				if (headers.get(0).getElementsByTag("a").size() == 0)
					break; // only fetch articles on main page
				Element href = headers.get(0).getElementsByTag("a").get(0);
				// logger.debug("header : {}", href.text());
				// logger.debug("href : {}", href.attr("abs:href"));
				// Writing data into csv
				String[] contents = new String[2];
				contents[0] = href.attr("abs:href");
				contents[1] = href.text().replace(",", "_");
				// WriteCSV.write(contents);
			}
			Set<WebURL> links = htmlParseData.getOutgoingUrls();
			logger.debug("Text length: {}", text.length());
			logger.debug("Html length: {}", html.length());
			logger.debug("Number of outgoing links: {}", links.size());
		}

		Header[] responseHeaders = page.getFetchResponseHeaders();
		if (responseHeaders != null) {
			logger.debug("Response headers:");
			for (Header header : responseHeaders) {
				logger.debug("\t{}: {}", header.getName(), header.getValue());
			}
		}

		logger.debug("=============");
	}

	@Override
	public void onBeforeExit() {
		logger.info("fetch_attempted {}: ",myCrawlStat.getFetchAttempted());
	}
	
	@Override
	  public Object getMyLocalData() {
	    return myCrawlStat;
	  }
}
