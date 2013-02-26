/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mdci;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * 
 * @author admin
 */
public class Sonars extends Thread{
    
    public static double spread = 231.775; // in milimeters
    AnalogChannel left;
    AnalogChannel right;
    Joystick stick = new Joystick(1);
     double leftDistance = left.getVoltage()/.000977;
        double rightDistance = right.getVoltage()/.000977;
        
    public Sonars(){
        left = new AnalogChannel(2);
        right = new AnalogChannel(3);
    }
    
    public void run(){
        while(true){
            if(stick.getRawButton(5)){
                System.out.println("Robot Left Sensor:" + leftDistance + "\tRobot Right Sensor:" + rightDistance);
                //System.out.println("Robot Angle to wall: " + getHeadingAngleDegrees());
                SmartDashboard.putNumber("Left Distance", leftDistance);
        SmartDashboard.putNumber("Right Distance", rightDistance);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
               // System.out.println("Robot Angle to wall: " + getHeadingAngleDegrees());
            }
        }
    }
    
    public double getHeadingAngleDegrees(){
        double arctan = 0.0;
        
       
        double r = Math.abs(leftDistance - rightDistance) / spread ;
        SmartDashboard.putNumber("Distance in mm", r);
        for(int i = 0; i < 100000; i++){
            arctan += (pow(-1, i) * pow(r, 2 * i + 1)) / (2 * i + 1);
        }
        
        if(leftDistance < rightDistance)
            arctan *= -1;
        
        return Math.toDegrees(arctan);
    }
    
    private double pow(double a, int b){
        double pow = 1;
        
        for(int i = 0; i < b; i++){
            pow *= a;
        }
        
        return pow;
    }
}
