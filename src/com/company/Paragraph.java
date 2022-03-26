package com.company;

import java.io.Serializable;

public class Paragraph implements Serializable {

    private int firstLimit;
    private int secondLimit;
    private String ownerId=null;

    Paragraph(String ownerId,int firstLimit,int secondLimit){
        this.firstLimit=firstLimit;
        this.secondLimit=secondLimit;
        this.ownerId=ownerId;
    }

    public int getFirstLimit() {
        return firstLimit;
    }

    public void setFirstLimit(int firstLimit) {
        this.firstLimit = firstLimit;
    }

    public int getSecondLimit() {
        return secondLimit;
    }

    public void setSecondLimit(int secondLimit) {
        this.secondLimit = secondLimit;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}
