/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timer;

import java.util.Timer;
import java.util.TimerTask;

public class TT {

    int secondsPassed = 0;
    
    Timer Mytimer = new Timer();
    TimerTask task = new TimerTask(){
        public void run(){
            secondsPassed++;
            System.out.println("SecondsPassed : " + secondsPassed);
            if(secondsPassed == 10 ){
               Mytimer.cancel();
               task.cancel();
               System.out.println("Stop");
            }
        }
        
    };
    
    public void Start(){
        Mytimer.scheduleAtFixedRate(task,1000,1000);
    }
    
    
    public static void main(String[] args){
        TT mytt = new TT();
        mytt.Start();
    }
  
    
}
