package com.example1.cp.gridpage;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class MyDialog extends ActionBarActivity implements SearchView.OnQueryTextListener  {

    private SearchView mSearchView;
    private String prodUrl;
    private ImageView prodImageView;
    private TextView prodTitleView, prodDescView;
    private String prodImg, prodTitle, prodDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intentGetProdId = getIntent();
        if(intentGetProdId!=null) {
            String prodID = intentGetProdId.getStringExtra("prodID");
            prodUrl =getResources().getString(R.string.prod_url);
            try {
                prodUrl = URLDecoder.decode(prodUrl, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            prodUrl = prodUrl+prodID;
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FadingActionBarHelper helper = new FadingActionBarHelper()
                .actionBarBackground(R.drawable.ab_background)
                .headerLayout(R.layout.header)
                .contentLayout(R.layout.activity_scrollview);
        setContentView(helper.createView(this));
        helper.initActionBar(this);

        prodImageView = (ImageView) findViewById(R.id.image_header);
        prodTitleView = (TextView) findViewById(R.id.prod_title);
        prodDescView = (TextView) findViewById(R.id.prod_desc);

        new HttpAsyncTask().execute(prodUrl);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Intent setIntentSearch = new Intent(this,SearchActivity.class);
        setIntentSearch.putExtra("searchValue", query);
        startActivity(setIntentSearch);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        GetJsonString getJsonString = new GetJsonString();


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... urls) {
            return getJsonString.GET(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject json = new JSONObject(result);
                JSONObject response = json.getJSONObject("response");
                JSONArray docs = response.getJSONArray("docs");

                prodTitle = docs.getJSONObject(0).getString("product_name");
                prodDesc = docs.getJSONObject(0).getString("long_desc");
                String strImage = docs.getJSONObject(0).getString("thumb_image_url");
                prodImg = strImage.replace("wid=120&hei=120", "wid=350&hei=350");

                setTitle(prodTitle);
                prodTitleView.setText(prodTitle);
                prodDescView.setText(prodDesc);
                Ion.with(prodImageView).placeholder(R.drawable.product).error(R.drawable.product).load(prodImg);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                prodTitleView.setText("Connect to Internet");
                e.printStackTrace();
            }
        }

    }
}