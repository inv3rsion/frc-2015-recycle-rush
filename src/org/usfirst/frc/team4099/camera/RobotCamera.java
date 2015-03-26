package org.usfirst.frc.team4099.camera;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.usfirst.frc.team4099.control.Gamepad;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.vision.AxisCamera;

public class RobotCamera {
	// yaw is left/right (x)
	// pitch is up/down (y)
	public static int YCENTER = 0;
	public static int WIDTH = 0;

	AxisCamera camera;
	private static final double INIT_YAW = 0.5;
	private static final double INIT_PITCH = 0.0;
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
		yawServo.set(INIT_YAW);
		pitchServo.set(INIT_PITCH);
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
		pitchServo.set(currentPitch);
		yawServo.set(currentYaw);
	}

	public void moveCamera(Gamepad control) {
		limitAngles();
		if (control.isDPadUpPressed()) {
			currentPitch -= MOVE_SPEED;
		}

		if (control.isDPadDownPressed()) {
			currentPitch += MOVE_SPEED;
		}

		if (control.isDPadRightPressed()) {
			currentYaw += MOVE_SPEED;
		}

		if (control.isDPadLeftPressed()) {
			currentYaw -= MOVE_SPEED;
		}

		limitAngles();
		updateCameraAngles();
	}

	public void takePhoto() {
		ColorImage img;
		try {
			img = camera.getImage();
			img.write("/home/admin/savedimgs/img-" + imgCount + "-" +System.currentTimeMillis() + ".jpg");
			imgCount++;
		} catch (NIVisionException e) {
			System.out.println("Could not take image.");
		}
	}

	public static float[] RGBtoCMYK(int r, int g, int b) {
		float[] cmyk = new float[4];
		if (r == 0 && g == 0 && b == 0) {
			cmyk[0] = 0;
			cmyk[1] = 0;
			cmyk[2] = 0;
			cmyk[3] = 1;
			return cmyk;
		}

		float compC = 1 - ((float) r / 255);
		float compM = 1 - ((float) g / 255);
		float compY = 1 - ((float) b / 255);
		float minCMY = Math.min(compC, Math.min(compM, compY));

		compC = (compC - minCMY) / (1 - minCMY);
		compM = (compM - minCMY) / (1 - minCMY);
		compY = (compY - minCMY) / (1 - minCMY);
		float compK = minCMY;
		cmyk[0] = compC;
		cmyk[1] = compM;
		cmyk[2] = compY;
		cmyk[3] = compK;

		return cmyk;
	}

	public Direction getDirection() {
		ColorImage image;

		try {
			image = camera.getImage();
			YCENTER = image.getHeight() / 2;
			WIDTH = image.getWidth();
			// img.write("/home/admin/savedimgs/img-" + imgCount + "-" +System.currentTimeMillis() + ".jpg");
			image.write("/home/admin/savedimgs/imgtemp.jpg");
		} catch (NIVisionException e) {
			System.out.println("Could not take image.");
		}
		
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("/home/admin/savedimgs/imgtemp.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// int[] LRYellows = new int[2];
		boolean yellow = false;
		int fromLeft = 0;
		int fromRight = 2447;
		while (!yellow && fromLeft < (WIDTH - 50) && fromRight > 50) {
			int countYellowsLeft = 0;
			int countYellowsRight = 0;
			for (int i = 0; i <= 50; i++) {
				int rgbFromLeft = img.getRGB(fromLeft + i, YCENTER);
				int rgbFromRight = img.getRGB(fromRight - i, YCENTER);

				// left yellows
				int rgb = rgbFromLeft;
				int r = (rgb >> 16) & 0xFF;
				int g = (rgb >> 8) & 0xFF;
				int b = (rgb) & 0xFF;
				float[] cmyk = RGBtoCMYK(r, g, b);
				if (cmyk[2] > cmyk[0] && cmyk[2] > cmyk[1] && cmyk[2] > cmyk[3])
					countYellowsLeft++;

				// right yellows
				rgb = rgbFromRight;
				r = (rgb >> 16) & 0xFF;
				g = (rgb >> 8) & 0xFF;
				b = (rgb) & 0xFF;
				cmyk = RGBtoCMYK(r, g, b);
				if (cmyk[2] > cmyk[0] && cmyk[2] > cmyk[1] && cmyk[2] > cmyk[3])
					countYellowsRight++;
			}

			if (countYellowsLeft >= 35 && countYellowsRight >= 35) {
				yellow = true;
				// break;
				return Direction.FORWARD;
			} else if (countYellowsLeft >= 35 && countYellowsRight < 35) {
				/*
				 * System.out.println(countYellowsLeft);
				 * System.out.println(countYellowsRight);
				 * System.out.println("Move left"); break;
				 */
				return Direction.LEFT;
			} else if (countYellowsLeft >= 35 && countYellowsRight < 35) {
				/*
				 * System.out.println(countYellowsLeft);
				 * System.out.println(countYellowsRight);
				 * System.out.println("Move right"); break;
				 */
				return Direction.RIGHT;
			}
			fromLeft += 10;
			fromRight -= 10;
			countYellowsLeft = 0;
			countYellowsRight = 0;
		}

		return Direction.NO_BOX;
	}
}
