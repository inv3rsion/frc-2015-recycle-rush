package org.usfirst.frc.team4099.robot.drive;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.RobotDrive;

<<<<<<< HEAD:src/org/usfirst/frc/team4099/robot/drive/SlideDriver.java
public class SlideDriver {
    private SpeedController sliderTalon;
    private RobotDrive arcadeDrive;

    public SlideDriver(RobotDrive arcadeDrive, int slider) {
=======
public class SlideDrive {
    private SpeedController sliderTalonFront;
    private SpeedController sliderTalonRear;
    private RobotDrive arcadeDrive;

    public SlideDrive(RobotDrive arcadeDrive, int sliderFront, int sliderRear) {
>>>>>>> FETCH_HEAD:src/org/usfirst/frc/team4099/robot/drive/SlideDrive.java
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
