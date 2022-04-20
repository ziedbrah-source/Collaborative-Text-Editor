package com.company;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class Main {
    static ArrayList <Window> sendWindows = new ArrayList<Window>();
    public static void main(String[] args) throws IOException, TimeoutException, BadLocationException {

        int n = 4;
        for (int i =1 ;i<=n;i++)
        {
            String queueName="file"+i;
            sendWindows.add(i-1,new Window(queueName));
        }
        for (int i =1 ;i<=n;i++)
        {
            String queueName="file"+i;
            sendWindows.get(i-1).afficher(queueName);
        }
        for (int i =1 ;i<=n;i++)
        {
            String queueName="file"+i;
            sendWindows.get(i-1).receiveInNotepad();
        }
        for (int i =1 ;i<=n;i++)
        {
            String queueName="file"+i;
            sendWindows.get(i-1).sendPos();
        }

    }
}