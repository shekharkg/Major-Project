package com.example1.cp.gridpage;

import android.content.Intent;
import android.os.Bundle;

/**
 * Created by SKG on 31-Mar-14.
 */
public class SearchActivity extends MainActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Intent getIntentSearch = getIntent();
        if (getIntentSearch != null) {
            search = getIntentSearch.getStringExtra("searchValue");

        }
        super.onCreate(savedInstanceState);
        setTitle("Result for: " + search);
    }
}
