package util;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;
import com.google.common.io.Files;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.BinaryParseData;

/**
 * @author Maolin Tu
 */
public class SaveImage {
	 private static File storageFolder;
	/**
	 * download images in local files
	 * @param page current page
	 * @param imgPatterns patterns of images allowed download
	 * @param path image save path
	 */
	public static void saveImage(Page page, Pattern imgPatterns, String path) {
        String url = page.getWebURL().getURL();
        storageFolder = new File(path);
        if (!storageFolder.exists()) {
            storageFolder.mkdirs();
        }
        // We are only interested in processing images which are bigger than 10k
        if (!imgPatterns.matcher(url).matches() ||
            !((page.getParseData() instanceof BinaryParseData))) {
            return;
        }

        // get a unique name for storing this image
        String extension = url.contains("?")?url.substring(url.lastIndexOf('.'), url.lastIndexOf("?")):url.substring(url.lastIndexOf('.'));
        String hashedName = UUID.randomUUID() + extension;

        // store image
        String filename = storageFolder.getAbsolutePath() + "/" + hashedName;
        try {
            Files.write(page.getContentData(), new File(filename));
            System.out.println("Stored: "+url);
        } catch (IOException iox) {
        	System.err.println("Failed to write file: "+filename+" "+iox);
        }
	}
}
