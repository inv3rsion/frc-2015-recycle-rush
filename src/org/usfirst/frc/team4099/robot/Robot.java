package org.usfirst.frc.team4099.robot;

import org.usfirst.frc.team4099.camera.RobotCamera;
import org.usfirst.frc.team4099.control.Gamepad;
import org.usfirst.frc.team4099.robot.drive.Driver;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;


public class Robot extends SampleRobot {
	public static final String CAMERA_IP = "10.40.99.11";
	public static final int YAW_SERVO = 8;
	public static final int PITCH_SERVO = 9;
	private RobotCamera camera = new RobotCamera(CAMERA_IP, YAW_SERVO, PITCH_SERVO);

	private Driver robotDrive;
    private Gamepad controller = new Gamepad(0);

    public static final String DEBUG_FILE = "/tmp/debug.txt";
    public static Debug debug = new Debug(DEBUG_FILE);
    
    public Robot() {
    	robotDrive = new Driver();
    }
    
    public void robotinit() {
    	debug.println("Robot initialized...");
    }
    
    public void disabled() {
    	debug.println("Robot disabled...");
    }

    public void autonomous() {
    	debug.println("Entering autonomous mode...");
        robotDrive.enterAutonomousMode();
    	while (isAutonomous() && isEnabled()) {
    		String dir = "";
    		while (true)
    		{
    			dir = camera.getDirection();
    			if (dir.equals("LEFT")) {
    				//move left
    			} else if (dir.equals("RIGHT")) {
    				//move right
    			} else if (dir.equals("NO YELLOW BOX")) {
    				//move away, there is no yellow box in view
    				break;
    			} else if (dir.equals("FORWARD")) {
    				//move forward to pick up box
    				break;
    			}
    		}
    	}
    }
    
    public void operatorControl() {
        robotDrive.enterTeleoperatedMode();
		debug.println("Entering teleoperated mode...");

		while (isOperatorControl() && isEnabled()) {
			// *******************
			// ** DRIVING ROBOT **
			// *******************
			robotDrive.drive(controller);

			// moving camera
			camera.moveCamera(controller);

			// *********************
			// ** BUTTON COMMANDS **
			// *********************

			// take photos
			if (controller.isAButtonPressed()) {
                Timer.delay(1.0);
				camera.takePhoto();
			}
			// switch drive mode
			if (controller.isYButtonPressed()) {
                Timer.delay(1.0);
                robotDrive.toggleDriveMode();
				debug.println("Current Drive Mode: " + robotDrive.getCurrentDriveMode());
			}

			// wait for motor update
            Timer.delay(0.005);
        }
    }
    
    public void test() {
    	debug.println("Entering testing mode...");
    }
}
