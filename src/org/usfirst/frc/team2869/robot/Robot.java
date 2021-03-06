package org.usfirst.frc.team2869.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import org.usfirst.frc.team2869.robot.RobotState.ArmControlState;
import org.usfirst.frc.team2869.robot.RobotState.MatchState;
import org.usfirst.frc.team2869.robot.subsystems.Arm;
import org.usfirst.frc.team2869.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2869.robot.subsystems.Input;
import org.usfirst.frc.team2869.robot.util.logging.CrashTracker;
import org.usfirst.frc.team2869.robot.util.other.Looper;
import org.usfirst.frc.team2869.robot.util.other.SubsystemManager;

import java.util.Arrays;

public class Robot extends IterativeRobot {

    private final SubsystemManager mSubsystemManager = new SubsystemManager(
            Arrays.asList(DriveTrain.getInstance(), Arm.getInstance(), Input.getInstance()));
    private Looper mEnabledLooper = new Looper();

    public void robotInit() {
        try {
            CrashTracker.logRobotInit();
            mSubsystemManager.registerEnabledLoops(mEnabledLooper);
            AutoChooser.loadAutos();
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

    public void autonomousInit() {
        try {
            CrashTracker.logAutoInit();
            RobotState.mMatchState = MatchState.AUTO;
            AutoChooser.updateGameData();
            mEnabledLooper.start();
            AutoChooser.startAuto();
        } catch (Throwable t) {
            CrashTracker.logThrowableCrash(t);
            throw t;
        }
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


    public void robotPeriodic() {
        try {
            mEnabledLooper.outputToSmartDashboard();
            mSubsystemManager.outputToSmartDashboard();
        } catch (Throwable t) {
            CrashTracker.logThrowableCrash(t);
            throw t;
        }
    }
}
