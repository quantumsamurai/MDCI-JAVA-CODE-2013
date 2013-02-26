/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mdci;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 *
 * @author admin
 */
public class EncoderTest extends Thread{
    
    Encoder left = new Encoder(1,2);
    Encoder right = new Encoder(3,4);
    double leftspeed = left.getDistance()/360; //rpm left encoder
    double rightspeed = right.getDistance()/360; // rpm right encoder
    double leftdistance = rightspeed*Math.PI*8;
    double rightdistance = rightspeed*Math.PI*8;
    
    
    double Base = (leftspeed + rightspeed) /2;
    
    public void run(){
        left.start();
        right.start();
        left.reset();
        right.reset();
        while(true){
            
            System.out.println("Left Encoder: " + leftspeed + "\tRight Encoder: " + rightspeed );
            System.out.println("Left Encoder Distance:" + leftdistance + "\tRight Encoder Distance:" + rightdistance);
            
            SmartDashboard.putNumber("RPM Base", Base);
            
            
            try{
                Thread.sleep(100);
            }
            catch(InterruptedException e){
                
            }
        }
    }
}
