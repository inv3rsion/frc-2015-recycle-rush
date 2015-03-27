package org.usfirst.frc.team4099.robot;

import org.usfirst.frc.team4099.control.FlightStick;
import org.usfirst.frc.team4099.control.Gamepad;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Elevator {
	public static final int LEFT_ELEVATOR = 6;
	public static final int RIGHT_ELEVATOR = 7;
	
	//private double kLift_P = 0.1;
	//private double kLift_I = 0.01;
	//private double kLift_D = 0.001;
	
	//private boolean usingPID = false;
	
	private Talon leftTalon;
	private Talon rightTalon;
	//private Encoder encoder;
	
	//private PIDController leftLiftPID;
	//private PIDController rightLiftPID;
	
	public static final double ELEVATOR_MAX = 80.0;
	public static final double ELEVATOR_MIN = 0.0;
	
	//public static final int ENCODER_PIN_1 = 4;
	//public static final int ENCODER_PIN_2 = 5;
	
	public static final double REDUCTION_FACTOR = -1.0;
	
	//private double currentHeight = 0;
	//private double DISTANCE_PER_PULSE = 0.01;
	
	public Elevator() {
		leftTalon = new Talon(LEFT_ELEVATOR);
		rightTalon = new Talon(RIGHT_ELEVATOR);
		
		/*encoder = new Encoder(ENCODER_PIN_1, ENCODER_PIN_2);
		encoder.setPIDSourceParameter(PIDSource.PIDSourceParameter.kDistance);
		encoder.setDistancePerPulse(DISTANCE_PER_PULSE);
		
		leftLiftPID = new PIDController(kLift_P, kLift_I, kLift_D, encoder, leftTalon);
		rightLiftPID = new PIDController(kLift_P, kLift_I, kLift_D, encoder, rightTalon);
		//leftLiftPID.enable();
		//rightLiftPID.enable();
		encoder.reset();*/
	}
	
	
	public void twoManOpHuman(FlightStick flight) {
		//leftTalon.set(flight.getSlider() / REDUCTION_FACTOR);
		//rightTalon.set(flight.getSlider() / REDUCTION_FACTOR);
		leftTalon.set(flight.getVerticalAxis() / REDUCTION_FACTOR);
		rightTalon.set(flight.getVerticalAxis() / REDUCTION_FACTOR);
	}
	
	public void twoManOpHuman(double move) {
		leftTalon.set(move / REDUCTION_FACTOR);
		rightTalon.set(move / REDUCTION_FACTOR);		
	}
	
	/*
	public void singleManOpPID(Gamepad control) {
		if (control.isDPadUpPressed()) {
			currentHeight += 0.25;
		} else if (control.isDPadDownPressed()) {
			currentHeight -= 0.25;
		}
		
		if (currentHeight > ELEVATOR_MAX) {
			currentHeight = ELEVATOR_MAX;
		}
		
		if (currentHeight < ELEVATOR_MIN) {
			currentHeight = ELEVATOR_MIN;
		}
		
		SmartDashboard.putString("encoder", "" + encoder.getDistance());
		SmartDashboard.putString("height", "" + currentHeight);
		setHeight(currentHeight);
	}*/
	/*
	public void twoManOpPID(FlightStick flight) {
		currentHeight = flight.getSlider() * ELEVATOR_MAX;
		setHeight(currentHeight);
	}*/
	
	//unused
	/*
	public void move(Gamepad control) {
		if (control.isDPadUpPressed()) {
			currentHeight += 0.01;
		} else if (control.isDPadDownPressed()) {
			currentHeight -= 0.01;
		}
		
		if (currentHeight > ELEVATOR_MAX) {
			currentHeight = ELEVATOR_MAX;
		}
		
		if (currentHeight < ELEVATOR_MIN) {
			currentHeight = ELEVATOR_MIN;
		}
		
		// probably useless
		/*if (control.isYButtonPressed()) {
			leftTalon.set(-0.6);
			rightTalon.set(-0.6);
		} else {
			leftLiftPID.enable();
			rightLiftPID.enable();
			leftTalon.set(0);
			rightTalon.set(0);
		}*/
		
		// might be used at competition
		/*
		leftTalon.set(Math.abs(control.getRightVerticalAxis()) / -1.5);
		rightTalon.set(Math.abs(control.getRightVerticalAxis()) / -1.5);
		*/
		
		// hopefully we can get this working
	//	setHeight(currentHeight);
	//}
/*
	public void setHeight(double height) {
		leftLiftPID.setSetpoint(height);
		rightLiftPID.setSetpoint(height);
	}
	*/
}