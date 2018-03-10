package org.usfirst.frc.team2869.robot.subsystems;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team2869.robot.Constants.LOGGING;

import org.usfirst.frc.team2869.robot.Constants.DRIVE;
import org.usfirst.frc.team2869.robot.DriveSignal;
import org.usfirst.frc.team2869.robot.Looper;
import org.usfirst.frc.team2869.robot.Loop;
import org.usfirst.frc.team2869.robot.MkGyro;
import org.usfirst.frc.team2869.robot.MkMath;
import org.usfirst.frc.team2869.robot.MkTalon;
import org.usfirst.frc.team2869.robot.MkTalon.TalonPosition;
import org.usfirst.frc.team2869.robot.ReflectingCSVWriter;
import org.usfirst.frc.team2869.robot.RobotMap;
import org.usfirst.frc.team2869.robot.subsystems.RobotState.DriveControlState;
import org.usfirst.frc.team2869.robot.trajectory.Path;
import org.usfirst.frc.team2869.robot.trajectory.PathFollower;
import org.usfirst.frc.team2869.robot.trajectory.TrajectoryStatus;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
public class DriveTrain extends Subsystem {

	//private final ReflectingCSVWriter<DriveDebugOutput> mCSVWriter;
	public MkTalon leftDrive, rightDrive;
	public MkGyro navX;
	private PathFollower pathFollower = null;
	private DriveDebugOutput mDebug = new DriveDebugOutput();
	private TrajectoryStatus leftStatus;
	private TrajectoryStatus rightStatus;
	private DriveSignal currentSetpoint;
	@SuppressWarnings("deprecation")
	public DriveTrain() {
		leftDrive = new MkTalon(DRIVE.LEFT_MASTER_ID, DRIVE.LEFT_SLAVE_ID, TalonPosition.Left);
		rightDrive = new MkTalon(DRIVE.RIGHT_MASTER_ID, DRIVE.RIGHT_SLAVE_ID, TalonPosition.Right);
		leftDrive.setPIDF();
		rightDrive.setPIDF();
		//leftDrive.setBrakeMode();
		//rightDrive.setBrakeMode();
		navX = new MkGyro(Port.kMXP);
		zeroGyro();

		leftDrive.invertMaster(DRIVE.LEFT_MASTER_INVERT);
		leftDrive.invertSlave(DRIVE.LEFT_SLAVE_INVERT);
		leftDrive.setSensorPhase(DRIVE.LEFT_INVERT_SENSOR);

		rightDrive.invertMaster(DRIVE.RIGHT_MASTER_INVERT);
		rightDrive.invertSlave(DRIVE.RIGHT_SLAVE_INVERT);
		rightDrive.setSensorPhase(DRIVE.RIGHT_INVERT_SENSOR);

		//mCSVWriter = new ReflectingCSVWriter<>(LOGGING.DRIVE_LOG_PATH,
				//DriveDebugOutput.class);
		leftStatus = TrajectoryStatus.NEUTRAL;
		rightStatus = TrajectoryStatus.NEUTRAL;
		currentSetpoint = DriveSignal.NEUTRAL;

		//RobotDrive drive = new RobotDrive(RobotMap.leftMotor1, RobotMap.leftMotor2);
		//If getting Output not updated often enough error,
		// try doing
		/*
		 * drive = new RobotDrive(new WPI_TalonSRX(RobotMap.leftMotor1),
		 *  new WPI_TalonSRX(RobotMap.rightMotor1), new WPI_TalonSRX(RobotMap.leftMotor2),
		 *   new WPI_TalonSRX(RobotMap.rightMotor2));
		 */
	}
	




	public static DriveTrain getInstance() {
		return InstanceHolder.mInstance;
	}

	/*
		Controls Drivetrain in PercentOutput Mode (without closed loop control)
 */
	public synchronized void setOpenLoop(DriveSignal signal) {
		RobotState.mDriveControlState = DriveControlState.OPEN_LOOP;
		leftDrive.set(ControlMode.PercentOutput, signal.getLeft());
		rightDrive.set(ControlMode.PercentOutput, signal.getRight());
		currentSetpoint = signal;
	}

	/**
	 * Controls Drivetrain in Closed-loop velocity Mode
	 * Method sets Talons in Native Units per 100ms
	 *
	 * @param signal An object that contains left and right velocities (inches per sec)
	 */

	public synchronized void setVelocitySetpoint(DriveSignal signal) {
		if (RobotState.mDriveControlState == DriveControlState.PATH_FOLLOWING) {
			leftDrive.set(ControlMode.Velocity, signal.getLeftNativeVelTraj());
			rightDrive.set(ControlMode.Velocity, signal.getRightNativeVelTraj());
		} else {
			RobotState.mDriveControlState = DriveControlState.VELOCITY_SETPOINT;
			leftDrive.set(ControlMode.Velocity, signal.getLeftNativeVel());
			rightDrive.set(ControlMode.Velocity, signal.getRightNativeVel());
		}

		currentSetpoint = signal;
	}

