/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rubik;

import cs.min2phase.Tools;
import cs.min2phase.Search;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Color;
import java.awt.event.*;

import java.io.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.*;

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//A simple GUI example to demonstrate how to use the package org.kociemba.twophase

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free for non-commercial
 * use. If Jigloo is being used commercially (ie, by a corporation, company or business for any purpose whatever) then
 * you should purchase a license for each developer using Jigloo. Please visit www.cloudgarden.com for details. Use of
 * Jigloo implies acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR THIS MACHINE, SO
 * JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class MainRubik extends javax.swing.JFrame {

    // +++++++++++++These variables used only in the GUI-interface+++++++++++++++++++++++++++++++++++++++++++++++++++++++
    private static final long serialVersionUID = 1L;
    private JButton[][] facelet = new JButton[6][9];
    private final JButton[] colorSel = new JButton[6];
    private final int FSIZE = 45;
    private final int[] XOFF = { 3, 6, 3, 3, 0, 9 };// Offsets for facelet display
    private final int[] YOFF = { 0, 3, 3, 6, 3, 3 };
    private final Color[] COLORS = { Color.white, Color.red, Color.green, Color.yellow, Color.orange, Color.blue };
    private JTextPane jTextPane1;
    private JCheckBox checkBoxShowStr;
    private JButton buttonRandom;
    private JCheckBox checkBoxUseSep;
    private JCheckBox checkBoxInv;
    private JCheckBox checkBoxShowLen;

    private JButton Solve;
    private JLabel jLabel2;
    private JLabel jLabel1;
    private JSpinner spinnerMaxMoves;
    private JSpinner spinnerTimeout;
    private Color curCol = COLORS[0];
    private int maxDepth = 21, maxTime = 5;
    boolean useSeparator = true;
    boolean showString = false;
    boolean inverse = true;
    boolean showLength = true;
    Search search = new Search();

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    boolean Bom;
    String Face = "";
    String Result;
    FileInputStream in = null;
    FileOutputStream out = null;
    Timer Mytimer = new Timer();
    int secondsPassed = 0;
    TimerTask task = new TimerTask(){
        //RWFileIO tt = new RWFileIO();
    @Override
    public void run(){
            secondsPassed++;
            System.out.println("SecondsPassed : " + secondsPassed);
        try {
            Bom = Read_File();
            System.out.println("Status file 1 = " + Bom);
            
        } catch (IOException ex) {
            Logger.getLogger(MainRubik.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(Bom == true ){
               Mytimer.cancel();
               task.cancel();
               System.out.println("Stop");
            }
        }
   
    };
    
    public void Start(){
        Mytimer.scheduleAtFixedRate(task,1000,1000);
    }
    ///////////////////////////////////////////////////////////////////////////////////
    
    public static void main(String[] args) {
        /*
        String cube = "bom tanapon Nk";
        
        MainProgram x = new MainProgram();
        x.Write_File("d:\\Rubik.txt",cube);
        
        x.Read_File("d:\\Rubik.txt");
        */
        
        String fileName = "m2p" + (Search.USE_TWIST_FLIP_PRUN ? "T" : "") + ".data";
        try {
            DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(fileName)));
            Tools.initFrom(dis);
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        if (!Search.isInited()) {
            Search.init();
            try {
                DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));
                Tools.saveTo(dos);
                dos.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainRubik inst;
                try {
                    inst = new MainRubik();
                    inst.setLocationRelativeTo(null);
                    inst.setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(MainRubik.class.getName()).log(Level.SEVERE, null, ex);
                }
               
            }
        });   
        
    }

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    public MainRubik() throws IOException {
        super();
        boolean b ;
        b = Read_File();
        System.out.println("Status is " + b);
        Write_File();
        
       // System.out.println("Status is " + b);
       Start();
        
        //initGUI();
    }
    
    public void Find_Result(){
         
        String cubeString = Face;//"DUFLUDBLRDBLURDURLDBFLFBUFRLRBRDUUFFBFRLLUFBBDFLRBDUDR";
        //System.out.println("Test Print  = " + cubeString);  // Bom Comment
        if (showString) {
            JOptionPane.showMessageDialog(null, "Cube Definiton String: " + cubeString);
        }
        
        int mask = 0;
        mask |= useSeparator ? Search.USE_SEPARATOR : 0;
        mask |= inverse ? Search.INVERSE_SOLUTION : 0;
        mask |= showLength ? Search.APPEND_LENGTH : 0;
        long t = System.nanoTime();
        String result = search.solution(cubeString, maxDepth, 100, 0, mask);;
        long n_probe = search.numberOfProbes();
        Result = result;
        System.out.println("Result  = " + result.toString());  // Bom Comment
        // ++++++++++++++++++++++++ Call Search.solution method from package org.kociemba.twophase ++++++++++++++++++++++++
        while (result.startsWith("Error 8") && ((System.nanoTime() - t) < maxTime * 1.0e9)) {
            result = search.next(100, 0, mask);
            n_probe += search.numberOfProbes();
        }
        t = System.nanoTime() - t;

        // +++++++++++++++++++ Replace the error messages with more meaningful ones in your language ++++++++++++++++++++++
        if (result.contains("Error")) {
            switch (result.charAt(result.length() - 1)) {
            case '1':
                result = "There are not exactly nine facelets of each color!";
                break;
            case '2':
                result = "Not all 12 edges exist exactly once!";
                break;
            case '3':
                result = "Flip error: One edge has to be flipped!";
                break;
            case '4':
                result = "Not all 8 corners exist exactly once!";
                break;
            case '5':
                result = "Twist error: One corner has to be twisted!";
                break;
            case '6':
                result = "Parity error: Two corners or two edges have to be exchanged!";
                break;
            case '7':
                result = "No solution exists for the given maximum move number!";
                break;
            case '8':
                result = "Timeout, no solution found within given maximum time!";
                break;
            }
            JOptionPane.showMessageDialog(null, result, Double.toString((t / 1000) / 1000.0) + " ms | " + n_probe + " probes", JOptionPane.INFORMATION_MESSAGE);
        } else {
            int solLen = (result.length() - (useSeparator ? 3 : 0) - (showLength ? 4 : 0)) / 3;
            //spinnerMaxMoves.setValue(solLen - 1);
            //jTextPane1.setText(String.format("%s\n" /*, %s ms, %d probes\n*/, result/*, Double.toString((t / 1000) / 1000.0), n_probe*/) + jTextPane1.getText());
            //jTextPane1.requestFocusInWindow();
            //jTextPane1.select(0, result.length());
        }
    }

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
  

    // ++++++++++++++++++++++++++++++++++++ End initGUI +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    // +++++++++++++++++++++++++++++++ Generate cube from GUI-Input and solve it ++++++++++++++++++++++++++++++++++++++++
   
    
    
    
    //////READ  AND  WRITE  FILE///////////////////////////////////////////////////////////
 public boolean Read_File() throws FileNotFoundException, IOException{
       boolean ans = true;
       String t= " ";
        try {
         in = new FileInputStream("D:\\Face.txt");
        int c;
         while ((c = in.read()) != -1) {
            //System.out.println("c = " + (char)c);
            /* 
            if(c == 32){
                System.out.println(" char = space");
                 ans =  false;
                 break;
            }
            */
            //System.out.println("SumFace " + (char)c);
            Face =  Face + (char)c;
            //ans = true;
            //out.write(c);    
       }
       if(Face.length() == 0){
            ans =  false;
            System.out.println("Face = " + Face.length());
            
       }
       else{
            ans = true;
            System.out.println("Face = " + Face);
            Find_Result();
       }
       
      }finally {
         if (in != null) {
            in.close();
         }
         
      }
     //System.out.println("Status = " + Face);
      return ans; 
     

    }
    public void Write_File() throws FileNotFoundException, IOException{
        try {
         out = new FileOutputStream("D:\\Result.txt");
         StringBuilder sb = new StringBuilder();
         char[] letter = Result.toCharArray();
         System.out.println("Letter = " + letter[0]);
         int c;
         for(int i=0;i<letter.length;i++){
            System.out.println("text = " + (int)letter[i]);
            out.write((int)letter[i]);
         }
         // tanapon ninket
      }finally {
         
         if (out != null) {
            out.close();
         }
      }

    }
    
}
