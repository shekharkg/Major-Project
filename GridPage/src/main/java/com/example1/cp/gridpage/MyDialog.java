package com.example1.cp.gridpage;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class MyDialog extends ActionBarActivity implements SearchView.OnQueryTextListener, AdapterView.OnItemClickListener {

    private SearchView mSearchView;
    private ImageView prodImageView;
    private TextView prodTitleView, prodDescView;
    private String prodImg, prodTitle, prodID, prodBuy;
    ListView imageListView;
    List<String> imageList= new ArrayList<String>();
    List<String> sizeList = new ArrayList<String>();
    SingleProductDetails objectSingleProductDetails;

    private ShareActionProvider myShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent getIntentDetails = getIntent();
        if (getIntentDetails != null) {
            //prodImg = getIntentDetails.getStringExtra("prodImage");
            //prodTitle = getIntentDetails.getStringExtra("prodTitle");
            prodID = getIntentDetails.getStringExtra("prodID");
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

        imageListView = (ListView) findViewById(R.id.listView);
        imageListView.setOnItemClickListener(this);

        //prodImg = prodImg.replace("style_search_image","properties");
        //prodImg = prodImg.replace("180_240","360_480_mini");

        //Ion.with(prodImageView).placeholder(R.drawable.product).error(R.drawable.product).load(prodImg);
        //prodTitleView.setText(prodTitle);
        //prodDescView.setText("Price : "+ prodPrice);


        new HttpAsyncTask().execute(prodBuy);

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView txt = (TextView) view.findViewById(R.id.text_sample);
        String imgUrl = txt.getText().toString();
        imgUrl = imgUrl.replace("48_64","360_480");
        Ion.with(prodImageView).placeholder(R.drawable.product).error(R.drawable.product).load(imgUrl);
    }

    class HttpAsyncTask extends AsyncTask<String, Void, Document> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected Document doInBackground(String... urls) {
            Document doc = null;
            try {
                doc = Jsoup.connect(prodBuy).userAgent("Mozilla").get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return doc;
        }

        @Override
        protected void onPostExecute(Document doc) {
            Elements title = doc.select("h1[class=title]");
            Elements description = doc.select("[id=product-description]");
            Elements price = doc.select("[class=price]");
            Elements imageArray = doc.select("[width=48]");
            Elements avail_size = doc.select("[data-availableinwarehouse]");
            Elements discount = doc.select("[class=discount]");
            Elements prodID = doc.select("[class=id pdt-code]");

            for (Element srcImg : imageArray)
            {
                imageList.add(srcImg.attr("abs:src"));
            }
            String image = imageList.get(0).replace("48_64", "360_480");
            String exactPrice = price.text();
            exactPrice = exactPrice.replace("click for offer"," ");
            for (Element src : avail_size)
            {
                sizeList.add(src.text());
            }

            prodTitleView.setText(title.text());
            prodDescView.setText(description.text());
            Ion.with(prodImageView).placeholder(R.drawable.product).error(R.drawable.product).load(image);

            ImageListAdapter adapter = new ImageListAdapter(MyDialog.this,imageList, imageList);
            imageListView.setAdapter(adapter);
            //prodTitleView.setText(title.text()+ "\n" +description.text()+ "\n" +exactPrice+ "\n" +imageList+ "\n" +prodID.text()+ "\n" +sizeList + "\n" + discount.text());
            objectSingleProductDetails = new SingleProductDetails(title.text(),description.text(),exactPrice,imageList,sizeList,discount.text(),prodID.text());
        }

    }

}