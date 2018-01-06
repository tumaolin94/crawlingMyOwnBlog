package util;
/**
 * @author Maolin Tu
 */
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;


public class GetArticles {
	/**
	 * getAllarticles, fetch all articles of one blog into a CSV file
	 * @param page current page
	 */	
	public static void getAllarticles(Page page) {
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
				Elements headers = item.getElementsByClass("entry-header"); // get header of article
				if (headers.size()==0||headers.get(0).getElementsByTag("a").size() == 0)
					break; // only fetch articles on main page
				Element href = headers.get(0).getElementsByTag("a").get(0);
				// Writing data into csv
				String[] contents = new String[2];
				contents[0] = href.attr("abs:href");
				contents[1] = href.text().replace(",", "_");
				WriteCSV.write(contents, "blog_articles");
			}
		}
	}
}