	/**
	 * @param path Robot Path
	 * @param dist_tol Position Tolerance for Path Follower
	 * @param ang_tol Robot Angle Tolerance for Path Follower (Degrees)
	 */
	public synchronized void setDrivePath(Path path, double dist_tol, double ang_tol) {
		pathFollower = new PathFollower(path, dist_tol, ang_tol);
		RobotState.mDriveControlState = RobotState.DriveControlState.PATH_FOLLOWING;
	}

	public boolean isPathFinished() {
		if (pathFollower.getFinished()) {
			setVelocitySetpoint(DriveSignal.NEUTRAL);
			RobotState.mDriveControlState = DriveControlState.VELOCITY_SETPOINT;
			pathFollower = null;
			return true;
		}
		return false;
	}

	private synchronized void updateTurnInPlace() {
		TrajectoryStatus leftUpdate = pathFollower
				.getLeftVelocity(navX.getYaw(), navX.getRawGyroZ(), 0);
		TrajectoryStatus rightUpdate = pathFollower
				.getRightVelocity(navX.getYaw(), navX.getRawGyroZ(), 0);
		setVelocitySetpoint(new DriveSignal(MkMath.AngleToVel(leftUpdate.getOutput()),
				MkMath.AngleToVel(rightUpdate.getOutput())));
		leftStatus = leftUpdate;
		rightStatus = rightUpdate;
	}

	/**
	 * Called from Looper during Path Following
	 * Gets a TrajectoryStatus containing output velocity and Desired Trajectory Information for logging
	 * Inputs Position, Speed and Angle to Trajectory Follower
	 * Creates a new Drive Signal that is then set as a velocity setpoint
	 */
	private void updatePathFollower() {
		TrajectoryStatus leftUpdate = pathFollower
				.getLeftVelocity(leftDrive.getPosition(), leftDrive.getSpeed(),
						navX.getFullYaw());
		TrajectoryStatus rightUpdate = pathFollower
				.getRightVelocity(rightDrive.getPosition(), rightDrive.getSpeed(),
						navX.getFullYaw());

		leftStatus = leftUpdate;
		rightStatus = rightUpdate;
		setVelocitySetpoint(new DriveSignal(leftUpdate.getOutput(), rightUpdate.getOutput()));
	}

	public void writeToLog() {
		//mCSVWriter.write();
	}

	public void outputToSmartDashboard() {
		leftDrive.updateSmartDash();
		rightDrive.updateSmartDash();
		SmartDashboard.putNumber("NavX Yaw", navX.getYaw());
		SmartDashboard.putString("Drive State", RobotState.mDriveControlState.toString());
		if (RobotState.mDriveControlState == DriveControlState.PATH_FOLLOWING) {
			SmartDashboard.putNumber("Left Desired Velocity", currentSetpoint.getLeft());
			SmartDashboard.putNumber("Right Desired Velocity", currentSetpoint.getRight());
			SmartDashboard.putNumber("NavX Full Yaw", navX.getFullYaw());
			SmartDashboard.putNumber("Desired Heading", leftStatus.getSeg().heading);
			SmartDashboard.putNumber("Heading Error", leftStatus.getAngError());
			SmartDashboard.putNumber("Left Desired Position", leftStatus.getSeg().pos);
			SmartDashboard.putNumber("Left Theoretical Vel", leftStatus.getSeg().vel);
			SmartDashboard.putNumber("Left Position Error", leftStatus.getPosError());
			SmartDashboard.putNumber("Left Desired Velocity Error", leftStatus.getVelError());
			SmartDashboard.putNumber("Right Desired Position", leftStatus.getSeg().pos);
			SmartDashboard.putNumber("Right Position Error", leftStatus.getPosError());
			SmartDashboard.putNumber("Right Theoretical Vel", rightStatus.getSeg().vel);
			SmartDashboard.putNumber("Right Desired Velocity Error", leftStatus.getVelError());
		}
	}

	public void stop() {
		setOpenLoop(DriveSignal.NEUTRAL);
	}

	public void zeroSensors() {
		leftDrive.resetEncoder();
		rightDrive.resetEncoder();
		navX.zeroYaw();
	}

	public void checkSystem() {
		leftDrive.testDrive();
		rightDrive.testDrive();
		if (!navX.isConnected()) {
			System.out.println("FAILED - NAVX DISCONNECTED");
		}

	}

