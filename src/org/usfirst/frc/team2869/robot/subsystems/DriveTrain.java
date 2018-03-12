package org.usfirst.frc.team2869.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import org.usfirst.frc.team2869.robot.Constants;
import org.usfirst.frc.team2869.robot.Constants.DRIVE;
import org.usfirst.frc.team2869.robot.RobotState;
import org.usfirst.frc.team2869.robot.auto.trajectory.Path;
import org.usfirst.frc.team2869.robot.auto.trajectory.PathFollower;
import org.usfirst.frc.team2869.robot.util.drivers.MkTalon;
import org.usfirst.frc.team2869.robot.util.logging.CrashTracker;
import org.usfirst.frc.team2869.robot.util.other.*;

public class DriveTrain extends Subsystem {

    private final MkTalon leftDrive, rightDrive;
    private final AHRS navX;
    private PathFollower pathFollower = null;
    private TrajectoryStatus leftStatus;
    private TrajectoryStatus rightStatus;
    private DriveSignal currentSetpoint;
    private double lastAngle = 0;
    private boolean brakePath = true;

    private DriveTrain() {
        leftDrive = new MkTalon(DRIVE.LEFT_MASTER_ID, DRIVE.LEFT_SLAVE_ID, MkTalon.TalonPosition.Left);
        rightDrive = new MkTalon(DRIVE.RIGHT_MASTER_ID, DRIVE.RIGHT_SLAVE_ID, MkTalon.TalonPosition.Right);
        leftDrive.setPIDF();
        rightDrive.setPIDF();
        navX = new AHRS(Port.kMXP);

        leftDrive.invertMaster(DRIVE.LEFT_MASTER_INVERT);
        leftDrive.invertSlave(DRIVE.LEFT_SLAVE_INVERT);
        leftDrive.setSensorPhase(DRIVE.LEFT_INVERT_SENSOR);

        rightDrive.invertMaster(DRIVE.RIGHT_MASTER_INVERT);
        rightDrive.invertSlave(DRIVE.RIGHT_SLAVE_INVERT);
        rightDrive.setSensorPhase(DRIVE.RIGHT_INVERT_SENSOR);

        leftStatus = TrajectoryStatus.NEUTRAL;
        rightStatus = TrajectoryStatus.NEUTRAL;
        currentSetpoint = DriveSignal.BRAKE;
    }

    public static DriveTrain getInstance() {
        return InstanceHolder.mInstance;
    }

    /* Controls Drivetrain in PercentOutput Mode (without closed loop control) */
    public synchronized void setOpenLoop(DriveSignal signal) {
        RobotState.mDriveControlState = RobotState.DriveControlState.OPEN_LOOP;
        leftDrive.set(ControlMode.PercentOutput, signal.getLeft(), signal.getBrakeMode());
        rightDrive.set(ControlMode.PercentOutput, signal.getRight(), signal.getBrakeMode());
        currentSetpoint = signal;
    }

    /**
     * Controls Drivetrain in Closed-loop velocity Mode
     * Method sets Talons in Native Units per 100ms
     *
     * @param signal An object that contains left and right velocities (inches per sec)
     */

    public synchronized void setVelocitySetpoint(DriveSignal signal, double leftFeed,
                                                 double rightFeed) {
        if (RobotState.mDriveControlState == RobotState.DriveControlState.PATH_FOLLOWING) {
            leftDrive.set(ControlMode.Velocity, signal.getLeftNativeVelTraj(), signal.getBrakeMode(),
                    leftFeed);
            rightDrive.set(ControlMode.Velocity, signal.getRightNativeVelTraj(), signal.getBrakeMode(),
                    rightFeed);
        } else {
            RobotState.mDriveControlState = RobotState.DriveControlState.VELOCITY_SETPOINT;
            leftDrive.set(ControlMode.Velocity, signal.getLeftNativeVel(), signal.getBrakeMode());
            rightDrive.set(ControlMode.Velocity, signal.getRightNativeVel(), signal.getBrakeMode());
        }
        currentSetpoint = signal;
    }

    /**
     * @param path     Robot Path
     * @param dist_tol Position Tolerance for Path Follower
     * @param ang_tol  Robot Angle Tolerance for Path Follower (Degrees)
     */
    public synchronized void setDrivePath(Path path, double dist_tol, double ang_tol,
                                          boolean brakeMode) {
        CrashTracker.logMarker("Began Path: " + path.getName());
        brakePath = brakeMode;
        double offset = lastAngle - Pathfinder
                .boundHalfDegrees(Pathfinder.r2d(path.getLeftWheelTrajectory().get(0).heading));
        for (Trajectory.Segment segment : path.getLeftWheelTrajectory().segments) {
            segment.heading = Pathfinder.boundHalfDegrees(Pathfinder.r2d(segment.heading) + offset);
        }
        for (Trajectory.Segment segment : path.getRightWheelTrajectory().segments) {
            segment.heading = Pathfinder.boundHalfDegrees(Pathfinder.r2d(segment.heading) + offset);
        }
        leftDrive.resetEncoder();
        rightDrive.resetEncoder();
        pathFollower = new PathFollower(path, dist_tol, ang_tol);
        RobotState.mDriveControlState = RobotState.DriveControlState.PATH_FOLLOWING;
    }

