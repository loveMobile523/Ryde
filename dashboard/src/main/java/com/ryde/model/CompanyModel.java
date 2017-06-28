package com.ryde.model;

/**
 * Created by dsndw3 on 07.06.2017.
 */

public class CompanyModel {
    private String name;
    private float fare;
    private float farePlus;
    private boolean checked;
    private int logoIndex;
    private String time;

    public CompanyModel() {}

    public CompanyModel(String name, float fare, float farePlus, boolean checked) {
        this.name = name;
        this.fare = fare;
        this.farePlus = farePlus;
        this.checked = checked;
    }

    public CompanyModel(String name, float fare, float farePlus, boolean checked, int logoIndex) {
        this.name = name;
        this.fare = fare;
        this.farePlus = farePlus;
        this.checked = checked;
        this.logoIndex = logoIndex;
    }

    public CompanyModel(String name, float fare, float farePlus, boolean checked, int logoIndex, String time) {
        this.name = name;
        this.fare = fare;
        this.farePlus = farePlus;
        this.checked = checked;
        this.logoIndex = logoIndex;
        this.time = time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setFare(float fare) {
        this.fare = fare;
    }

    public float getFare() {
        return fare;
    }

    public void setFarePlus(float farePlus) {
        this.farePlus = farePlus;
    }

    public float getFarePlus() {
        return farePlus;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setLogoIndex(int logoIndex) {
        this.logoIndex = logoIndex;
    }

    public int getLogoIndex() {
        return logoIndex;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }
}
