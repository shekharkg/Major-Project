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

import com.etsy.android.grid.StaggeredGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by SKG on 24-Mar-14.
 */
abstract public class MainActivity extends ActionBarActivity implements AbsListView.OnScrollListener, AbsListView.OnItemClickListener, SearchView.OnQueryTextListener {

    private static final String TAG = "MainActivity";
    String title = "product title";
    String id = "product id";
    String image = "http://image.shutterstock.com/display_pic_with_logo/849265/111971633/stock-…to-concept-error-on-white-background-page-not-found-d-render-111971633.jpg";
    int start,rows,end;
    String search = "nike";
    StaggeredGridView myGridView;
    private boolean myHasRequestedMore;
    GridAdapter myAdapter;
   // String baseUrl;
   // String ajaxUrl;
    LayoutInflater layoutInflater;
    View footer;
    TextView txtFooterTitle;
    SearchView mSearchView;
    JSONArray jsonString;
    String postUrl;

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

       // baseUrl = getResources().getString(R.string.base_url);
        try {
            jsonString = new JSONArray(getResources().getString(R.string.json_string));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        postUrl = getResources().getString(R.string.base_url_m);

        try {
            postUrl = URLDecoder.decode(postUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

       /* try {
            baseUrl = URLDecoder.decode(baseUrl, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/

        start = 0;
        rows = 50;
        end = 0;
       // ajaxUrl = baseUrl+start+"&rows="+rows+"&q="+search;

        myGridView.setAdapter(myAdapter);
        myGridView.setOnScrollListener(this);
        myGridView.setOnItemClickListener(this);
        new HttpAsyncTask().execute(postUrl);
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
        Intent setIntentSearch = new Intent(this,SearchActivity.class);
        setIntentSearch.putExtra("searchValue", query);
        startActivity(setIntentSearch);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

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
        new HttpAsyncTask().execute(postUrl);
        start += rows;
        //ajaxUrl = baseUrl+start+"&rows="+rows+"&q="+search;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        ProductData productData = myAdapter.getItem(position);
        Intent setIntentProdId = new Intent(this, MyDialog.class);
        setIntentProdId.putExtra("prodPrice",productData.getId());
        setIntentProdId.putExtra("prodTitle",productData.getTitle());
        setIntentProdId.putExtra("prodImage",productData.getImage());
        startActivity(setIntentProdId);
    }

    class HttpAsyncTask extends AsyncTask<String, Void, String> {
       GetJsonString getJsonString = new GetJsonString();


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
           txtFooterTitle.setText("Loading...");
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... urls) {
            return getJsonString.GET(urls[0],jsonString, start, search);
        }

        @Override
        protected void onPostExecute(String result) {
            txtFooterTitle.setText("PostExecute");
            try {
                JSONObject json = new JSONObject(result);
                JSONObject response1 = json.getJSONObject("response1");
                JSONArray products = response1.getJSONArray("products");
                if(products.length() < 50){
                    end = 1;
                    txtFooterTitle.setText("JSon End");
                }
                for (int i = 0; i < products.length(); i++) {
                    title = products.getJSONObject(i).getString("product");
                    image = products.getJSONObject(i).getString("search_image");
                    id = products.getJSONObject(i).getString("price");
                    myAdapter.add(new ProductData(title, image, id));
                }
                myAdapter.notifyDataSetChanged();
                myHasRequestedMore = false;
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
}
