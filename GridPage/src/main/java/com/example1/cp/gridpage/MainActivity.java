package com.example1.cp.gridpage;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;

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

/**
 * Created by SKG on 24-Mar-14.
 */
public class MainActivity extends ActionBarActivity implements AbsListView.OnScrollListener, AbsListView.OnItemClickListener, SearchView.OnQueryTextListener {

    private static final String TAG = "MainActivity";
    static String title = "product title";
    static String description = "product description";
    static String image = "http://image.shutterstock.com/display_pic_with_logo/849265/111971633/stock-…to-concept-error-on-white-background-page-not-found-d-render-111971633.jpg";
    private int start,rows,end;
    private String search;
    private StaggeredGridView myGridView;
    private boolean myHasRequestedMore;
    private GridAdapter myAdapter;
    private String baseUrl = "http://0.us-east-1a.search-sandbox.ss1.mobile.brmob.net:7090/solr/searsoutlet_com_products/select_qd20130731?facet=false&wt=json&fl=product_id%2Cproduct_name%2Cthumb_image_url%2Cmfr_name%2Cprice&start=";
    private String ajaxUrl;
    private LayoutInflater layoutInflater;
    private View footer;
    private TextView txtFooterTitle;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myGridView = (StaggeredGridView) findViewById(R.id.grid_view);
        layoutInflater = getLayoutInflater();
        footer = layoutInflater.inflate(R.layout.list_item_header_footer, null);
        txtFooterTitle =  (TextView) footer.findViewById(R.id.txt_title);
        txtFooterTitle.setText("THE FOOTER!");
        myGridView.addFooterView(footer);
        myAdapter = new GridAdapter(this, R.id.txt_line);

        Intent intent = getIntent();
        if(intent!=null) {
            search = intent.getStringExtra("searchValue");
        }

        start = 0;
        rows = 30;
        end = 0;
        ajaxUrl = baseUrl+start+"&rows="+rows+"&q="+search;

        myGridView.setAdapter(myAdapter);
        myGridView.setOnScrollListener(this);
        myGridView.setOnItemClickListener(this);
        new HttpAsyncTask().execute(ajaxUrl);
        start += rows;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Toast.makeText(this, query, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("searchValue",query);
        startActivity(intent);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_search:
                mSearchView.setIconified(false);
                return true;
        }

        return false;
    }

    @Override
    public void onScrollStateChanged(final AbsListView view, final int scrollState) {
        Log.d(TAG, "onScrollStateChanged:" + scrollState);
    }

    @Override
    public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
        Log.d(TAG, "onScroll firstVisibleItem:" + firstVisibleItem +
                " visibleItemCount:" + visibleItemCount +
                " totalItemCount:" + totalItemCount);
        // our handling
        if (!myHasRequestedMore) {
            int lastInScreen = firstVisibleItem + visibleItemCount;
            if ((lastInScreen >= totalItemCount) && (end==0)){
                Log.d(TAG, "onScroll lastInScreen - so load more");
                myHasRequestedMore = true;
                onLoadMoreItems();
            }
        }
    }

    private void onLoadMoreItems() {
        new HttpAsyncTask().execute(ajaxUrl);
        start += rows;
        ajaxUrl = baseUrl+start+"&rows="+rows+"&q="+search;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Toast.makeText(this, "Item Clicked: " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this,MyDialog.class);
        startActivity(intent);
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
           txtFooterTitle.setText("Loading...");
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... urls) {
            return GET(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            txtFooterTitle.setText("");
            try {
                JSONObject json = new JSONObject(result);
                JSONObject response = json.getJSONObject("response");
                JSONArray docs = response.getJSONArray("docs");
                if(docs.length() < 30){
                    end = 1;
                    txtFooterTitle.setText("JSon End");
                }
                for (int i = 0; i < docs.length(); i++) {
                    title = docs.getJSONObject(i).getString("product_name");
                    image = docs.getJSONObject(i).getString("thumb_image_url");
                    description = docs.getJSONObject(i).getString("product_name");
                    myAdapter.add(new ProductData(title, description, image));
                }
                myAdapter.notifyDataSetChanged();
                myHasRequestedMore = false;
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                txtFooterTitle.setText("Connect to the internet");
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
