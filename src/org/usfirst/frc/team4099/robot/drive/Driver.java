package org.usfirst.frc.team4099.robot.drive;

import org.usfirst.frc.team4099.autonomous.AutoDrive;
import org.usfirst.frc.team4099.camera.RobotCamera;
import org.usfirst.frc.team4099.control.FlightStick;
import org.usfirst.frc.team4099.control.Gamepad;

import edu.wpi.first.wpilibj.RobotDrive;


public class Driver {

    protected DriveMode currentMode = DriveMode.SLIDE;
    protected RobotDrive arcadeDrive;
    protected SlideDrive slideDrive;
    protected AutoDrive autoDrive;
    
    public static final double REDUCTION_FACTOR = 1.5;
	public static final int FRONT_LEFT_MOTOR = 0;
	public static final int REAR_LEFT_MOTOR = 1;
	public static final int FRONT_RIGHT_MOTOR = 2;
	public static final int REAR_RIGHT_MOTOR = 3;
    public static final int FRONT_SLIDE_MOTOR = 4;
    public static final int REAR_SLIDE_MOTOR = 5;
	
    
	public Driver(RobotCamera cam) {
        arcadeDrive = new RobotDrive(FRONT_LEFT_MOTOR, REAR_LEFT_MOTOR, FRONT_RIGHT_MOTOR, REAR_RIGHT_MOTOR);
        slideDrive = new SlideDrive(arcadeDrive, FRONT_SLIDE_MOTOR, REAR_SLIDE_MOTOR);
        autoDrive = new AutoDrive(cam, slideDrive);
        arcadeDrive.setExpiration(0.1);
	}
	
	public void setDriveMode(DriveMode type) {
		currentMode = type;
	}
	
	public DriveMode getCurrentDriveMode() {
		return currentMode;
	}
	
	public void toggleDriveMode() {
		switch (currentMode) {
		case ARCADE:
			setDriveMode(DriveMode.SLIDE);
			break;
			
		case SLIDE:
			setDriveMode(DriveMode.ARCADE);
			break;
		}
	}

    public void enterTeleoperatedMode() {
    	this.autoDrive = null;
        this.arcadeDrive.setSafetyEnabled(true);
    }
    
    public void enterAutonomousMode() {
    	this.arcadeDrive.setSafetyEnabled(true);
    }
    
    public void autoDrive() {
    	this.autoDrive.autoDrive();
    }
	
    // Using a controller
	public void drive(Gamepad controller, FlightStick flight) {
		switch (currentMode) {
		case ARCADE:
			if (controller.getLeftVerticalAxis()>0 || controller.getLeftHorizontalAxis() > 0) {
				// Uses controller to move robot
				arcadeDrive.arcadeDrive(controller.getLeftVerticalAxis() / REDUCTION_FACTOR,
                        -controller.getLeftHorizontalAxis() / REDUCTION_FACTOR);
			} else {
				// Uses Flight Stick to move robot
				arcadeDrive.arcadeDrive(flight.getVerticalAxis() / REDUCTION_FACTOR,
						-flight.getTwist() / REDUCTION_FACTOR);
			}
			
			break;

		case SLIDE:
            System.out.println(controller.getRightHorizontalAxis());
//			slideDrive.slideDrive(controller.getLeftVerticalAxis() / REDUCTION_FACTOR, -controller.getLeftHorizontalAxis() / REDUCTION_FACTOR, controller.getRightHorizontalAxis());
			
            if (controller.getLeftVerticalAxis() != 0 || controller.getRightHorizontalAxis() != 0 || controller.getLeftHorizontalAxis() != 0) {
            	// Uses controller to move robot
                slideDrive.slideDrive(controller.getLeftVerticalAxis() / REDUCTION_FACTOR, -controller.getRightHorizontalAxis() / REDUCTION_FACTOR, controller.getLeftHorizontalAxis());
            } else {
            	// Uses Flight Stick to move robot
            	slideDrive.slideDrive(flight.getVerticalAxis() / REDUCTION_FACTOR, -flight.getTwist() / REDUCTION_FACTOR, flight.getHorizontalAxis());
            }
            /*if (controller.isDPadLeftPressedStrict()) {
            	slideDrive.slideDrive(controller.getLeftVerticalAxis() / REDUCTION_FACTOR, -0.75, controller.getLeftHorizontalAxis());
            } else if (controller.isDPadRightPressedStrict()) {
            	slideDrive.slideDrive(controller.getLeftVerticalAxis() / REDUCTION_FACTOR, 0.75, controller.getLeftHorizontalAxis());
            } else {
            	slideDrive.slideDrive(controller.getLeftVerticalAxis() / REDUCTION_FACTOR, 0.0, controller.getLeftHorizontalAxis());            	
            }*/
            break;
		}
	}
	
	// Using numbers
	public void drive(double forward, double sideways, double pivot) {
		switch (currentMode) {
		case ARCADE:
				arcadeDrive.arcadeDrive(forward / REDUCTION_FACTOR,
                        -sideways / REDUCTION_FACTOR);
			break;
		case SLIDE:
            slideDrive.slideDrive(forward / REDUCTION_FACTOR, -pivot / REDUCTION_FACTOR, sideways);
            break;
		}
	}
}
