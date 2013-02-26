/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mdci;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author admin
 */
public class Climbing extends Thread{
    Joystick joystick = new Joystick(2);
    Talon Arm = new Talon(6);
    Relay Winch = new Relay(4);
    
    
     public void run(){
     while(true){
         
      if(joystick.getRawButton(1)){
            Arm.set(joystick.getY());
            SmartDashboard.putString("Climber", "On" );
            }
      
      else Arm.set(0);
            SmartDashboard.putString("Climber", "Off");
      
      
      if(joystick.getRawButton(2)){
               Winch.set(Relay.Value.kOn); 
               SmartDashboard.putString("Winch", "Activated");
               Winch.setDirection(Relay.Direction.kReverse);  }
      
      
      
      
      if(joystick.getRawButton(3)){
                Winch.set(Relay.Value.kOn);
                SmartDashboard.putString("Winch", "Activated");
                Winch.setDirection(Relay.Direction.kForward); }
      else {
             Winch.set(Relay.Value.kOff);
         }
            SmartDashboard.putString("Winch", "Off");
         
         try{
                Thread.sleep(25);
            }
            catch(Exception e){
                
            }}
     }
}
