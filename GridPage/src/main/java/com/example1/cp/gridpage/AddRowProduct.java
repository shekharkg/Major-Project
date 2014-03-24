package com.example1.cp.gridpage;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
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

/**
 * Created by SKG on 24-Mar-14.
 */
public class AddRowProduct  extends AsyncTask<Void,Void,Void> {

    ArrayList<ProductData> productRow;
    String feedUrl = "https://gdata.youtube.com/feeds/api/users/TheViralFeverVideos/uploads?v=2&alt=jsonc";
    ProgressDialog dialog;


    @Override
    protected Void doInBackground(Void... params)
    {
        HttpClient client = new DefaultHttpClient();
        HttpGet getRequest = new HttpGet(feedUrl);
        try
        {
            HttpResponse response = client.execute(getRequest);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();

            if(statusCode!=200)
            {
                return null;
            }
            InputStream jsonStream = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(jsonStream));
            StringBuilder builder = new StringBuilder();
            String line;
            while((line = reader.readLine())!=null){
                builder.append(line);
            }
            String jsonData = builder.toString();
            JSONObject json = new JSONObject(jsonData);
            JSONObject data = json.getJSONObject("data");
            JSONArray items = data.getJSONArray("items");

            for(int i=0; i<items.length();i++)
            {
                JSONObject video = items.getJSONObject(i);
                ProductData productAdd = new ProductData(video.getString("title"), video.getString("description"), video.getString("hqDefault"));
                productRow.add(productAdd);
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}