package util;

import com.google.gson.Gson;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import model.Product;

public class JsonConverter {

    public void convert(List<Product> products) throws IOException {

        try (FileWriter fileWriter
                     = new FileWriter("Products.json")) {
            Gson gson = new Gson();
            String json = gson.toJson(products);
            synchronized (fileWriter) {
                fileWriter.append(json);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
