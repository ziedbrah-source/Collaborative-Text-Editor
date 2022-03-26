package com.company;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;

public class Notepad extends JFrame {
    JMenuBar menubar = new JMenuBar();
    private JLayeredPane layeredPane=null;
    public String getUniqueID() {
        return uniqueID;
    }
    JMenu addParagraph = new JMenu("add Paragraph");
    JTextArea textArea = new JTextArea();
    private String uniqueID=null;
    HashMap<String, JLabel> map = new HashMap<>();
    Notepad() throws IOException, TimeoutException  {
        this.uniqueID = UUID.randomUUID().toString();
        setTitle("Collaborative text Editor.");
        setBounds(0, 0, 800, 800);
        layeredPane = new JLayeredPane();
        layeredPane.setBounds(0,0,800,800);
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
                    Position currentPosition=new Position(layeredPane,(int)rectangle.getX(),(int)rectangle.getY()+(int)rectangle.getHeight(),uniqueID);
                    System.out.println(rectangle);
                    Sender.sendPosition(currentPosition);


                    //label1.setBounds((int)(rectangle.getX()),(int)(rectangle.getY()),50,50);
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
                layeredPane.repaint();
*/



                Sender.sendMsg(toSend);
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
        MsgReceiver msgRec=new MsgReceiver(textArea,uniqueID);
        PositionReceiver posRec=new PositionReceiver(this);
        posRec.receivePosition();
        //myThread.start();
        msgRec.receiveMsg();

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

}
