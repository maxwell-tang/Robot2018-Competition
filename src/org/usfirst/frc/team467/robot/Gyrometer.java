package org.usfirst.frc.team467.robot;

import org.usfirst.frc.team467.robot.imu.ADIS16448_IMU;
import org.usfirst.frc.team467.robot.imu.IMU;
import org.usfirst.frc.team467.robot.imu.LSM9DS1_IMU;

/*
 *  Simple wrapper class around a gyro. This is implemented as a singleton
 */
public class Gyrometer {
	private IMU imu = null;
	private static Gyrometer instance;
	private double measuresPerDegree;


	/*
	 * private constructor (singleton pattern)
	 */
	private Gyrometer() {
		if (RobotMap.useRemoteImu) {
			imu = new LSM9DS1_IMU();
			measuresPerDegree = 1;
	
		} else {
			imu = new ADIS16448_IMU();
			measuresPerDegree = 4;
		}
	}

	/**
	 * Returns a single instance of the gyro object.
	 */
	public static Gyrometer getInstance() {
		if (instance == null) {
			instance = new Gyrometer();
		}
		return instance;
	}

	/*
	 * Reset gyro
	 */
	public void reset() {
		imu.reset();
	}

	/*
	 * Calibrate gyro
	 */
	public void calibrate() {
		imu.calibrate();
	}
	
	/**
	 * Returns the Z angle of the gyro in Radians. Note, the IMU returns 1440 degrees per rotation.
	 *
	 * @return the gyro angle
	 */
	public double yawRadians() {
		if (RobotMap.robotID == RobotMap.RobotID.PreseasonBot) {
		return imu.getAngleZ() * Math.PI / (180 * measuresPerDegree);
		} else {
			if (RobotMap.robotID == RobotMap.RobotID.Competition_1) {
			}return -imu.getAngleX() * Math.PI / (180 * measuresPerDegree);
		}
	}

	/**
	 * Returns the angle of the robot orientation in Degrees. Robot is assumed to be pointing forward at 0.0. Clockwise rotation is
	 * positive, counter clockwise rotation is negative
	 *
	 * @return the robot angle
	 */
	public double yawDegrees() {
		return Math.toDegrees(yawRadians());
	}

	/**
	 * Returns the X angle of the gyro in Radians. Note, the IMU returns 1440 degrees per rotation.
	 *
	 * @return the gyro angle
	 */
	public double rollRadians() {
		if (RobotMap.robotID == RobotMap.RobotID.PreseasonBot) {
		return imu.getAngleX() * Math.PI / (180 * measuresPerDegree);
		} else {
			if (RobotMap.robotID == RobotMap.RobotID.Competition_1) {
			} return -imu.getAngleY() * Math.PI / (180 * measuresPerDegree);
		} 
	}

	/**
	 * Returns the X angle of the gyro in Degrees. Note, the IMU returns 1440 degrees per rotation.
	 *
	 * @return the gyro angle
	 */
	public double rollDegrees() {
		return Math.toDegrees(rollRadians());
	}

	/**
	 * Returns the Y angle of the gyro in Radians. Note, the IMU returns 1440 degrees per rotation.
	 *
	 * @return the gyro angle
	 */
	public double pitchRadians() {
		if (RobotMap.robotID == RobotMap.RobotID.PreseasonBot) {
		return imu.getAngleY() * Math.PI / (180 * measuresPerDegree);
		} else {
		    if (RobotMap.robotID == RobotMap.RobotID.Competition_1) {
		    } return -imu.getAngleZ() * Math.PI / (180 * measuresPerDegree);
		}
	}

	/**
	 * Returns the Y angle of the gyro in Degrees. Note, the IMU returns 1440 degrees per rotation.
	 *
	 * @return the gyro angle
	 */
	public double pitchDegrees() {
		return Math.toDegrees(pitchRadians());
	}

}