	public void registerEnabledLoops(Looper enabledLooper) {
		Loop mLoop = new Loop() {

			@Override
			public void onStart(double timestamp) {
				synchronized (DriveTrain.this) {

				}
			}

			/**
			 * Updated from mEnabledLoop in Robot.java
			 * Controls drivetrain during Path Following and Turn In Place and logs
			 * Drivetrain data in all modes
			 * @param timestamp In Seconds Since Code Start
			 */
			@Override
			public void onLoop(double timestamp) {
				synchronized (DriveTrain.this) {
					updateDebugOutput(timestamp);
					//mCSVWriter.add(mDebug);
					switch (RobotState.mDriveControlState) {
						case OPEN_LOOP:
							zeroTrajectoryStatus();
							return;
						case VELOCITY_SETPOINT:
							zeroTrajectoryStatus();
							return;
						case PATH_FOLLOWING:
							updatePathFollower();
							updateTrajectoryStatus();
							return;
						case TURN_IN_PLACE:
							updateTurnInPlace();
							updateTrajectoryStatus();
						default:
							System.out
									.println("Unexpected drive control state: " + RobotState.mDriveControlState);
							break;
					}
				}
			}
			@Override
			public void onStop(double timestamp) {
				stop();
			}
		};
		enabledLooper.register(mLoop);
	}

	private void zeroGyro() {
		navX.zeroYaw();
	}

	public double getYaw() {
		return navX.getYaw();
	}

	private void updateDebugOutput(double timestamp) {
		mDebug.timestamp = timestamp;
		mDebug.controlMode = RobotState.mDriveControlState.toString();
		mDebug.leftOutput = leftDrive.getPercentOutput();
		mDebug.rightOutput = rightDrive.getPercentOutput();
		mDebug.rightPosition = leftDrive.getPosition();
		mDebug.leftPosition = rightDrive.getPosition();
		mDebug.leftVelocity = leftDrive.getSpeed();
		mDebug.rightVelocity = rightDrive.getSpeed();
		mDebug.heading = navX.getFullYaw();
		mDebug.leftSetpoint = currentSetpoint.getLeft();
		mDebug.rightSetpoint = currentSetpoint.getRight();
	}

	private void zeroTrajectoryStatus() {
		mDebug.leftDesiredPos = 0;
		mDebug.leftDesiredVel = 0;
		mDebug.rightDesiredPos = 0;
		mDebug.rightDesiredVel = 0;
		mDebug.desiredHeading = 0;
		mDebug.headingError = 0;
		mDebug.leftVelError = 0;
		mDebug.leftPosError = 0;
		mDebug.rightVelError = 0;
		mDebug.rightPosError = 0;
		mDebug.desiredX = 0;
		mDebug.desiredY = 0;
	}

	private void updateTrajectoryStatus() {
		mDebug.leftDesiredPos = leftStatus.getSeg().pos;
		mDebug.leftDesiredVel = leftStatus.getSeg().vel;
		mDebug.rightDesiredPos = rightStatus.getSeg().pos;
		mDebug.rightDesiredVel = rightStatus.getSeg().vel;
		mDebug.desiredHeading = leftStatus.getSeg().heading;
		mDebug.headingError = leftStatus.getAngError();
		mDebug.leftVelError = leftStatus.getVelError();
		mDebug.leftPosError = leftStatus.getPosError();
		mDebug.rightVelError = rightStatus.getVelError();
		mDebug.rightPosError = rightStatus.getPosError();
		mDebug.desiredX = (leftStatus.getSeg().x + rightStatus.getSeg().x) / 2;
		mDebug.desiredY = (leftStatus.getSeg().y + rightStatus.getSeg().y) / 2;
	}

	public static class DriveDebugOutput {

		double timestamp;
		String controlMode;
		double leftOutput;
		double rightOutput;
		double leftSetpoint;
		double rightSetpoint;
		double leftPosition;
		double rightPosition;
		double leftVelocity;
		double rightVelocity;
		double heading;
		double desiredHeading;
		double headingError;
		double leftDesiredVel;
		double leftDesiredPos;
		double leftPosError;
		double leftVelError;
		double rightDesiredVel;
		double rightDesiredPos;
		double rightPosError;
		double rightVelError;
		double desiredX;
		double desiredY;
	}

	private static class InstanceHolder {

		private static final DriveTrain mInstance = new DriveTrain();
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}






	public synchronized void updateRacingDrive(double forward, double reverse, double turn, boolean cubeInputs){
        RobotState.mDriveControlState = DriveControlState.OPEN_LOOP;
        double deadband = .08;
        double power = 0.0;
        
        if(turn < deadband && turn > -deadband) turn = 0.0;
        if(forward < deadband && reverse > -deadband) forward = 0.0;
        if(reverse < deadband && reverse > -deadband) reverse = 0.0;
        
        if(forward > 0.0) power = forward;
        else power = reverse;
        
       double left;
       double right;
       
       left = power + turn;
       right = power - turn;
        leftDrive.set(ControlMode.PercentOutput, left);
        rightDrive.set(ControlMode.PercentOutput, right);    
    }


}
