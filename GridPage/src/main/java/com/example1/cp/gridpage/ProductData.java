package com.example1.cp.gridpage;

/**
 * Created by SKG on 24-Mar-14.
 */
public class ProductData {
    String title;
    String description;
    String image;

    public ProductData(String title,String description, String image){
        this.title = title;
        this.description = description;
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }


}
