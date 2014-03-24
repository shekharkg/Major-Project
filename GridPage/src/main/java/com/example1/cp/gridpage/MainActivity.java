package com.example1.cp.gridpage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;

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
public class MainActivity extends Activity implements AbsListView.OnScrollListener, AbsListView.OnItemClickListener {


    private static final String TAG = "MainActivity";
    public static final String SAVED_DATA_KEY = "SAVED_DATA";

    private StaggeredGridView myGridView;
    private boolean myHasRequestedMore;
    private GridAdapter myAdapter;

    private ArrayList<ProductData> myData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myGridView = (StaggeredGridView) findViewById(R.id.grid_view);


        myAdapter = new GridAdapter(this, R.id.txt_line);



        // do we have saved data?
       /* if (savedInstanceState != null) {
            myData = savedInstanceState.getStringArrayList(SAVED_DATA_KEY);
        }*/

        if (myData == null) {
            myData = SampleData.generateSampleData();
        }

        for (ProductData data : myData) {
            myAdapter.add(data);
        }

        myGridView.setAdapter(myAdapter);

        myGridView.setOnScrollListener(this);

        myGridView.setOnItemClickListener(this);
    }

   /* @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(SAVED_DATA_KEY, myData);
    }*/

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
            if (lastInScreen >= totalItemCount) {
                Log.d(TAG, "onScroll lastInScreen - so load more");
                myHasRequestedMore = true;
                onLoadMoreItems();
            }
        }
    }

    private void onLoadMoreItems() {
        final ArrayList<ProductData> sampleData = SampleData.generateSampleData();
        for (ProductData data : sampleData) {
            myAdapter.add(data);
        }
        // stash all the data in our backing store
        myData.addAll(sampleData);
        // notify the adapter that we can update now
        myAdapter.notifyDataSetChanged();
        myHasRequestedMore = false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Toast.makeText(this, "Item Clicked: " + position, Toast.LENGTH_SHORT).show();
    }


}
