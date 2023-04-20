package com.bct.fuelpay.model;

public class ModelTransactions {

    private String dateTime;
    private String tranId;
    private String Product;
    private String Amount;
    private String pump;
    private String Volume;
    private String price;
    private String mop;
    private String attandant;

    public ModelTransactions() {
    }

    public ModelTransactions(String dateTime, String tranId, String product, String amount, String pump, String volume) {
        this.dateTime = dateTime;
        this.tranId = tranId;
        Product = product;
        Amount = amount;
        this.pump = pump;
        this.Volume = volume;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMop() {
        return mop;
    }

    public void setMop(String mop) {
        this.mop = mop;
    }

    public String getAttandant() {
        return attandant;
    }

    public void setAttandant(String attandant) {
        this.attandant = attandant;
    }

    public String getVolume() {
        return Volume;
    }

    public void setVolume(String volume) {
        Volume = volume;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTranId() {
        return tranId;
    }

    public void setTranId(String tranId) {
        this.tranId = tranId;
    }

    public String getProduct() {
        return Product;
    }

    public void setProduct(String product) {
        Product = product;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getPump() {
        return pump;
    }

    public void setPump(String pump) {
        this.pump = pump;
    }
}
