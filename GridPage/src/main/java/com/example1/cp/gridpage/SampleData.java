package com.example1.cp.gridpage;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SampleData {
    //static String url = "http://0.us-east-1a.search-sandbox.ss1.mobile.brmob.net:7090/solr/searsoutlet_com_products/select_qd20130731?q=fridge&facet=false&wt=json&fl=product_id%2Cproduct_name%2Cthumb_image_url%2Cmfr_name%2Cprice&";
    public static final int SAMPLE_DATA_ITEM_COUNT = 5;
    JSONArray productList = null;
    static String title = "product title";
    static String description = "product description";
    static String image = "http://image.shutterstock.com/display_pic_with_logo/849265/111971633/stock-…to-concept-error-on-white-background-page-not-found-d-render-111971633.jpg";
    static ArrayList<ProductData> data = new ArrayList<ProductData>(SAMPLE_DATA_ITEM_COUNT);
    public static ArrayList<ProductData> generateSampleData() {


        new HttpAsyncTask().execute("http://0.us-east-1a.search-sandbox.ss1.mobile.brmob.net:7090/solr/searsoutlet_com_products/select_qd20130731?q=fridge&facet=false&wt=json&fl=product_id%2Cproduct_name%2Cthumb_image_url%2Cmfr_name%2Cprice&start=0&rows=100");
        for (int i = 0; i < SAMPLE_DATA_ITEM_COUNT; i++) {
            data.add(new ProductData(title, description, image));
        }
        return data;
    }

    private static class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject json = new JSONObject(result);
                JSONObject response = json.getJSONObject("response");
                JSONArray docs = response.getJSONArray("docs");

                for (int i = 0; i < docs.length(); i++) {
                    title = docs.getJSONObject(i).getString("product_name");
                    image = docs.getJSONObject(i).getString("thumb_image_url");
                    description = docs.getJSONObject(i).getString("product_name");
                    data.add(new ProductData(title, description, image));
                }
                //etResponse.setText(str);
                //etResponse.setText(json.toString(1));

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

}