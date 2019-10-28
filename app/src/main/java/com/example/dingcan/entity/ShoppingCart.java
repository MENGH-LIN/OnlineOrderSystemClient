package com.example.dingcan.entity;

import java.io.Serializable;
import java.util.List;

public class ShoppingCart implements Serializable {

    private int sId;
    private String sName;
    private float sPrice;
    private int sNum;
    private float sTotalPrice;

    public int getsId() {
        return sId;
    }

    public void setsId(int sId) {
        this.sId = sId;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public Float getsPrice() {
        return sPrice;
    }

    public void setsPrice(Float sPrice) {
        this.sPrice = sPrice;
    }

    public int getsNum() {
        return sNum;
    }

    public void setsNum(int sNum) {
        this.sNum = sNum;
    }

    public Float getsTotalPrice() {
        return sTotalPrice;
    }

    public void setsTotalPrice(Float sTotalPrice) {
        this.sTotalPrice = sTotalPrice;
    }

    public String toString(){
        return "ShoppingCart{" +
                "id=" + sId +
                ", name='" + sName + '\'' +
                ", num='" + sNum + '\'' +
                ", price='" + sPrice + '\'' +
                ", totalPrice=" + sTotalPrice +
                '}';
    }
}

