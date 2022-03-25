package com.company;

import java.io.Serializable;

public class Obj implements Serializable {
    private char msg;
    private String id;

    Obj(char msg,String id){
        this.msg=msg;
        this.id=id;
    }
    public char getMsg() {
        return msg;
    }

    public String getId() {
        return id;
    }
}
