/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mdci;

import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.CriteriaCollection;
import edu.wpi.first.wpilibj.image.NIVision.MeasurementType;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author admin
 */
public class Camera extends Thread{

   
   AxisCamera camera = AxisCamera.getInstance("10.16.1.11");
    Joystick[] Joysticks = new Joystick[2];
   CriteriaCollection cc;
   ColorImage ColorImage;
   DriverStationLCD b_LCD = DriverStationLCD.getInstance();
   
  
   
   

    
    public void run(){
        if(Joysticks[0].getRawButton(1)){
while(true){
    camera.writeResolution(AxisCamera.ResolutionT.k320x240);
        cc = new CriteriaCollection();
        cc.addCriteria(MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH, 30, 400, false);
        cc.addCriteria(MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT, 40, 400, false);
        try {
            ColorImage = camera.getImage();
            BinaryImage Threshold = ColorImage.thresholdRGB(0, 45, 25, 225, 0, 40);
            BinaryImage BigObjectsimage = Threshold.removeSmallObjects(false, 2);
        BinaryImage ConvexHull = BigObjectsimage.convexHull(false);
        BinaryImage ParticleFilter = ConvexHull.particleFilter(cc);
        
        } catch (AxisCameraException ex) {
        } catch (NIVisionException ex) {
        }
        
       
        
            
      try {
            ColorImage = camera.getImage();
            BinaryImage Threshold = ColorImage.thresholdRGB(0, 45, 25, 225, 0, 40);
            BinaryImage BigObjectsimage = Threshold.removeSmallObjects(false, 2);
        BinaryImage ConvexHull = BigObjectsimage.convexHull(false);
        BinaryImage ParticleFilter = ConvexHull.particleFilter(cc);

        
        ParticleAnalysisReport[] reports = ParticleFilter.getOrderedParticleAnalysisReports();  // get list of results
                for (int i = 0; i < reports.length; i++) {                                // print results
                    ParticleAnalysisReport r = reports[i];
            //target = 24X 36      
                    System.out.println("Particle: " + i + ":  Center of mass x: " + r.center_mass_x);
                   
                     SmartDashboard.putNumber("Center Mass X:", r.center_mass_x);
                 System.out.println("Number of Particles:" + ParticleFilter.getNumberParticles());
 
                }
                SmartDashboard.putNumber("Number of Particles: ", ParticleFilter.getNumberParticles());
                SmartDashboard.putNumber("Height:  ", ParticleFilter.getHeight());
                SmartDashboard.putNumber("Width:  ", ParticleFilter.getWidth());
               
                
                
        
        } catch (AxisCameraException ex) {
        } catch (NIVisionException ex) {
        }
        
            try{
             Thread.sleep (25);
            }
             catch(Exception e){
                
            }
    }}}
}
