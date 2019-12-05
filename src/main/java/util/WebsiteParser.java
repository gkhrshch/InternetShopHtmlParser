package util;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import model.Product;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class WebsiteParser {

    final static Logger logger = Logger.getLogger(WebsiteParser.class);

    private static final String URL = "https://www.aboutyou.de/maenner/bekleidung";
    private static final String URL_CORE = "https://www.aboutyou.de";
    private List<Product> products = new ArrayList<>();

    public synchronized List<Product> getProducts() throws IOException, InterruptedException {
        AtomicInteger httpRequestCount = new AtomicInteger(0);
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        final Document mainPage = Jsoup.parse(new URL(URL), 10000);
        executorService.submit(httpRequestCount::incrementAndGet);
        Elements elements = mainPage.getElementsByAttributeValue("data-test-id", "ProductTile");

        elements.forEach(element -> {
            String url = element.attr("href");
            Document itemPage = null;
            try {
                itemPage = Jsoup.parse(new URL(URL_CORE + url), 10000);
                executorService.submit(httpRequestCount::incrementAndGet);
            } catch (IOException e) {
                logger.error("IOException" + e);
            }

            String brand = element.getElementsByAttributeValue("data-test-id",
                    "BrandName").first().text();
            String price = element.getElementsByAttributeValue("data-test-id",
                    "ProductPriceFormattedBasePrice").text();
            String articleId = itemPage.getElementsByAttributeValue("data-test-id", "ArticleNumber")
                    .first().text().substring(9);
            String name = itemPage.getElementsByAttributeValue("data-test-id", "ProductName")
                    .first().text();

            Elements colorsElements =
                    itemPage.getElementsByAttributeValue("data-test-id", "ColorVariantColorInfo");
            List<String> colors = new ArrayList<>();
            colorsElements.forEach(colorElement -> {
                String[] colorsToSplit = colorElement.text().split(" / ");
                colors.addAll(Arrays.asList(colorsToSplit));
            });
            for (String color : colors) {
                Product product = new Product();
                product.setArticleId(articleId);
                product.setColor(color);
                product.setBrand(brand);
                product.setPrice(price);
                product.setName(name);
                products.add(product);
            }
        });
        logger.debug("Amount of triggered http requests: " + httpRequestCount);
        executorService.shutdown();
        logger.debug("Amount of extracted products: " + products.size());
        Thread.sleep(5000);
        return products;
    }
}
