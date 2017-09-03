package com.win.service;

import com.win.slots.backend.domain.sale_events.SaleEventItemEntityPK;


public class ItemDTO {

    private boolean foundMongo;
    private boolean foundPostgre;
    private int saleEventType;
    private String type;
    private String name;
    SaleEventItemEntityPK saleEventItemEntityPK;

    public ItemDTO(){
        foundMongo = false;
        foundPostgre = false;
        saleEventType = 0;
        type = "";
        name = "";
        saleEventItemEntityPK = null;
    }

    public boolean isFoundMongo() {
        return foundMongo;
    }

    public void setFoundMongo(boolean foundMongo) {
        this.foundMongo = foundMongo;
    }

    public boolean isFoundPostgre() {
        return foundPostgre;
    }

    public void setFoundPostgre(boolean foundPostgre) {
        this.foundPostgre = foundPostgre;
    }

    public int getSaleEventType() {
        return saleEventType;
    }

    public void setSaleEventType(int saleEventType) {
        this.saleEventType = saleEventType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SaleEventItemEntityPK getSaleEventItemEntityPK() {
        return saleEventItemEntityPK;
    }

    public void setSaleEventItemEntityPK(SaleEventItemEntityPK saleEventItemEntityPK) {
        this.saleEventItemEntityPK = saleEventItemEntityPK;
    }
}
