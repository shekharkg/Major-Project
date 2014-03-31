package com.example1.cp.gridpage;

/**
 * Created by SKG on 24-Mar-14.
 */
public class ProductData {
    String title;
    String image;
    String id;

    public ProductData(String title, String image, String id){
        this.title = title;
        this.image = image;
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getImage() {
        return image;
    }


}
