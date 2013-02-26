/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mdci;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author admin
 */
public class FiringSystem extends Thread{
    
    Talon shooter = new Talon(5);
    Relay kicker = new Relay(1);
    Relay hopper = new Relay(2);
    Joystick joystick = new Joystick(2);
    
    
    public void run(){
        while(true){
            
            if (joystick.getRawButton(10)) {
                hopper.setDirection(Relay.Direction.kReverse);
                hopper.set(Relay.Value.kOn);
                SmartDashboard.putString("Frisbee", "Loading");
            } else {
                hopper.set(Relay.Value.kOff);
                SmartDashboard.putString("Frisbee", "Not Loading");
            }

            if (joystick.getRawButton(1)) {
                kicker.setDirection(Relay.Direction.kReverse);
                kicker.set(Relay.Value.kOn);
                SmartDashboard.putString("Kicker", "Going Forward");
            }

            if (joystick.getRawButton(8)) {
                shooter.set(-1);
                SmartDashboard.putString("Shooter", "Starting");
                Timer.delay(2.0);
                SmartDashboard.putString("Shooter", "Full Speed");
            } else {
                shooter.set(0);
                SmartDashboard.putString("Shooter", "Off");
            }


            if (joystick.getRawButton(3)) {
                kicker.setDirection(Relay.Direction.kForward);
                kicker.set(Relay.Value.kOn);
                SmartDashboard.putString("Kicker", "Going Backward");
            } else if (!joystick.getRawButton(7)) {
                kicker.set(Relay.Value.kOff);
            }
            
            
            
            if(joystick.getRawButton(4)){
                shooter.set(-1);
                hopper.set(Relay.Value.kOn);
                hopper.setDirection(Relay.Direction.kReverse);
                Timer.delay(2.5);
                shooter.set(-1);
                kicker.set(Relay.Value.kOn);
                kicker.setDirection(Relay.Direction.kForward);
                Timer.delay(3.0);
                shooter.set(0);
                kicker.set(Relay.Value.kOn);
                kicker.setDirection(Relay.Direction.kReverse);
                Timer.delay(1.5);
                
            }




            try {
                Thread.sleep(15);
            } catch (Exception e) {
            }
        }
    }
}
