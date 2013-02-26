/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mdci;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.mdci.GyroDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author admin
 */
public class TankDrive extends Thread{
    double JOYSTICK_DEAD_BAND = 0.09;
    Talon[] talons = new Talon[4];
    Joystick[] joysticks = new Joystick[2];
    double[] transmission = {0.45, 0.60, 0.90};
    
    
    public void run(){
        
        
        
        for (int i = 0; i < talons.length; i++) {
            talons[i] = new Talon(i+1);
        }
        
        joysticks[0] = new Joystick(1);
        joysticks[1] = new Joystick(2);
        double leftX, rightX;
        double leftY, rightY;
        int speed = 0;
        int loopCount =0 ;
        int MAXIMUM_JOYSTICK_AXISES = 5;
        double[] axis = new double[MAXIMUM_JOYSTICK_AXISES + 1];
        Gyro Gyro = new Gyro(1);
        GyroDrive Auto = new GyroDrive();
        while(true){
            
           
           loopCount = loopCount+1 ;
            
            if(joysticks[0].getRawButton(1)){
                    Auto.AutoDrive();
            }
            if(joysticks[0].getRawButton(10) && loopCount < 50){
                            if( speed < transmission.length) {
                                speed++;
                            }
                            loopCount = 0;
                        }
             if(joysticks[0].getRawButton(11) && loopCount < 50){
                            if( speed > 0 ){
                                speed--;
                                
                            }
                            loopCount = 0;
                        }
            for (int i = 0; i < axis.length; i++) {
                            if (Math.abs(axis[i]) < JOYSTICK_DEAD_BAND) {
                                axis[i] = 0;
                            }
                        }
            SmartDashboard.putNumber("Max Speed of Transmission", transmission[speed]);
            rightY = joysticks[0].getRawAxis(2) * transmission[speed];
            leftY = joysticks[0].getRawAxis(4) * transmission[speed];
            leftX = joysticks[0].getRawAxis(1) * 1;
            rightX = joysticks[0].getRawAxis(1) * 1;
            talons[0].set(leftY);   
            talons[1].set(rightY);
            talons[2].set(leftY);
            talons[3].set(rightY);
            if(joysticks[0].getRawButton(1)){
            
            
            
            
            }
            
            
            
            
            if(joysticks[0].getRawButton(5)){
            talons[0].set(leftX);   //pwm1
            talons[1].set(leftX);//pwm2
            talons[2].set(rightX);//pwm3
            talons[3].set(rightX);//pwm4
            SmartDashboard.putBoolean("Cartesian Drive", true);
            }
            else{ SmartDashboard.putBoolean("Cartesian Drive", false);
            }
            
           //.drive.mecanumDrive_Cartesian(left, right, 0,0); 
            
          if(joysticks[0].getRawButton(1)){
              if(Gyro.getAngle() != 90){
                    if(Gyro.getAngle() < 90){
                        talons[0].set(.25);   
            talons[1].set(-.25);
            talons[2].set(.25);
            talons[3].set(-.25);
                    }
                    if(Gyro.getAngle() > 90){
                    talons[0].set(-.25);   
            talons[1].set(.25);
            talons[2].set(-.25);
            talons[3].set(.25);}
                    talons[0].set(1);   
            talons[1].set(-1);
            talons[2].set(1);
            talons[3].set(-1);
              } 
          }
          else{}
            
          
          
          
          
          
          
          try{
                Thread.sleep(5);
            }
            catch(InterruptedException e){
            }
        }
    }
}
