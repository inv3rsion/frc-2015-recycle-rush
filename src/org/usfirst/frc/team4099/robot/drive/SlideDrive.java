package org.usfirst.frc.team4099.robot.drive;

//import org.usfirst.frc.team4099.robot.Robot;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.RobotDrive;

public class SlideDrive {
	
	// private double currentSpeed;
	
	private SpeedController sliderTalonFront;
	private SpeedController sliderTalonRear;
	private RobotDrive arcadeDrive;

	public SlideDrive(RobotDrive arcadeDrive, int sliderFront, int sliderRear) {
		this.arcadeDrive = arcadeDrive;
		this.sliderTalonFront = new Talon(sliderFront);
		this.sliderTalonRear = new Talon(sliderRear);
	}
	
	public RobotDrive getArcadeDrive() {
		return this.arcadeDrive;
	}

	private void slide(double speed) {
		sliderTalonFront.set(speed);
		sliderTalonRear.set(speed);
	}

	public void slideDrive(double forward, double turn, double strafe) {
		strafe = -strafe;
		if (Math.abs(forward) < 0.4) {
			forward = 0;
		}
		if (Math.abs(turn) < 0.5) {
			turn = 0;
		}

		arcadeDrive.arcadeDrive(forward, turn);
		//Robot.debug.println("" + strafe);
		
		if (Math.abs(strafe) > 0.1) {
			/*if (Math.abs(strafe - currentSpeed) > 0.3) {
				if (strafe < 0) {
					this.slide(currentSpeed + 0.1);
					currentSpeed += 0.1;
				} else {
					this.slide(currentSpeed - 0.1);
					currentSpeed -= 0.1;
				}
			} else {
				this.slide(strafe);
			}*/
			this.slide(strafe);
		} else {
			this.slide(0);
		}
	}
}
