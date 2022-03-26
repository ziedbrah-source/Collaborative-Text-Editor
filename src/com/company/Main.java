package com.company;

import javax.swing.*;
import java.util.ArrayList;

public class Main {
    static ArrayList<Notepad> notepadArrayList = new ArrayList<Notepad>();
    public static void main(String[] args)throws Exception {
	// write your code here
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        //.setVisible(true)
        for(int i=0;i<2;i++){
            notepadArrayList.add(new Notepad());
        }

        for (Notepad num : notepadArrayList) {
            num.setVisible(true);
            num.receiveInNotepad();
        }
        for (Notepad num : notepadArrayList) {
            num.sendPos();
        }

    }
}
