package edu.mdci;
/*
 * @author 
*  ______       This
 * d[-__-]b      Guy
 * --|..|--"'    Is
 * ^ |__|        Busy
 *   L L         Existing
 *
 * MainRobot.java Collection of tools for robot operation. Central Logic for
 * robot control All in one file
 */
import edu.wpi.first.wpilibj.Accelerometer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.camera.AxisCamera;

public final class MainRobot {

    /*
     * Constants during robot operation
     * start port of 0 prevents initialization and use of that hardware type
     * 
     */
    private final EntryPoint entryPoint;
    private static boolean DEBUG = false;
    //
    private static final int FRONT_RIGHT_WHEEL = 1, FRONT_LEFT_WHEEL = 2,
            REAR_RIGHT_WHEEL = 3, REAR_LEFT_WHEEL = 4;
    //
    private static final double[] transmission = {0.45, 0.60, 0.90};
    //
    private static final int MAXIMUM_JOYSTICK_AXISES = 5;
    private static final int ENCODER_START_PORT = 1;
    //private static final int DRIVE_MOTOR_CONTROLLERS_START_PORT = 1;
    private static final int JOYSTICK_START_PORT = 1;
    private static final int TALON_START_PORT = 1;
    private static final int ACCELEROMETER_PORT = 5;
    //private static final int SOLENOID_START_PORT = 0;
    private static final int SERVO_START_PORT = 7;
    private static final int GYRO_PORT = 1;
    //private static final int COMPRESSOR_RELAY_PORT = 0;
    //private static final int COMPRESSOR_PRESSURE_SWITCH_PORT = 0;
    private static final int SONAR_START_PORT = 0;
    //private static final int DRIVE_MOTOR_CONTROLLERS = 0; // 0 for Talons, default Jaguar
    //private static final int FIRING_MOTOR_CONTROLLERS = 0; //same as above ^^
    public static final String CAMERA_IP = "192.168.0.90";
    private static final int FIRING_MOTOR_CONTROLLERS_START_PORT = 5;
    //
    private static final double JOYSTICK_DEAD_BAND = 0.03;
    //
    //private Jaguar[] driveJaguars = new Jaguar[4];
    //private Jaguar[] firingJaguars = new Jaguar[2];
    private Joystick[] joysticks = new Joystick[1];
    //private Solenoid[] solenoids = new Solenoid[2];
    private Servo[] servos = new Servo[2];
    private Talon[] talons = new Talon[5];
    //private Talon[] firingTalons = new Talon[2];
    private Ultrasonic[] sonars = new Ultrasonic[3];
    private Encoder[] encoders = new Encoder[3];
    private Accelerometer adxl345;
    private Gyro gyro;
    //private Compressor compressor;
    private AxisCamera camera;
    //
    private final Object tankDriveThreadLock = new Object();
    private final Object servoTestThreadLock = new Object();
    private final Object cartDriveThreadLock = new Object();
    private final Object pneumaticsThreadLock = new Object();
    private final Object intelligenceThreadLock = new Object();
    private final Object encoderThreadLock = new Object();
    private final Object firingTestThreadLock = new Object();

    /* boolean threadHouseKeeping(long ThreadSleepTimeMiliseconds);
     * this thread sleeps for the time specified and takes care of
     * thread tasks which include sleeping. returns the status of
     * running in EntryPoint, which is the flag for running the thread
     * if it returns true then the thread may continue execution otherwise
     * it shoudl terminate
     */
    /**
     * Tank Drive pushes Left joystick input to the left motor controllers and
     * right joystick values to right motor controllers
     *
     * @return New Thread Object that takes care of tank drive
     */
    public Thread newTankDriveThread() {

        /*
         * We create a new Thread from an annonymous Runnable class and return it
         */

        return new Thread(new Runnable() {
            /*
             * storage for retrieved values
             */
            double[] axis = new double[MAXIMUM_JOYSTICK_AXISES + 1];

            /*
             * the run() method is called when a thread begins execution
             */
            public void run() {

                /*
                 * Synchronization locks onto an object to esure that the
                 * current thread is the only one using it, we lock onto
                 * any object here to ensure that only one drive thread
                 * is executed at a time (in case someone creates another one)
                 */
                synchronized (tankDriveThreadLock) {
                    int loopCount =0 ; 
                    while (true) {

                        loopCount = loopCount+1 ;
                        
                        //get all aixs values and assighn them to their respective axis slot
                        for (int i = 1; i < axis.length; i++) {
                            axis[i] = joysticks[0].getRawAxis(i);
                        }

                        /*
                         * If we have one joystick i.e. the game pad, then we use a certain set of axises
                         */

                        //check for deadband
                        for (int i = 0; i < axis.length; i++) {
                            if (Math.abs(axis[i]) < JOYSTICK_DEAD_BAND) {
                                axis[i] = 0;
                            }
                        }

                        int speed = 0;
                        
                        if(joysticks[0].getRawButton(3) && loopCount < 50){
                            if( speed < transmission.length) {
                                speed++;
                            }
                            loopCount = 0;
                        }
                        
                        if(joysticks[0].getRawButton(4) && loopCount < 50){
                            if( speed > 0 ){
                                speed--;
                                
                            }
                            loopCount = 0;
                        }
                        
                        /*
                         * Now we assign values to the controller type we are using
                         * right now we are inverting the left side
                         */
                                talons[FRONT_LEFT_WHEEL - 1].set(-1 * axis[2] * transmission[speed]);
                                talons[FRONT_RIGHT_WHEEL - 1].set(-1 * axis[2] * transmission[speed]);
                                talons[REAR_RIGHT_WHEEL - 1].set(axis[5] * transmission[speed]);
                                talons[REAR_LEFT_WHEEL - 1].set(axis[5]* transmission[speed]);

                                if (DEBUG) {
                                    for (int i = 0; i < talons.length; i++) {
                                        System.out.print("Talon " + i + ": " + talons[i].get() + "\t");
                                    }
                                    System.out.println();
                                }

                        /*
                         * print out our values
                         */
                        if (DEBUG) {
                            System.out.println("Axis 1: " + axis[1] + "\tAxis 2: " + axis[2] + "\tAxis 3: " + axis[3]
                                    + "\tAxis 4: " + axis[4] + "\tAxis 5: " + axis[5]);
                        }

                        /*
                         * sleep for 20 miliseconds and if false is returned we stop execution
                         */
                        if (!threadHouseKeeping(20)) {
                            return;
                        }
                    }
                }
            }
        });
    }

