package com.company;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

class Window extends JFrame implements DocumentListener, ActionListener {

    // JFrame
    JFrame f;
    boolean test=true;
    //String content;
    String queueName;
    JScrollPane scroll;
    private String uniqueID=null;
    HashMap<String, JLabel> map = new HashMap<>();
    private JLayeredPane layeredPane=null;
    // label to display text
    JLabel l;

    // text area
    JTextArea jt;


    public Window(String queueName) {
        this.queueName = queueName;
        this.uniqueID = UUID.randomUUID().toString();
        layeredPane = new JLayeredPane();
        layeredPane.setBounds(0,0,800,800);


        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void afficher(String titre) {
        this.
                // create a new frame to store text field and button
                f = new JFrame(titre);
        // create a text area, specifying the rows and columns
        jt = new JTextArea(20, 40);
        jt.add(layeredPane);
        scroll = new JScrollPane(jt);


        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jt.getDocument().addDocumentListener(this);
        JPanel p = new JPanel();
        JButton button = new JButton("Click here to let others write!");
        JButton button1 = new JButton("Your Id is "+getUniqueID());
        p.add(button1);
        button.addActionListener(this);
        // Add button to JPanel
        p.add(button);
        // And JPanel needs to be added to the JFrame itself!
        this.getContentPane().add(p);
        // add the text area and button to panel


        p.add(scroll);

        //  p.add(jt);
        f.add(p);
        f.pack();

        // set the size of frame


        f.setSize(500, 400);

        f.setVisible(true);
        Caret caret=getJt().getCaret();
        caret.addChangeListener(new ChangeListener(){

            @Override            public void stateChanged(ChangeEvent arg0) {
                System.out.println(arg0.toString());
                int pos=getJt().getCaretPosition();
                System.out.println(pos);
                try {
                    Rectangle rectangle = getJt().modelToView( getJt().getCaretPosition() );
                    Position currentPosition=new Position(layeredPane,(int)rectangle.getX(),(int)rectangle.getY()+(int)rectangle.getHeight(),uniqueID);
                    System.out.println(rectangle);
                    Send.sendPosition(currentPosition);

                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }


        });
        try {
            //Reception.recevoir(t.get(queue), queue);
            Receive.recevoir(jt, queueName,this);
        } catch (Exception e) {

        }
    }


    @Override
    public void insertUpdate(DocumentEvent evt) {

        if(!test)
            return;
        int startOffset = evt.getOffset();
        int endOffset = startOffset + evt.getLength();

        String modified = "";
        try {
            modified = jt.getText(startOffset, endOffset - startOffset);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        //System.out.println("Start offset: " + startOffset);
        // System.out.println("end offset: " + endOffset);
        // System.out.println("Insertion: " + modified);
        String msg =modified;
        try {
            //Envoi.envoyer(msg, queueName,startOffset,endOffset,"i");
            Send.envoyer(msg, queueName,startOffset,endOffset,"i");
        } catch (Exception exception) {

        }

    }

    @Override
    public void removeUpdate(DocumentEvent evt) {
        if(!test)
            return;
        int startOffset = evt.getOffset();
        int endOffset = startOffset + evt.getLength();

        // System.out.println("Start offset: " + startOffset);
        //  System.out.println("end offset: " + endOffset);
        //  System.out.println("Delete");
        String msg = "";
        try {
            //Envoi.envoyer(msg, queueName,startOffset,endOffset,"d");
            Send.envoyer(msg, queueName,startOffset,endOffset,"d");
        } catch (Exception exception) {

        }

    }

    public JTextArea getJt() {
        return jt;
    }
    public void DrawRectangleInPosition(Position pos){
        if (map.containsKey(pos.getId())) {
            JLabel j=map.get(pos.getId());
            j.setBounds(pos.getX(),pos.getY(),50,50);
        }else{
            JLabel label= new JLabel();
            label.setOpaque(true);
            label.setBackground(Color.RED);
            label.setText(pos.getId());
            label.setBounds(pos.getX(),pos.getY(),50,50);
            layeredPane.add(label, Integer.valueOf(0));
            map.put(pos.getId(),label);
        }
    }
    public void sendPos() throws BadLocationException {
        Rectangle rectangle = getJt().modelToView( getJt().getCaretPosition() );
        Position currentPosition=new Position(layeredPane,(int)rectangle.getX(),(int)rectangle.getY()+(int)rectangle.getHeight(),uniqueID);
        System.out.println(rectangle);
        Send.sendPosition(currentPosition);
    }
    public void receiveInNotepad() throws IOException, TimeoutException {

        PositionReceiver posRec=new PositionReceiver(this);
        posRec.receivePosition();
        ClearReceiver clearRec=new ClearReceiver(this);
        clearRec.receiveClear();


    }
    @Override
    public void changedUpdate(DocumentEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

            System.out.println("i'm in action Performed");
            Send.sendClear(new Clear(this.getUniqueID()));


    }
}