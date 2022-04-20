package com.company;
import javax.swing.*;
import java.io.Serializable;
public class Position implements Serializable{
    private int x;
    private int y;
    private JLayeredPane layeredPane= null;
    private String id;
    Position(JLayeredPane layeredPane, int x, int y,String uniqueID){
        this.layeredPane=layeredPane;
        this.id=uniqueID;
        this.x=x;
        this.y=y;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}