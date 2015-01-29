package org.usfirst.frc.team4099.robot.drive;

import org.usfirst.frc.team4099.control.Gamepad;
import org.usfirst.frc.team4099.robot.drive.SlideDriver;

import edu.wpi.first.wpilibj.RobotDrive;

public class Driver {
	private DriveMode currentMode = DriveMode.SLIDE;
    private RobotDrive arcadeDrive;
    private SlideDriver slideDrive;

    public static final double REDUCTION_FACTOR = 2.0;
	public static final int FRONT_LEFT_MOTOR = 0;
	public static final int REAR_LEFT_MOTOR = 1;
	public static final int FRONT_RIGHT_MOTOR = 6;
	public static final int REAR_RIGHT_MOTOR = 7;
    public static final int FRONT_SLIDE_MOTOR = 2;
    public static final int REAR_SLIDE_MOTOR = 3;
	
	public Driver() {
        arcadeDrive = new RobotDrive(FRONT_LEFT_MOTOR, REAR_LEFT_MOTOR, FRONT_RIGHT_MOTOR, REAR_RIGHT_MOTOR);
<<<<<<< HEAD
        slideDrive = new SlideDriver(arcadeDrive, SLIDE_MOTOR);
=======
        slideDrive = new SlideDrive(arcadeDrive, FRONT_SLIDE_MOTOR, REAR_SLIDE_MOTOR);
>>>>>>> FETCH_HEAD
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

    public void enterAutonomousMode() {
        //TODO: Maybe implement something here?
    }

    public void enterTeleoperatedMode() {
        this.arcadeDrive.setSafetyEnabled(true);
    }
	
	public void drive(Gamepad controller) {
		switch (currentMode) {
		case ARCADE:
			arcadeDrive.arcadeDrive(controller.getLeftVerticalAxis() / REDUCTION_FACTOR,
                                    controller.getLeftHorizontalAxis() / REDUCTION_FACTOR);
			break;

		case SLIDE:
            // drive instructions
            // use left joystick to move forward and backward, left right to strafe
            // use left trigger button to turn left
            // use right trigger button to turn right
            /*if (!(controller.isLeftTriggerPressed() && controller.isRightTriggerPressed())) {  // make sure that both turns are not pressed
                if (controller.isLeftTriggerPressed())
                    slideDrive.slideDrive(controller.getLeftVerticalAxis() / REDUCTION_FACTOR,
                                          controller.getLeftTrigger() / REDUCTION_FACTOR,
                                          controller.getLeftHorizontalAxis() / REDUCTION_FACTOR);                    

                else if (controller.isRightTriggerPressed())
                    // note that the negative may be placed in the wrong if statement, test to see
                    slideDrive.slideDrive(controller.getLeftVerticalAxis() / REDUCTION_FACTOR,
                                          controller.getRightTrigger() / REDUCTION_FACTOR,
                                          controller.getLeftHorizontalAxis() / REDUCTION_FACTOR);

            } else {
                    slideDrive.slideDrive(controller.getLeftVerticalAxis() / REDUCTION_FACTOR,
                                          0.0,
                                          controller.getLeftHorizontalAxis() / REDUCTION_FACTOR);
            }*/
            slideDrive.slideDrive(controller.getLeftVerticalAxis() / REDUCTION_FACTOR,
                    controller.getLeftHorizontalAxis() / REDUCTION_FACTOR,
                    controller.getRightHorizontalAxis() / REDUCTION_FACTOR);
			break;
		}
	}
}
