package org.usfirst.frc.team4099.robot;

import org.usfirst.frc.team4099.camera.RobotCamera;
import org.usfirst.frc.team4099.control.FlightStick;
import org.usfirst.frc.team4099.control.Gamepad;
import org.usfirst.frc.team4099.robot.drive.Driver;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Robot extends SampleRobot {
	public static final String CAMERA_IP = "10.40.99.11";
	public static final int YAW_SERVO = 8;
	public static final int PITCH_SERVO = 9;
	private RobotCamera camera = new RobotCamera(CAMERA_IP, YAW_SERVO, PITCH_SERVO);
	private LimitSwitches limitswitches = new LimitSwitches();
	private Driver robotDrive;
	private Gamepad controller = new Gamepad(0);

	public String recordPath = "";
	public ArrayList<double[]> moves = new ArrayList<double[]>(10000);

	private FlightStick flight = new FlightStick(1);

	private Elevator elevator = new Elevator();
	// private Reel reel = new Reel();

	public static final String DEBUG_FILE = "/tmp/debug.txt";
	public static Debug debug = new Debug(DEBUG_FILE);

	public Robot() {
		// Add stuff to SmartDashboard
		SendableChooser sendableChooser = new SendableChooser();
		sendableChooser.addDefault("Tote and Bin", "moveBins");
		sendableChooser.addObject("Move w/o Picking", "move");
		sendableChooser.addObject("DO NOT USE", "stack");
		SmartDashboard.putData("Auto Mode", sendableChooser);
		SmartDashboard.putBoolean("isUsingPID?", false);
		limitswitches.addToSmartDashboard();
		// Create the driver object
		robotDrive = new Driver(camera);
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
			robotDrive.autoDrive();
		}
	}

	public void operatorControl() {
		debug.println("Entering teleoperated mode...");
		robotDrive.enterTeleoperatedMode();
		while (isOperatorControl() && isEnabled()) {

			// Start recording moves
			if (controller.isAButtonPressed() && controller.isBButtonPressed()
					&& !recordPath.equals("")) {
				System.out.println("Recording moves... be quick!");
				System.out.println("Remember to use the controller and singleManOpHuman mode.");
				recordPath = SmartDashboard.getString("PathToRecord");
			}
			// Actually record moves if there's a place to put them
			if (!recordPath.equals("")) {
				// Gets inputs, only uses controller movement for now (may
				// change if necessary)
				double[] inputs = { controller.getLeftVerticalAxis(),
						controller.getLeftHorizontalAxis(),
						controller.getRightHorizontalAxis(), flight.getSlider() };
				moves.add(inputs);
			}
			if (controller.isXButtonPressed() && controller.isYButtonPressed()) {
				// TODO: write to file
				try {
					PrintWriter printWriter = new PrintWriter(recordPath,
							"UTF-8");
					for (int i = 0; i < moves.size(); i++) {
						printWriter.println(Double.toString(moves.get(i)[0])
								+ "," + Double.toString(moves.get(i)[1]) + ","
								+ Double.toString(moves.get(i)[2]) + ","
								+ Double.toString(moves.get(i)[3]));
					}
					printWriter.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				// Resets recordPath so that other autoModes can be recorded,
				// stops recording
				recordPath = "";
			}

			robotDrive.drive(controller, flight);

			// move the reel in wheels
			// reel.move(controller, flight);

			if (!SmartDashboard.getBoolean("isUsingPID?"))
				elevator.twoManOpHuman(flight);
			else
				// pid move mode (pick 1)
				elevator.singleManOpPID(controller);
			// elevator.twoManOpPID(flight);

			// elevator.move(controller);
			// moving camera
			// camera.moveCamera(controller);

			// wait for motor update
			Timer.delay(0.005);
		}
	}

	public void test() {
		debug.println("Entering testing mode...");
	}
}
