package com.uav.mandiratepe.vo;

import java.io.Serializable;

public class DataAdapterVO implements Serializable {

    private String text;
    private String text2;
    private int image;
    private String associatedValue;

    public DataAdapterVO(){

    }

    public DataAdapterVO(String text, String text2){
        this.text=text;
        this.text2=text2;
    }




    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getAssociatedValue() {
        return associatedValue;
    }

    public void setAssociatedValue(String associatedValue) {
        this.associatedValue = associatedValue;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }
}
