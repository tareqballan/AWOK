package com.example.awok.model;

import com.google.gson.annotations.SerializedName;

public class Car {
    @SerializedName("carID")
    private int carID;

    @SerializedName("image")
    private String image;

    @SerializedName("makeEn")
    private String makeEn;

    @SerializedName("makeAr")
    private String makeAr;

    @SerializedName("modelEn")
    private String modelEn;

    @SerializedName("modelAr")
    private String modelAr;

    @SerializedName("bodyEn")
    private String bodyEn;

    @SerializedName("bodyAr")
    private String bodyAr;

    @SerializedName("year")
    private int year;

    @SerializedName("AuctionInfo")
    private AuctionInfo auctionInfo;


    public void setAuctionInfo(AuctionInfo auctionInfo) {
        this.auctionInfo = auctionInfo;
    }

    public void setCarID(int carID) {
        this.carID = carID;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setMakeEn(String makeEn) {
        this.makeEn = makeEn;
    }

    public void setModelEn(String modelEn) {
        this.modelEn = modelEn;
    }

    public void setBodyEn(String bodyEn) {
        this.bodyEn = bodyEn;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMakeAr(String makeAr) {
        this.makeAr = makeAr;
    }

    public void setModelAr(String modelAr) {
        this.modelAr = modelAr;
    }

    public void setBodyAr(String bodyAr) {
        this.bodyAr = bodyAr;
    }

    public int getCarID() {
        return carID;
    }

    public String getImage() {
        return image;
    }

    public String getMakeEn() {
        return makeEn;
    }

    public String getModelEn() {
        return modelEn;
    }

    public String getBodyEn() {
        return bodyEn;
    }

    public int getYear() {
        return year;
    }

    public String getMakeAr() {
        return makeAr;
    }

    public String getModelAr() {
        return modelAr;
    }

    public String getBodyAr() {
        return bodyAr;
    }

    public AuctionInfo getAuctionInfo() {
        return auctionInfo;
    }

    // The card title is a combination of multiple fields
    public String getEnglishCardTitle(){
        return makeEn+" "+modelEn+" "+bodyEn+" "+year;
    }

    public String getArabicCardTitle(){ return makeAr + " " + modelAr + " " +bodyAr + " " + year;}



    // For Mapping Nested Objects
    public class AuctionInfo{
        @SerializedName("lot")
        private String lot;

        @SerializedName("bids")
        private String bids;

        @SerializedName("currentPrice")
        private String currentPrice;

        @SerializedName("currencyEn")
        private String currencyEn;

        @SerializedName("currencyAr")
        private String currencyAr;


        public String getLot() {
            return lot;
        }

        public String getBids() {
            return bids;
        }

        public String getCurrencyEn() {
            return currencyEn;
        }

        public String getCurrentPrice() {
            return currentPrice;
        }

        public String getCurrencyAr() {
            return currencyAr;
        }

        public void setLot(String lot) {
            this.lot = lot;
        }

        public void setBids(String bids) {
            this.bids = bids;
        }

        public void setCurrencyEn(String currencyEn) {
            this.currencyEn = currencyEn;
        }

        public void setCurrentPrice(String currentPrice) {
            this.currentPrice = currentPrice;
        }

        public void setCurrencyAr(String currencyAr) {
            this.currencyAr = currencyAr;
        }
    }
}
