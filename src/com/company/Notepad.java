package com.company;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.*;

public class Notepad extends JFrame  implements ActionListener{
    JMenuBar menubar = new JMenuBar();
    private JLayeredPane layeredPane=null;
    public String getUniqueID() {
        return uniqueID;
    }
    JMenu file = new JMenu("File");
    JMenuItem addParagraph = new JMenuItem("AddPar");
    JTextArea textArea = new JTextArea();
    private String uniqueID=null;
    HashMap<String, JLabel> map = new HashMap<>();
    HashMap<String, Paragraph>paragraphMap = new HashMap<>();
    Notepad() throws IOException, TimeoutException, BadLocationException {
        this.uniqueID = UUID.randomUUID().toString();
        setTitle("Collaborative text Editor.");
        setBounds(0, 0, 800, 800);
        layeredPane = new JLayeredPane();
        layeredPane.setBounds(0,0,800,800);
        textArea.add(layeredPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JScrollPane scrollpane = new JScrollPane(textArea);
        setJMenuBar(menubar);
        menubar.add(file);
        file.add(addParagraph);

/*        ((AbstractDocument)textArea.getDocument()).setDocumentFilter(new DocumentFilter() {

            private boolean allowChange(int offset) {
                //int offsetLastLine = textArea.getLineCount() == 0 ? 0 : textArea.getLineStartOffset(textArea.getLineCount() - 1);
                Paragraph p=paragraphMap.get(uniqueID);
                if((offset>=p.getFirstLimit()) && (offset<=p.getSecondLimit())){
                    return true;
                }else return false;
            }

            @Override
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                if (allowChange(offset)) {
                    super.remove(fb, offset, length);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (allowChange(offset)) {
                    super.replace(fb, offset, length, text, attrs);

                }
            }

            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (allowChange(offset)) {
                    Paragraph p=paragraphMap.get(uniqueID);
                    p.setSecondLimit(p.getSecondLimit()+1);
                    System.out.println("SECOND LIMIT HERE "+ p.getSecondLimit());
                    Sender.sendParagraphInfo(p);
                    super.insertString(fb, offset, string, attr);
                }
            }



        });*/
        addParagraph.addActionListener(this);
        textArea.setEnabled(false);
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

                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }
        });


        KeyListener listener = new KeyListener() {
            @Override
            public void keyPressed(KeyEvent event) {
                Paragraph p=paragraphMap.get(uniqueID);
                int offset=event.getKeyLocation();
                if(!(offset>=p.getFirstLimit()) || !(offset<=p.getSecondLimit())){
                    textArea.getInputMap().put(KeyStroke.getKeyStroke(event.getKeyChar()),"doNothing");
                }

            }
            @Override
            public void keyReleased(KeyEvent event) {
                //printEventInfo("Key Released", event);
            }
            @Override
            public void keyTyped(KeyEvent event) {
                Obj toSend=new Obj(event.getKeyChar(),uniqueID);
                int offset=event.getKeyLocation();
                Paragraph p=paragraphMap.get(uniqueID);
                if((offset>=p.getFirstLimit()) && (offset<=p.getSecondLimit())){
                    p.setSecondLimit(p.getSecondLimit()+1);
                    Sender.sendParagraphInfo(p);

                }

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
        ParagraphInfoReceiver paragraphInfoReceiver=new ParagraphInfoReceiver(uniqueID,paragraphMap);
        posRec.receivePosition();
        msgRec.receiveMsg(map);
        paragraphInfoReceiver.receiveParagraph();

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
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("AddPar")) {

            int offsetLastLine=textArea.getSelectionEnd();
            Paragraph p=null;
            if(offsetLastLine>0){
                textArea.append("\n\n");
                p=new Paragraph(uniqueID,offsetLastLine+2,offsetLastLine+2);
            }
            System.out.println(offsetLastLine);
            p=new Paragraph(uniqueID,offsetLastLine,offsetLastLine);
            textArea.setEnabled(true);
            paragraphMap.put(uniqueID,p);
            Sender.sendParagraphInfo(p);

        }
    }
    public void sendPos() throws BadLocationException {
        Rectangle rectangle = textArea.modelToView( textArea.getCaretPosition() );
        Position currentPosition=new Position(layeredPane,(int)rectangle.getX(),(int)rectangle.getY()+(int)rectangle.getHeight(),uniqueID);
        System.out.println(rectangle);
        Sender.sendPosition(currentPosition);
    }

}
