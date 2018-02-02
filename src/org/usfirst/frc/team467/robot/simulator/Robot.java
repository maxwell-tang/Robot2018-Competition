/**
 * 
 */
package org.usfirst.frc.team467.robot.simulator;

import org.usfirst.frc.team467.robot.simulator.communications.RobotData;
import org.usfirst.frc.team467.robot.simulator.gui.MapController;

/**
 *
 */
public class Robot {
	
	public static final double WIDTH = 2;
	
	public static final double LENGTH = 3;
	
	Drive drive;
	
	MapController simulatorView;
	
	RobotData data;
	
	public void robotInit() {
		drive = DriveSimulator.getInstance();
		
		data = RobotData.getInstance();
		data.startServer();
	}
	
	public void setView(MapController simulatorView) {
		this.simulatorView = simulatorView;
	}
	
	int moveCount = 0;
	
	public void autonomousInit() {
		drive.zeroPosition();
		data.startPosition(20, 1.5);
		data.send();
		moveCount = 0;
	}
	
	public void autonomousPeriodic() {
		
		switch(moveCount) {
		
		case 0:
			if (drive.moveDistance(5, 5)) {
				moveCount++;
				drive.zeroPosition();				
			}
			break;
		
		case 1:
			if (drive.moveDistance(-0.785, 0.785)) {
				moveCount++;
				drive.zeroPosition();				
			}
			break;
		
		case 2:
			if (drive.moveDistance(5, 5)) {
				moveCount++;
				drive.zeroPosition();
			}
			break;

		case 3:
			if (drive.moveDistance(1.57, -1.57)) {
				moveCount++;
				drive.zeroPosition();
			}
			break;

		case 4:
			if (drive.moveDistance(5, 5)) {
				moveCount++;
				drive.zeroPosition();
			}
			break;

		default:
		}

	}
	
	public static void main(String[] args) {		
		Robot robot = new Robot();
		robot.robotInit();
		robot.autonomousInit();
		while(true) robot.autonomousPeriodic();
	}

}
