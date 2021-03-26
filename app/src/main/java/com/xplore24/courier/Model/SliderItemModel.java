package com.xplore24.courier.Model;

public class SliderItemModel {
    private String imageUrl;

    public SliderItemModel(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public SliderItemModel() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
