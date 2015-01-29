package org.usfirst.frc.team4099.robot.drive;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.RobotDrive;

class SlideDrive {
    private SpeedController sliderTalonFront;
    private SpeedController sliderTalonRear;
    private RobotDrive arcadeDrive;

    public SlideDrive(RobotDrive arcadeDrive, int sliderFront, int sliderRear) {
        this.arcadeDrive = arcadeDrive;
        this.sliderTalonFront = new Talon(sliderFront);
        this.sliderTalonRear = new Talon(sliderRear);
    }

    private void slide(double speed) {
        sliderTalonFront.set(speed);
        sliderTalonRear.set(speed);
    }

    public void slideDrive(double forward, double turn, double strafe) {
        arcadeDrive.arcadeDrive(forward, turn);
        this.slide(strafe);
    }
}
