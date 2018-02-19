package org.usfirst.frc.team467.robot;

import org.apache.log4j.Logger;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;

public class Grabber {

	public enum GrabberState {
		GRAB,
		NEUTRAL,
		RELEASE
	}

	public static final int GRAB_TIME_MS = 1000;
	public static final int RELEASE_TIME_MS = 1000;

	private int grabCount = 0;
	private int releaseCount = 0;
	private int count = 0;

	private GrabberState state = GrabberState.NEUTRAL;

	private static final Logger LOGGER = Logger.getLogger(Grabber.class);

	private static Grabber instance;
	private SpeedController left;
	private SpeedController right;
	private boolean hadCube = false;
	private boolean hasCube = false;
	OpticalSensor os;

	private Grabber() {
		if (RobotMap.HAS_GRABBER) {
			left = new Spark(RobotMap.GRABBER_L_CHANNEL);
			right = new Spark(RobotMap.GRABBER_R_CHANNEL);
			os = OpticalSensor.getInstance();
		} else {
			left = new NullSpeedController();
			right = new NullSpeedController();
			os = OpticalSensor.getInstance();
		}

		grabCount = GRAB_TIME_MS/20;
		releaseCount = RELEASE_TIME_MS/20;
	}

	public static Grabber getInstance() {
		if (instance == null) {
			instance = new Grabber();
		}

		return instance;
	}

	public void periodic() {
		if (!RobotMap.HAS_GRABBER) {
			return;
		}
		
		double speed = 0.0;
		switch (state) {

		case GRAB:
			if(hasCube() || count > grabCount) {
				state = GrabberState.NEUTRAL;
			} else {
				speed = RobotMap.MAX_GRAB_SPEED;
			}
			break;

		case NEUTRAL:
			speed = 0.0;
			break; 

		case RELEASE:
			if(count > releaseCount) {
				state = GrabberState.NEUTRAL;
			} else {
				speed = -1 * RobotMap.MAX_GRAB_SPEED;
			}

			break;

		default:

		}
		
		left.set(speed);
		right.set(-1 * speed);
		count++;

		// Save the previous state and check for current state.
		hadCube = hasCube;
		hasCube = os.detectedTarget();
	}

	public void grab() {
		state = GrabberState.GRAB;
		count = 0;
	}

	public void release() {
		state = GrabberState.RELEASE;
		count = 0;
	}

	public void pause() {
		state = GrabberState.NEUTRAL;
	}

	public void grab(double throttle) {
		if (!RobotMap.HAS_GRABBER) {
			return;
		}

		if (Math.abs(throttle) < RobotMap.MIN_GRAB_SPEED) {
			throttle = 0.0;
		}

		if (justGotCube() && throttle < 0.0) {
			DriverStation.getInstance().setNavRumble(100);
			DriverStation.getInstance().setDriverRumble(100);
		}

		LOGGER.debug("Grabber Throttle=" + throttle);
		left.set(throttle * RobotMap.MAX_GRAB_SPEED);
		right.set(-1 * throttle * RobotMap.MAX_GRAB_SPEED);

		// Save the previous state and check for current state.
		hadCube = hasCube;
		hasCube = os.detectedTarget();
	}

	public boolean justGotCube() {
		return (!hadCube && hasCube());
	}
	
	public boolean hasCube() {
		return (os.detectedTarget() && RobotMap.HAS_GRABBER);
	}
}
