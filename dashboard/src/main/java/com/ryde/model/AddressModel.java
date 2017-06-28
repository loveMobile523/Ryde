package com.ryde.model;

/**
 * Created by dsndw3 on 09.06.2017.
 */

public class AddressModel {
    private String title;

    public AddressModel() {}

    public AddressModel(String title) {
        this.title = title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
