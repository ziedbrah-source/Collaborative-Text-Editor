package com.company;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;

public class Notepad extends JFrame {
    JMenuBar menubar = new JMenuBar();
    JMenu addParagraph = new JMenu("add Paragraph");
    JTextArea textArea = new JTextArea();
    private String uniqueID=null;
    private JLabel label1= new JLabel();
    Notepad() throws IOException, TimeoutException {
        this.uniqueID = UUID.randomUUID().toString();
        setTitle("Collaborative text Editor.");
        setBounds(0, 0, 800, 800);
        JLayeredPane layeredPane = new JLayeredPane();


        label1.setOpaque(true);
        label1.setBackground(Color.RED);
        label1.setText("here we go biatch");
        layeredPane.setBounds(0,0,800,800);
        layeredPane.add(label1, Integer.valueOf(0));
        textArea.add(layeredPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JScrollPane scrollpane = new JScrollPane(textArea);
        setJMenuBar(menubar);
        menubar.add(addParagraph);

        Caret caret=textArea.getCaret();

        caret.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent arg0) {
                System.out.println(arg0.toString());
                int pos=textArea.getCaretPosition();
                System.out.println(pos);
                try {
                    Rectangle rectangle = textArea.modelToView( textArea.getCaretPosition() );
                    System.out.println(rectangle);
                    label1.setBounds((int)(rectangle.getX()),(int)(rectangle.getY()),50,50);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }
        });
        KeyListener listener = new KeyListener() {
            @Override
            public void keyPressed(KeyEvent event) {
                //printEventInfo("Key Pressed", event);
            }
            @Override
            public void keyReleased(KeyEvent event) {
                //printEventInfo("Key Released", event);
            }
            @Override
            public void keyTyped(KeyEvent event) {
                Obj toSend=new Obj(event.getKeyChar(),uniqueID);

/*                layeredPane.remove(label1);
                layeredPane.revalidate();
                layeredPane.repaint();*/


                Sender.send(toSend);
            }};
        textArea.addKeyListener(listener);
        add(scrollpane);
        textArea.setFont((new Font(Font.SANS_SERIF, Font.PLAIN, 20)));
        scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollpane.setBorder(BorderFactory.createEmptyBorder());
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

    }

    public void receiveInNotepad() throws IOException, TimeoutException {
        Receiver myThread=new Receiver(textArea,uniqueID);
        myThread.start();
    }

}
