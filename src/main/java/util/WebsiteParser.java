package util;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import model.Product;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebsiteParser {

    final static Logger logger = Logger.getLogger(WebsiteParser.class);

    private static final String URL = "https://www.aboutyou.de/maenner/bekleidung";
    private static final String URL_CORE = "https://www.aboutyou.de";
    private List<Product> products = new ArrayList<>();

    public synchronized List<Product> getProducts() throws IOException, InterruptedException {
        HttpRequestCounter httpRequestCounter = new HttpRequestCounter();

        final Document mainPage = Jsoup.parse(new URL(URL), 10000);
        httpRequestCounter.increment();
        Elements productElements = mainPage.getElementsByAttributeValue("data-test-id", "ProductTile");

        productElements.forEach(productElement -> {
            String productUrl = URL_CORE + productElement.attr("href");
            Document productPage = null;
            try {
                productPage = Jsoup.parse(new URL(productUrl), 10000);
                httpRequestCounter.increment();
            } catch (IOException e) {
                logger.error("IOException" + e);
            }

            List<String> productProperties =
                    Collections.synchronizedList(extractProductProperties(productElement,
                            productPage));

            List<String> colors = getColors(productPage);
            for (String color : colors) {
                Product product = new Product();
                product.setColor(color);
                product.setBrand(productProperties.get(0));
                product.setPrice(productProperties.get(1));
                product.setArticleId(productProperties.get(2));
                product.setName(productProperties.get(3));
                products.add(product);
            }
        });
        logger.debug("Amount of triggered http requests: " + HttpRequestCounter.getCount());
        logger.debug("Amount of extracted products: " + products.size());
        Thread.sleep(5000);
        return products;
    }

    List<String> getColors(Document itemPage) {
        Elements colorsElements =
                itemPage.getElementsByAttributeValue("data-test-id", "ColorVariantColorInfo");
        List<String> colors = new ArrayList<>();
        colorsElements.forEach(colorElement -> {
            String[] colorsToSplit = colorElement.text().split(" / ");
            colors.addAll(Arrays.asList(colorsToSplit));
        });
        return colors;
    }

    List<String> extractProductProperties(Element productElement, Document productPage) {
        List<String> productProperties = Collections.synchronizedList(new ArrayList<>());
        productProperties.add(productElement.getElementsByAttributeValue("data-test-id",
                "BrandName").first().text());
        productProperties.add(productElement.getElementsByAttributeValue("data-test-id",
                "ProductPriceFormattedBasePrice").text());
        productProperties.add(productPage.getElementsByAttributeValue("data-test-id",
                "ArticleNumber").first().text().substring(9));
        productProperties.add(productPage.getElementsByAttributeValue("data-test-id",
                "ProductName").first().text());
        return productProperties;
    }
}
