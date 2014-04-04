package com.example1.cp.gridpage;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by SKG on 04-Apr-14.
 */
public class ShopByBrands extends ActionBarActivity implements AbsListView.OnScrollListener, AbsListView.OnItemClickListener, SearchView.OnQueryTextListener {

    private static final String TAG = "MainActivity";
  //  String title, id, image, buy;
    //int start,rows,end;
    String search = "nike";
    StaggeredGridView myGridView;
    private boolean myHasRequestedMore;
    GridAdapterBrands myAdapter;
    // String baseUrl;
    // String ajaxUrl;
    LayoutInflater layoutInflater;
    View footer;
    TextView txtFooterTitle;
    SearchView mSearchView;
   // JSONArray jsonString;
    String postUrl;
    String replaceUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brands);

        myGridView = (StaggeredGridView) findViewById(R.id.grid_view);
        layoutInflater = getLayoutInflater();
        footer = layoutInflater.inflate(R.layout.list_item_header_footer, null);
        txtFooterTitle =  (TextView) footer.findViewById(R.id.txt_title);
        txtFooterTitle.setText("THE FOOTER!");
        myGridView.addFooterView(footer);
        myAdapter = new GridAdapterBrands(this, R.id.txt_line);

       // start = 0;
       // rows = 50;
       // end = 0;

        replaceUrl = getResources().getString(R.string.prod_url_m);
        try {
            replaceUrl = URLDecoder.decode(replaceUrl,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        postUrl=getResources().getString(R.string.brands_url);
        try {
            postUrl = URLDecoder.decode(postUrl,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

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
        /*Log.d(TAG, "onScroll firstVisibleItem:" + firstVisibleItem +
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
        }*/
        txtFooterTitle.setText("End...");
    }

    private void onLoadMoreItems() {
        new HttpAsyncTask().execute(postUrl);
       // start += rows;
       // rows += 50;
        //ajaxUrl = baseUrl+start+"&rows="+rows+"&q="+search;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        ProductDataBrands productData = myAdapter.getItem(position);
        Intent setIntentProdId = new Intent(this, SearchActivity.class);
        setIntentProdId.putExtra("searchValue",productData.getBrand_names().toString());
        startActivity(setIntentProdId);
    }

    class HttpAsyncTask extends AsyncTask<String, Void, Document> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            txtFooterTitle.setText("Loading...");
            super.onPreExecute();
        }

        @Override
        protected Document doInBackground(String... urls) {
            Document doc = null;
            try {
                doc = Jsoup.connect(postUrl).userAgent("Mozilla").get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return doc;
        }

        @Override
        protected void onPostExecute(Document doc) {
            ConnectivityManager connec = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connec != null && (connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) ||(connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED)) {
                //You are connected, do something online.
                Elements brand_title = doc.select("[class=no-decoration-link]");
                Elements brand_image = doc.select("[width=120]");

                String brand_names, brand_url, brand_logo;
                for (int i = 0; i < brand_title.size(); i++) {
                    brand_names = brand_title.get(i).text();
                    brand_url = brand_title.get(i).attr("abs:href").replace(replaceUrl, "");
                    brand_logo = brand_image.get(i).attr("abs:src");
                    brand_logo = brand_logo.replace(" ", "%20");
                    myAdapter.add(new ProductDataBrands(brand_names, brand_url, brand_logo));
                }

                myAdapter.notifyDataSetChanged();
                myHasRequestedMore = false;
            }else if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||  connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED ) {
                //Not connected.
                txtFooterTitle.setText("Connect to Internet...");
            }

           }

    }
}
