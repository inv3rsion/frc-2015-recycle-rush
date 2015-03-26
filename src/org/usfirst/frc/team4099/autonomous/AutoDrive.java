package org.usfirst.frc.team4099.autonomous;

import org.usfirst.frc.team4099.camera.*;
import org.usfirst.frc.team4099.robot.Elevator;
import org.usfirst.frc.team4099.robot.drive.SlideDrive;

import edu.wpi.first.wpilibj.Timer;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoDrive {
	private RobotCamera camera;
	private SlideDrive slideDrive;
	private Elevator elevator;
	private AutoMode mode;
	
	public Boolean movedToAutoZone = false;
	
	private boolean timing = false;
	
    public static final double REDUCTION_FACTOR = 1.5;
	
	private final double FORWARD_50_INCHES_TIME = 1.5;
	private final double PIVOT_90_DEGREES_TIME = 2.3;
	private final double SLIDE_50_INCHES_TIME = 4.0;
	
	public AutoDrive(RobotCamera camera, SlideDrive slideDrive) {
		this.camera = camera;
		this.slideDrive = slideDrive;
		
		// Checks for autonomous mode
		String modeString = SmartDashboard.getString("AutoMode");
		if(modeString.equalsIgnoreCase("move")) {
			this.mode = AutoMode.MOVE_TO_AUTO_ZONE;
		} else if(modeString.equalsIgnoreCase("stack")) {
			System.err.println("ye fewl what you mad be?");
			this.mode = AutoMode.PICK_AND_STACK_TOTES_AND_MOVE_TO_AUTO_ZONE;
		} else {
			System.err.println("Defaulting to stack bin and tote and move");
			this.mode = AutoMode.PICK_UP_TOTE_AND_MOVE_TO_AUTO_ZONE;
		}
	}
	
	public void move(double forwardDistance, double pivotDegrees, double slideDistance) {
		double[] powers = new double[3];
		
		//Set which direction to run the motors
		if(forwardDistance > 0) {
			powers[0] = 1;
		} else if(forwardDistance < 0){
			powers[0] = -1;
		}
		else powers[0] = 0;
		
		if(pivotDegrees > 0) {
			powers[1] = 1;
		} else if(pivotDegrees < 0){
			powers[1] = -1;
		}
		else powers[1] = 0;
		
		if(slideDistance > 0) {
			powers[2] = 1;
		} else if(slideDistance < 0){
			powers[2] = -1;
		}
		else powers[2] = 0;
		
		
		double[] times = new double[3];
		
		// Calculate times per 1 unit, multiply by actual distance
		times[0] = Math.abs(forwardDistance * FORWARD_50_INCHES_TIME / 50);
		times[1] = Math.abs(pivotDegrees * PIVOT_90_DEGREES_TIME / 90);
		times[2] = Math.abs(slideDistance * SLIDE_50_INCHES_TIME / 50);
		
		int[] order = new int[3];
		
		// Set the order for them to turn off
		if(times[0] >= times[1] && times[1] >= times[2]) {
			order[0] = 2;
			order[1] = 1;
			order[2] = 0;
		} else if (times[0] >= times[1] && times[0] >= times[2]) {
			order[0] = 1;
			order[1] = 2;
			order[2] = 0;
		} else if (times[1] >= times[0] && times[0] >= times[2]) {
			order[0] = 2;
			order[1] = 0;
			order[2] = 1;
		} else if (times[1] >= times[0] && times[1] >= times[2]) {
			order[0] = 0;
			order[1] = 2;
			order[2] = 1;
		} else if (times[2] >= times[0] && times[0] >= times[1]) {
			order[0] = 1;
			order[1] = 0;
			order[2] = 2;
		} else {
			order[0] = 0;
			order[1] = 1;
			order[2] = 2;
		}
		
		
		//Drive, wait, reset, repeat
		slideDrive.slideDrive(powers[0], powers[1], powers[2]);
		Timer.delay(times[order[0]]);
		powers[order[0]] = 0;
		slideDrive.slideDrive(powers[0], powers[1], powers[2]);
		Timer.delay(times[order[0]] - times[order[1]]);
		powers[order[1]] = 0;
		slideDrive.slideDrive(powers[0], powers[1], powers[2]);
		Timer.delay(times[order[0]] - times[order[1]] - times[order[2]]);
		slideDrive.slideDrive(0, 0, 0);
	}
	
	public void timingMoveToAutoZone() {
		// Into recycling bin
		move(20, 0, 0);
		// TODO: pick up bin
		elevator.setHeight(1);
		// Out of recycling bin
		move(-30, 0, 0);
		
		// Move over to tote
		move(0, 28, 0);
		move(17, 0, 0);
		//TODO: place recycling bin
		elevator.setHeight(.5);
		move(-17, 0, 0);
		elevator.setHeight(0);
		// Move into auto zone
		simpleMoveToAuto();
	}
	
	public void simpleMoveToAuto() {
		move(154, 0, 0);
	}
	
	public void turnAround() {
		move(0, 0, 90);
		move(-20, 0, 0);
		move(0, 0, 90);
	}
	
	public ArrayList<double[]> getFileOutput(Path path) {
		ArrayList<double[]> moves = new ArrayList<double[]>(10000);
		
		Charset charset = Charset.forName("US-ASCII");
		try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
		    String line = null;
		    while ((line = reader.readLine()) != null) {
		    	String[] outputStringArray = line.split(",");
		        double[] output = {
		        		Double.parseDouble(outputStringArray[0]),
		        		Double.parseDouble(outputStringArray[1]),
		        		Double.parseDouble(outputStringArray[2]),
		        		Double.parseDouble(outputStringArray[3])
		        };
		        moves.add(output);
		    }
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}
		return moves;
	}
	
	public void doFile(AutoMode mode) {
		// Figures out which file to use
		String pathToFile = "";
		switch(mode) {
		case MOVE_TO_AUTO_ZONE:
			pathToFile = "move.txt";
			break;
		case PICK_UP_TOTE_AND_MOVE_TO_AUTO_ZONE:
			pathToFile = "movePick.txt";
			break;
		default:
			pathToFile = "movePick.txt";
		}
		
		// Translates file to numbers
		ArrayList<double[]> moves = getFileOutput(Paths.get(pathToFile));
		
		// Runs numbers
		for(int i = 0; i < moves.size(); i++) {
			slideDrive.slideDrive(moves.get(i)[0] / REDUCTION_FACTOR, -moves.get(i)[2] / REDUCTION_FACTOR, moves.get(i)[1]);
			elevator.twoManOpHuman(moves.get(i)[3]);
			Timer.delay(0.005);
		}
		
	}
	
	public void autoDrive() {
		
		// Check for which auto mode it is
		// Then, if it's supposed to do timing moving, do that
		if(!movedToAutoZone) {
			switch(mode) {
			case MOVE_TO_AUTO_ZONE:
				if(timing)
					simpleMoveToAuto();
				else
					doFile(AutoMode.MOVE_TO_AUTO_ZONE);
				break;
			case PICK_UP_TOTE_AND_MOVE_TO_AUTO_ZONE:
				if(timing)
					timingMoveToAutoZone();
				else
					doFile(AutoMode.PICK_UP_TOTE_AND_MOVE_TO_AUTO_ZONE);
				break;
			default:
				if(timing)
					timingMoveToAutoZone();
				else
					doFile(AutoMode.PICK_UP_TOTE_AND_MOVE_TO_AUTO_ZONE);
			}
			movedToAutoZone = true;
		}
	}
}