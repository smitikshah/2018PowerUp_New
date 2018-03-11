package org.usfirst.frc.team2869.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2869.robot.RobotState.ArmControlState;
import org.usfirst.frc.team2869.robot.RobotState.MatchState;
import org.usfirst.frc.team2869.robot.subsystems.Arm;
import org.usfirst.frc.team2869.robot.subsystems.Claw;
import org.usfirst.frc.team2869.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2869.robot.subsystems.Input;
import org.usfirst.frc.team2869.robot.util.logging.CrashTracker;
import org.usfirst.frc.team2869.robot.util.other.Looper;
import org.usfirst.frc.team2869.robot.util.other.SubsystemManager;

import java.util.Arrays;

public class Robot extends IterativeRobot {

    private final SubsystemManager mSubsystemManager = new SubsystemManager(
            Arrays.asList(DriveTrain.getInstance(), Arm.getInstance(), Claw.getInstance(), Input.getInstance()));
    private Looper mEnabledLooper = new Looper();

    DigitalInput lightSwitch;
    Spark floor;
    Input input;
    boolean ArmState;
    boolean armStateTest;
    Command autonomousCommand;
    boolean buttonToggle = false;
    //public static DoubleSolenoid armCylinder = new DoubleSolenoid(6,7);
    //public static Do5ubleSolenoid cubeGrabber = new DoubleSolenoid(4,5);


    //private static CameraServer camera;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {
        lightSwitch = new DigitalInput(1);
        input = new Input();
        try {
            CrashTracker.logRobotInit();
            mSubsystemManager.registerEnabledLoops(mEnabledLooper);
            AutoChooser.loadChooser();
        } catch (Throwable t) {
            CrashTracker.logThrowableCrash(t);
            throw t;
        }
    }

    @Override
    public void disabledInit() {
        try {
            CrashTracker.logDisabledInit();
            AutoChooser.disableAuto();
            mEnabledLooper.stop();
            RobotState.mMatchState = MatchState.DISABLED;
            RobotState.mArmState = RobotState.ArmState.ENABLE;
        } catch (Throwable t) {
            CrashTracker.logThrowableCrash(t);
            throw t;
        }
    }

    @Override
    public void disabledPeriodic() {
        allPeriodic();
    }

    public void autonomousInit() {
        try {
            CrashTracker.logAutoInit();
            RobotState.mMatchState = MatchState.AUTO;
            mEnabledLooper.start();
            AutoChooser.startAuto();
        } catch (Throwable t) {
            CrashTracker.logThrowableCrash(t);
            throw t;
        }
    }

    @Override
    public void autonomousPeriodic() {
        allPeriodic();
    }

    @Override
    public void teleopInit() {
        try {
            CrashTracker.logTeleopInit();
            RobotState.mMatchState = MatchState.TELEOP;
            RobotState.mDriveControlState = RobotState.DriveControlState.OPEN_LOOP;
            RobotState.mArmControlState = ArmControlState.MOTION_MAGIC;
            mEnabledLooper.start();
        } catch (Throwable t) {
            CrashTracker.logThrowableCrash(t);
            throw t;
        }
    }

    /**
     * This function is called periodically during operator control
     */
    @Override
    public void teleopPeriodic() {
        allPeriodic();
    }

    @Override
    public void testInit() {
        try {
            mEnabledLooper.start();
            mSubsystemManager.checkSystem();
        } catch (Throwable t) {
            CrashTracker.logThrowableCrash(t);
            throw t;
        }
    }

    @Override
    public void testPeriodic() {

    }

    private void allPeriodic() {
        try {
            mEnabledLooper.outputToSmartDashboard();
        } catch (Throwable t) {
            CrashTracker.logThrowableCrash(t);
            throw t;
        }
    }
}
