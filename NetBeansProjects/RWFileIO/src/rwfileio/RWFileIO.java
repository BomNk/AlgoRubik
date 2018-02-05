/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rwfileio;

/**
 *
 * @author tanap
 */


import java.io.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RWFileIO {

    /**
     * @param args the command line arguments
     */
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
            Logger.getLogger(RWFileIO.class.getName()).log(Level.SEVERE, null, ex);
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
    
    
    public static void main(String[] args) throws IOException {
        boolean b;
        RWFileIO test1 = new RWFileIO();
       b = test1.Read_File();
       System.out.println("Status is " + b);
       test1.Start();

// TODO code application logic here
    }
    
    public boolean Read_File() throws FileNotFoundException, IOException{
       boolean ans = true;
       String t= " ";
        try {
         in = new FileInputStream("D:\\Face.txt");
         int c;
         while ((c = in.read()) != -1) {
              System.out.println("c = " + (char)c);
            if(c == 32){
                System.out.println(" char = space");
                 ans =  false;
                 break;
            }
            System.out.println("text = " + (char)c);
            Face =  Face + (char)c;
            ans = true;
            //out.write(c);    
       }
          System.out.println("Face = " + Face);
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
         in = new FileInputStream("D:\\Face.txt");
         out = new FileOutputStream("D:\\Result.txt");
         
         int c;
         while ((c = in.read()) != -1) {
            System.out.println("text = " + c);
            out.write(c);
         }
      }finally {
         if (in != null) {
            in.close();
         }
         if (out != null) {
            out.close();
         }
      }

    }
    
    
    
    
    
}
