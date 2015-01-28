package org.usfirst.frc.team4099.robot;

import org.usfirst.frc.team4099.camera.RobotCamera;
import org.usfirst.frc.team4099.control.Gamepad;
import org.usfirst.frc.team4099.robot.drive.Driver;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;


public class Robot extends SampleRobot {
	public static final String CAMERA_IP = "10.40.99.11";
	private RobotCamera camera = new RobotCamera(CAMERA_IP, YAW_SERVO, PITCH_SERVO);

	public static final int YAW_SERVO = 10;
	public static final int PITCH_SERVO = 11;

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
				camera.takePhoto();
			}
			// switch drive mode
			if (controller.isYButtonPressed()) {
				debug.println("Current Drive Mode: " + robotDrive.getCurrentDriveMode());
                robotDrive.toggleDriveMode();
			}

			// wait for motor update
            Timer.delay(0.005);
        }
    }
    
    public void test() {
    	debug.println("Entering testing mode...");
    }
}
