package com.example1.cp.gridpage;

/**
 * Created by SKG on 04-Apr-14.
 */
public class ProductDataBrands {
    String brand_names, brand_url, brand_logo;

    public ProductDataBrands(String brand_names, String brand_url, String brand_logo) {
        this.brand_names = brand_names;
        this.brand_url = brand_url;
        this.brand_logo = brand_logo;
    }

    public String getBrand_names() {
        return brand_names;
    }

    public String getBrand_url() {
        return brand_url;
    }

    public String getBrand_logo() {

        return brand_logo;
    }

    public void setBrand_names(String brand_names) {
        this.brand_names = brand_names;
    }

    public void setBrand_url(String brand_url) {
        this.brand_url = brand_url;
    }

    public void setBrand_logo(String brand_logo) {
        this.brand_logo = brand_logo;
    }
}
