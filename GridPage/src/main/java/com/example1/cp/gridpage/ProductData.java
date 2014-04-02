package com.example1.cp.gridpage;

/**
 * Created by SKG on 24-Mar-14.
 */
public class ProductData {
    String title;
    String image;
    String id;
    String buy;

    public ProductData(String title, String image, String id, String buy){
        this.title = title;
        this.image = image;
        this.id = id;
        this.buy = buy;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getBuy() {
        return buy;
    }

    public String getImage() {
        return image;
    }


}
