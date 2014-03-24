package com.example1.cp.gridpage;

        import java.util.ArrayList;

public class SampleData  {

    public static final int SAMPLE_DATA_ITEM_COUNT = 30;

    public static ArrayList<ProductData> generateSampleData() {
        final ArrayList<ProductData> data = new ArrayList<ProductData>(SAMPLE_DATA_ITEM_COUNT);
        String title = "product title";
        String description = "product description";
        String image = "http://images.kooves.com/uploads/products/47765_390bb862e68488c956d5c4c75fc8b4ca_image1_thumb_90_120.jpg";

        for (int i = 0; i < SAMPLE_DATA_ITEM_COUNT; i++) {
            data.add(new ProductData(title, description,image));
        }

        return data;
    }

}
