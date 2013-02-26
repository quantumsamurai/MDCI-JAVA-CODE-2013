/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mdci;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;

/**
 *
 * @author admin
 */
public class Arcade extends Thread{
    RobotDrive drive = new RobotDrive(2,3,1,4);
    Joystick joystick = new Joystick(1);
    Gyro gyro = new Gyro(1);
    
    public void run(){
        while(true){
            //drive.arcadeDrive(new Joystick(1));
            drive.mecanumDrive_Cartesian(joystick.getX(), joystick.getY(), joystick.getZ(), gyro.getAngle());
        }
    }
}
