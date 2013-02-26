/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mdci;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author Chamale Hing
 */
public class GyroDrive {
    
 Talon[] Talons = new Talon[4];
 RobotDrive Drive = new RobotDrive(Talons[0],Talons[1],Talons[2],Talons[3]);
 Joystick[] Joysticks = new Joystick[2];
 Gyro gyro = new Gyro(1);
 double Kp = 0.03;
 
 
 public void AutoDrive(){
     while(true){
         Joysticks[0] = new Joystick(1);
         Drive.drive(Joysticks[0].getY(), gyro.getAngle()*Kp);
         Timer.delay(.004);
     }
     
            }
}
 