    public synchronized boolean isPathFinished() {
        if (pathFollower.getFinished()) {
            lastAngle = pathFollower.getEndHeading();
            RobotState.mDriveControlState = RobotState.DriveControlState.OPEN_LOOP;
            pathFollower = null;
            leftStatus = TrajectoryStatus.NEUTRAL;
            rightStatus = TrajectoryStatus.NEUTRAL;
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
                        -navX.getYaw());
        TrajectoryStatus rightUpdate = pathFollower
                .getRightVelocity(rightDrive.getPosition(), rightDrive.getSpeed(),
                        -navX.getYaw());
        leftStatus = leftUpdate;
        rightStatus = rightUpdate;
        if (isEncodersConnected()) {
            setVelocitySetpoint(new DriveSignal(leftUpdate.getOutput(), rightUpdate.getOutput(),
                            brakePath),
                    leftUpdate.getArbFeed(), rightUpdate.getArbFeed());
        } else {
            leftDrive.set(ControlMode.PercentOutput,
                    ((1.0 / MkMath.RPMToInchesPerSec(DRIVE.RIGHT_RPM_MAX)) * leftUpdate.getOutput()), false,
                    leftUpdate.getArbFeed());
            rightDrive.set(ControlMode.PercentOutput,
                    ((1.0 / MkMath.RPMToInchesPerSec(DRIVE.LEFT_RPM_MAX)) * rightUpdate.getOutput()), false,
                    rightUpdate.getArbFeed());
        }
    }

    @Override
    public void outputToSmartDashboard() {
        leftDrive.updateSmartDash();
        rightDrive.updateSmartDash();
        SmartDashboard.putString("Drive State", RobotState.mDriveControlState.toString());
        SmartDashboard.putBoolean("Drivetrain Status",
                leftDrive.isEncoderConnected() && rightDrive.isEncoderConnected());

        if (RobotState.mDriveControlState == RobotState.DriveControlState.PATH_FOLLOWING
                && leftStatus != TrajectoryStatus.NEUTRAL) {
            SmartDashboard.putNumber("NavX Yaw", navX.getYaw());
            SmartDashboard.putNumber("Heading Error", leftStatus.getAngError());
            /*SmartDashboard.putNumber("Left Desired Velocity", currentSetpoint.getLeft());
            SmartDashboard.putNumber("Right Desired Velocity", currentSetpoint.getRight());
            SmartDashboard.putNumber("Desired Heading", leftStatus.getSeg().heading);
            SmartDashboard.putNumber("Left Desired Position", leftStatus.getSeg().position);
            SmartDashboard.putNumber("Left Theoretical Vel", leftStatus.getSeg().velocity);
            SmartDashboard.putNumber("Left Position Error", leftStatus.getPosError());
            SmartDashboard.putNumber("Left Desired Velocity Error", leftStatus.getVelError());
            SmartDashboard.putNumber("Right Desired Position", leftStatus.getSeg().position);
            SmartDashboard.putNumber("Right Position Error", leftStatus.getPosError());
            SmartDashboard.putNumber("Right Theoretical Vel", rightStatus.getSeg().velocity);
            SmartDashboard.putNumber("Right Desired Velocity Error", leftStatus.getVelError()); */
        }
    }

    public double getYaw() {
        return navX.getYaw();
    }

    @Override
    public void checkSystem() {
        leftDrive.set(ControlMode.PercentOutput, 1, true);
        rightDrive.set(ControlMode.PercentOutput, 1, true);
        Timer.delay(5.0);
        leftDrive.set(ControlMode.PercentOutput, 0, true);
        rightDrive.set(ControlMode.PercentOutput, 0, true);
        boolean check = true;
        if (leftDrive.getPosition() < Constants.DRIVE.MIN_TEST_POS
                || leftDrive.getSpeed() < Constants.DRIVE.MIN_TEST_VEL) {
            System.out.println("FAILED - LEFT DRIVE FAILED TO REACH REQUIRED SPEED OR POSITION");
            System.out
                    .println("Position: " + leftDrive.getPosition() + " Speed: " + leftDrive.getSpeed());
            check = false;
            CrashTracker.logMarker(
                    "Left Drive Test Failed - Vel: " + leftDrive.getSpeed() + " Pos: " + leftDrive
                            .getPosition());
        } else {
            System.out
                    .println("Position: " + leftDrive.getPosition() + " Speed: " + leftDrive.getSpeed());
        }
        if (rightDrive.getPosition() < Constants.DRIVE.MIN_TEST_POS
                || rightDrive.getSpeed() < Constants.DRIVE.MIN_TEST_VEL) {
            System.out.println("FAILED - RIGHT DRIVE FAILED TO REACH REQUIRED SPEED OR POSITION");
            System.out
                    .println("Position: " + rightDrive.getPosition() + "Speed: " + rightDrive.getSpeed());
            check = false;
            CrashTracker.logMarker(
                    "Right Drive Test Failed - Vel: " + rightDrive.getSpeed() + " Pos: " + rightDrive
                            .getPosition());
        } else {
            System.out
                    .println("Position: " + rightDrive.getPosition() + " Speed: " + rightDrive.getSpeed());
        }

        if (!navX.isConnected()) {
            System.out.println("FAILED - NAVX DISCONNECTED");
            check = false;
        }

        if (check) {
            System.out.println("Drive Test Success");
        }

        leftDrive.resetConfig();
        rightDrive.resetConfig();
    }

    @Override
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
                            if (pathFollower != null) {
                                updatePathFollower();
                            }
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
                setOpenLoop(DriveSignal.BRAKE);
            }
        };
        enabledLooper.register(mLoop);
    }

    public boolean isEncodersConnected() {
        return leftDrive.isEncoderConnected() && rightDrive.isEncoderConnected();
    }

    private static class InstanceHolder {

        private static final DriveTrain mInstance = new DriveTrain();
    }

}
