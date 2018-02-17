package org.usfirst.frc.team467.robot.vision;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoCamera.WhiteBalance;
import edu.wpi.first.wpilibj.CameraServer;

//import javafx.scene.shape.Box;

public class VisionProcessing {
	
	public Mat source;

	public double cubeCenterPointY;
	public double cubeCenterPointX;
	
	private DetectPowerCubePipeline pipeline;
	private static final Logger LOGGER = Logger.getLogger(VisionProcessing.class);
	private static VisionProcessing instance;

	private double windowHeight;
	private double windowWidth;
	private double cameraAngleToCube;
	
	public static final double CUBE_WIDTH = 13.0;
	public static final double CUBE_HEIGHT = 11.0;
	private static final double FOCAL_LENGTH = 634;

	private UsbCamera camera = null;

	private double average;
	private MovingAverage averageAngle;


	
		
	private VisionProcessing() {
		pipeline = new DetectPowerCubePipeline();
		averageAngle = new MovingAverage(25); 
	}

	public static VisionProcessing getInstance() {
		if (instance == null) {
			instance = new VisionProcessing();
		}
		return instance;
	}

	public double findCube(Mat source) {
		double cameraDistanceX;
		double cameraDistanceY;
		double lengthOfShortLeg;
		double cubeDistance = 19.5; // Manually measured for now.
		cameraAngleToCube = Double.NaN;

		// grabFrame();
		if (!source.empty()) {
			pipeline.process(source);
			windowHeight = source.size().height;
			windowWidth = source.size().width;
			ArrayList<Rect> boundingBoxes = new ArrayList<Rect>();
			for (MatOfPoint points : pipeline.convexHullsOutput()) {
				Rect box = Imgproc.boundingRect(points);
				boundingBoxes.add(box);
				LOGGER.info("ADDED Bounding BOX X:" + box.x + " Y: " + box.y + " H: " + box.height + " W: " + box.width);
				cubeCenterPointY = (box.height / 2) + box.y;
				cubeCenterPointX = (box.width / 2) + box.x;
				Imgproc.rectangle(source, new Point(box.x, box.y), new Point(box.x + box.width, box.y + box.height),
						new Scalar(0, 255, 0, 0), 5);
//
				// if (box.height < box.width) {
				// //TODO : This configuration has it so that the grabber barely fits, be exact.
				// System.out.println("orientation 1");
				// System.out.println("Cube center point Y: " + cubeCenterPointY + " Cube center
				// point X: " + cubeCenterPointX);
				//
				//
				// }
				// if(box.height > box.width) {
				// System.out.println("orientation 2");
				// //TODO : Configure this orientation, it has more slop than orientation 1.
				// System.out.println("Cube center point Y: " + cubeCenterPointY + " Cube center
				// point X: " + cubeCenterPointX);
				//
				// }
				
				cameraDistanceX = cubeCenterPointX - (windowWidth / 2);
				cameraDistanceY = cubeCenterPointY - (windowHeight / 2);
				
				cameraAngleToCube = 0.00294524375 * cameraDistanceX;
				
				if(!Double.isNaN(cameraAngleToCube)) {
					average = averageAngle.average(cameraAngleToCube);
				}

				LOGGER.info("Angle Measure in Degrees = " +
						 Math.toDegrees(average));

				//				cameraAngleToCube = Math.atan2(cameraDistanceX, FOCAL_LENGTH);

				// System.out.println("Window width: " + windowWidth + " Window Height: " +
				// windowHeight); //The width is 640 pixels and the height is 480 pixels.
				// System.out.println("Camera distance X: " + cameraDistanceX + " Camera
				// distance Y: " + cameraDistanceY);

				// System.out.println("x: " + box.x + " y: " + box.y + " width: " + box.width +
				// " height: " + box.height);
				// System.out.println("Angle Measure in Radians = " + cameraAngleToCube);
				break;
			}
		}
		return Math.toDegrees(average);
	}

	/**
	 * This method is a generated getter for the output of a Find_Contours.
	 * 
	 * @return ArrayList<MatOfPoint> output from Find_Contours.
	 */
	public void findContoursOutput() {
		System.out.println("COUNT: " + pipeline.filterContoursOutput().size());
		for (MatOfPoint points : pipeline.convexHullsOutput()) {
			System.out.println("Test " + points);
		}
	}

	public double angleMeasure() {
		return cameraAngleToCube;
	}
	
	public double averageAngle() {
		return average;
	}

}