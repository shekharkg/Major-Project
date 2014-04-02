package com.example1.cp.gridpage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class MyDialog extends ActionBarActivity implements SearchView.OnQueryTextListener  {

    private SearchView mSearchView;
    private ImageView prodImageView;
    private TextView prodTitleView, prodDescView;
    private String prodImg, prodTitle, prodPrice, prodBuy;

    private ShareActionProvider myShareActionProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent getIntentDetails = getIntent();
        if (getIntentDetails != null) {
            prodImg = getIntentDetails.getStringExtra("prodImage");
            prodTitle = getIntentDetails.getStringExtra("prodTitle");
            prodPrice = getIntentDetails.getStringExtra("prodPrice");
            prodBuy = getIntentDetails.getStringExtra("prodBuy");
        }

        String mainUrl = getResources().getString(R.string.prod_url_m);
        try {
            mainUrl = URLDecoder.decode(mainUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        prodBuy = mainUrl + prodBuy;

        setTitle(prodTitle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FadingActionBarHelper helper = new FadingActionBarHelper()
                .actionBarBackground(R.drawable.ab_background)
                .headerLayout(R.layout.activity_scrollview)
                .contentLayout(R.layout.header);
        setContentView(helper.createView(this));
        helper.initActionBar(this);

        prodImageView = (ImageView) findViewById(R.id.image_header);
        prodTitleView = (TextView) findViewById(R.id.prod_title);
        prodDescView = (TextView) findViewById(R.id.prod_desc);

        prodImg = prodImg.replace("style_search_image","properties");
        prodImg = prodImg.replace("180_240","360_480_mini");

        Ion.with(prodImageView).placeholder(R.drawable.product).error(R.drawable.product).load(prodImg);
        prodTitleView.setText(prodTitle);
        prodDescView.setText("Price : "+prodPrice);
    }

    public void buy(View v){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(prodBuy));
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setOnQueryTextListener(this);

        MenuItem shareItem = menu.findItem(R.id.menu_item_share);
        myShareActionProvider = (ShareActionProvider)
                MenuItemCompat.getActionProvider(shareItem);
        myShareActionProvider.setShareIntent(createShareIntent());
        return super.onCreateOptionsMenu(menu);
    }
    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, prodBuy);
        return shareIntent;
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


}