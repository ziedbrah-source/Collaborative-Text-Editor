package com.company;

import java.io.Serializable;

public class Clear  implements Serializable {
    private String id;
    Clear(String id){
        this.id=id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {

        return id;
    }
}
