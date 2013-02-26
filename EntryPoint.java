/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.mdci;

import edu.wpi.first.wpilibj.SimpleRobot;
import edu.mdci.GyroDrive;
import java.util.Vector;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class EntryPoint extends SimpleRobot {
    private Vector threads = new Vector();
    private boolean running = false;
    GyroDrive Auto = new GyroDrive();
    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {
        Auto.AutoDrive();
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        //MainRobot robot = new MainRobot(this);
        System.out.println("Starting up...");
        running = true;
        
        //threads.addElement(robot.newCartDriveThread());
        //threads.addElement(robot.newServoTestThread());        threads.addElement(new Thread(new TankDrive()));

        //threads.addElement(robot.newTankDriveThread());
        //threads.addElement(robot.newEncoderOutputThread());
        
        threads.addElement(new Thread(new TankDrive()));
        threads.addElement(new Thread(new EncoderTest()));
        threads.addElement(new Thread(new FiringSystem()));
        threads.addElement(new Thread(new Sonars()));
        threads.addElement(new Thread(new Climbing()));
        
        for(int i = 0; i < threads.size(); i++){
            ((Thread) threads.elementAt(i)).start();
        }
        
    }
    
    public void robotInit(){

    }
    
    public void disabled(){
        //running = false; //informs all threads that it is their last execution cycle
    }
    
    public boolean running(){
        return running;
    }
}
