import java.io.IOException;
import org.apache.log4j.Logger;
import util.JsonConverter;
import util.WebsiteParser;

public class Main {

    final static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        WebsiteParser websiteParser = new WebsiteParser();
        JsonConverter jsonConverter = new JsonConverter();
        try {
            jsonConverter.convert(websiteParser.getProducts());
        } catch (IOException e) {
            logger.error("IOException" + e);
        } catch (InterruptedException e) {
            logger.error("InterruptedException" + e);
        }
    }
}



