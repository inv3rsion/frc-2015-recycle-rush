package org.usfirst.frc.team4099.camera;

import org.usfirst.frc.team4099.control.Gamepad;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.vision.AxisCamera;

public class RobotCamera {
	// yaw is left/right (x)
	// pitch is up/down (y)
	
	AxisCamera camera;
	private static final double INIT_YAW = 0.5;
	private static final double INIT_PITCH = 0.5;
	private static final double MOVE_SPEED = 1.0 / 180.0;
	private Servo yawServo;
	private Servo pitchServo;
	private double currentPitch;
	private double currentYaw;
	private int imgCount;

	public RobotCamera(String ip, int yawServo, int pitchServo) {
		this.camera = new AxisCamera(ip);
		this.yawServo = new Servo(yawServo);
		this.pitchServo = new Servo(pitchServo);
		cameraInit();
	}
	
	public void cameraInit() {
		// set the servos to the middle
		yawServo.set(INIT_PITCH);
		pitchServo.set(INIT_YAW);
	}
	
	public void limitAngles() {
		if (currentPitch > 1.0)
			currentPitch = 1.0;
		
		if (currentYaw > 1.0)
			currentYaw = 1.0;
		
		if (currentPitch < 0.0)
			currentPitch = 0.0;
		
		if (currentYaw < 0.0)
			currentYaw = 0.0;
	}
	
	public void updateCameraAngles() {
		pitchServo.set(currentYaw);
		yawServo.set(currentPitch);
	}
	
	public void moveCamera(Gamepad control) {
		limitAngles();
		if (control.isDPadUpPressed()) {
			if (currentPitch < 1.0)
				currentPitch += MOVE_SPEED;
		}

		if (control.isDPadDownPressed()) {
			if (currentPitch > 0.0)
				currentPitch -= MOVE_SPEED;
		}
		
		if (control.isDPadRightPressed()) {
			if (currentYaw > 1.0)
				currentYaw += MOVE_SPEED;
		}
		
		if (control.isDPadLeftPressed()) {
			if (currentYaw < 0.0)
				currentYaw -= MOVE_SPEED;
		}

		limitAngles();
		updateCameraAngles();
	}

	public void takePhoto() {
		ColorImage img;
		try {
			img = camera.getImage();
			img.write("/home/admin/savedimgs/img-" + imgCount + "-" + System.currentTimeMillis() + ".jpg");
			imgCount++;
		} catch (NIVisionException e) {
			System.out.println("Could not take image.");
		}
	}
}