    public Thread newEncoderOutputThread() {
        return new Thread(new Runnable() {
            public void run() {
                synchronized (encoderThreadLock) {
                    for(int i = 0; i < encoders.length; i++){
                        encoders[i].start();
                    }
                    
                    while (true) {
                        for (int i = 0; i < encoders.length; i++) {
                            System.out.print("Encoder " + i + ": " + encoders[i].getRate() + "\t");
                        }
                        System.out.println();

                        if (!threadHouseKeeping(20)) {
                            return;
                        }
                    }
                }
            }
        });
    }


    public Thread newServoTestThread() {
        return new Thread(new Runnable() {
            public void run() {
                synchronized (servoTestThreadLock) {
                    while (true) {
                        servos[0].setAngle(gyro.getAngle());
                        servos[1].set(joysticks[0].getRawAxis(3));

                        if (DEBUG) {
                            System.out.println("Gyro Angle: " + gyro.getAngle());
                        }

                        if (!threadHouseKeeping(20)) {
                            return;
                        }
                    }
                }
            }
        });
    }

    public Thread newCartDriveThread() {
        return new Thread(new Runnable() {
            RobotDrive drive;

            public void run() {
                synchronized (cartDriveThreadLock) {
                    try {
                        drive = new RobotDrive(1, 2, 3, 4);
                    } catch (Exception e) {
                        System.err.println("Error initiating RobotDrive, jags or talons already created");
                        return;
                    }

                    while (true) {
                        drive.mecanumDrive_Cartesian(joysticks[0].getX(), joysticks[0].getY(), joysticks[0].getRawAxis(3), gyro.getAngle());
                        System.out.println("1: " + joysticks[0].getRawAxis(1) + "\t2: " + joysticks[0].getRawAxis(2) + "\tGyro: " + gyro.getAngle());

                        if (!threadHouseKeeping(20)) {
                            return;
                        }
                    }
                }
            }
        });
    }



    /**
     * MainRobot() Constructor takes care of initializing all hardware
     * components
     *
     * @param ep Reference to the entry point of the application
     */
    public MainRobot(EntryPoint ep) {
        entryPoint = ep;
        try {

                    for (int c = 0; c < talons.length; c++) {
                        talons[c] = new Talon(TALON_START_PORT + c);
                    }


            if (JOYSTICK_START_PORT != 0) {
                for (int c = 0; c < joysticks.length; c++) {
                    joysticks[c] = new Joystick(JOYSTICK_START_PORT + c);
                }
            }

            if (SERVO_START_PORT != 0) {
                for (int c = 0; c < servos.length; c++) {
                    servos[c] = new Servo(SERVO_START_PORT + c);
                }
            }

            if (SONAR_START_PORT != 0) {
                for (int c = 0; c < sonars.length; c++) {
                    //sonars[c] = new Ultrasonic(SONAR_START_PORT + c);
                }
            }

            if (ENCODER_START_PORT != 0) {
                for (int c = 0, d = 0; c < encoders.length; c++, d++) {
                    encoders[c] = new Encoder(ENCODER_START_PORT + d, ENCODER_START_PORT + ++d);
                }
            }

            //for (int c = 0; c < solenoids.length; c++) {
            //    solenoids[c] = new Solenoid(SOLENOID_START_PORT + c);
            //}

            if (ACCELEROMETER_PORT != 0) {
                adxl345 = new Accelerometer(ACCELEROMETER_PORT);
            }

            if (GYRO_PORT != 0) {
                gyro = new Gyro(GYRO_PORT);
            }

            //camera = AxisCamera.getInstance(CAMERA_IP);



        } catch (Exception e) {
            System.err.println("Error in initializing objects!!!!");
            e.printStackTrace();
        }
    }

    /**
     * threadHouseKeeping(long sleepMili) Sleeps current thread for time
     * specified and returns status of application
     *
     * @param sleepMili Amount of time to sleep in miliseconds
     * @return running status of application, threads should cease execution if
     * false is returned
     */
    private boolean threadHouseKeeping(long sleepMili) {
        try {
            Thread.sleep(sleepMili);
        } catch (InterruptedException e) {
        }
        return entryPoint.running();
    }
}
