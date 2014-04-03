package com.example1.cp.gridpage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SKG on 04-Apr-14.
 */
public class SingleProductDetails {
    String title;
    String description;
    String price;
    List<String> imgUrls = new ArrayList<String>();
    List<String> avail_size = new ArrayList<String>();
    String discount_percent;
    String prodID;

    public SingleProductDetails(String title, String description, String price, List<String> imgUrls,List<String> avail_size, String discount_percent, String prodID){
        this.title = title;
        this.description = description;
        this.price = price;
        this.imgUrls = imgUrls;
        this.avail_size = avail_size;
        this.discount_percent = discount_percent;
        this.prodID = prodID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public List<String> getImgUrls() {
        return imgUrls;
    }

    public List<String> getAvail_size() {
        return avail_size;
    }

    public String getDiscount_percent() {
        return discount_percent;
    }

    public String getProdID() {
        return prodID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setImgUrls(List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }

    public void setAvail_size(List<String> avail_size) {
        this.avail_size = avail_size;
    }

    public void setDiscount_percent(String discount_percent) {
        this.discount_percent = discount_percent;
    }

    public void setProdID(String prodID) {
        this.prodID = prodID;
    }
}
