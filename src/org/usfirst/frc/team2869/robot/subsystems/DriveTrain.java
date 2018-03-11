package org.usfirst.frc.team2869.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team2869.robot.Constants.DRIVE;
import org.usfirst.frc.team2869.robot.RobotState;
import org.usfirst.frc.team2869.robot.RobotState.DriveControlState;
import org.usfirst.frc.team2869.robot.auto.trajectory.Path;
import org.usfirst.frc.team2869.robot.auto.trajectory.PathFollower;
import org.usfirst.frc.team2869.robot.auto.trajectory.TrajectoryStatus;
import org.usfirst.frc.team2869.robot.util.drivers.MkTalon;
import org.usfirst.frc.team2869.robot.util.drivers.MkTalon.TalonPosition;
import org.usfirst.frc.team2869.robot.util.other.DriveSignal;
import org.usfirst.frc.team2869.robot.util.other.Loop;
import org.usfirst.frc.team2869.robot.util.other.Looper;
import org.usfirst.frc.team2869.robot.util.other.Subsystem;

public class DriveTrain extends Subsystem {
    private MkTalon leftDrive, rightDrive;
    private AHRS navX;
    private PathFollower pathFollower = null;
    private TrajectoryStatus leftStatus;
    private TrajectoryStatus rightStatus;
    private DriveSignal currentSetpoint;

    private DriveTrain() {
        leftDrive = new MkTalon(DRIVE.LEFT_MASTER_ID, DRIVE.LEFT_SLAVE_ID, TalonPosition.Left);
        rightDrive = new MkTalon(DRIVE.RIGHT_MASTER_ID, DRIVE.RIGHT_SLAVE_ID, TalonPosition.Right);
        leftDrive.setPIDF();
        rightDrive.setPIDF();
        //leftDrive.setBrakeMode();
        //rightDrive.setBrakeMode();
        navX = new AHRS(Port.kMXP);
        zeroGyro();

        leftDrive.invertMaster(DRIVE.LEFT_MASTER_INVERT);
        leftDrive.invertSlave(DRIVE.LEFT_SLAVE_INVERT);
        leftDrive.setSensorPhase(DRIVE.LEFT_INVERT_SENSOR);

        rightDrive.invertMaster(DRIVE.RIGHT_MASTER_INVERT);
        rightDrive.invertSlave(DRIVE.RIGHT_SLAVE_INVERT);
        rightDrive.setSensorPhase(DRIVE.RIGHT_INVERT_SENSOR);

        leftStatus = TrajectoryStatus.NEUTRAL;
        rightStatus = TrajectoryStatus.NEUTRAL;
        currentSetpoint = DriveSignal.NEUTRAL;
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
     * @param path     Robot Path
     * @param dist_tol Position Tolerance for Path Follower
     * @param ang_tol  Robot Angle Tolerance for Path Follower (Degrees)
     */
    public synchronized void setDrivePath(Path path, double dist_tol, double ang_tol) {
        pathFollower = new PathFollower(path, dist_tol, ang_tol);
        RobotState.mDriveControlState = RobotState.DriveControlState.PATH_FOLLOWING;
    }

    public synchronized boolean isPathFinished() {
        if (pathFollower.getFinished()) {
            setVelocitySetpoint(DriveSignal.NEUTRAL);
            RobotState.mDriveControlState = DriveControlState.VELOCITY_SETPOINT;
            pathFollower = null;
            return true;
        }
        return false;
    }

    /**
     * Called from Looper during Path Following
     * Gets a TrajectoryStatus containing output velocity and Desired Trajectory Information for logging
     * Inputs Position, Speed and Angle to Trajectory Follower
     * Creates a new Drive Signal that is then set as a velocity setpoint
     */
    private synchronized void updatePathFollower() {
        TrajectoryStatus leftUpdate = pathFollower
                .getLeftVelocity(leftDrive.getPosition(), leftDrive.getSpeed(),
                        navX.getYaw());
        TrajectoryStatus rightUpdate = pathFollower
                .getRightVelocity(rightDrive.getPosition(), rightDrive.getSpeed(),
                        navX.getYaw());

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
            SmartDashboard.putNumber("NavX Full Yaw", navX.getYaw());
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
                    leftDrive.resetEncoder();
                    rightDrive.resetEncoder();
                    navX.zeroYaw();
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
                    switch (RobotState.mDriveControlState) {
                        case OPEN_LOOP:
                            return;
                        case VELOCITY_SETPOINT:
                            return;
                        case PATH_FOLLOWING:
                            updatePathFollower();
                            return;
                        default:
                            System.out
                                    .println("Unexpected drive control state: " + RobotState.mDriveControlState);
                            break;
                    }
                }
            }

            @Override
            public void onStop(double timestamp) {
                setOpenLoop(DriveSignal.NEUTRAL);
            }
        };
        enabledLooper.register(mLoop);
    }

    private void zeroGyro() {
        navX.zeroYaw();
    }

    public synchronized void updateRacingDrive(double forward, double reverse, double turn, boolean cubeInputs) {
        RobotState.mDriveControlState = DriveControlState.OPEN_LOOP;
        double deadband = .08;
        double power = 0.0;

        if (turn < deadband && turn > -deadband) turn = 0.0;
        if (forward < deadband && reverse > -deadband) forward = 0.0;
        if (reverse < deadband && reverse > -deadband) reverse = 0.0;

        if (forward > 0.0) power = forward;
        else power = reverse;

        double left;
        double right;

        left = power + turn;
        right = power - turn;
        leftDrive.set(ControlMode.PercentOutput, left);
        rightDrive.set(ControlMode.PercentOutput, right);
    }

    private static class InstanceHolder {

        private static final DriveTrain mInstance = new DriveTrain();
    }


}
