package org.usfirst.frc.team2869.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import org.usfirst.frc.team2869.robot.RobotState.ArmControlState;
import org.usfirst.frc.team2869.robot.RobotState.MatchState;
import org.usfirst.frc.team2869.robot.subsystems.Cylinder;
import org.usfirst.frc.team2869.robot.subsystems.Input;
import org.usfirst.frc.team2869.robot.util.logging.CrashTracker;
import org.usfirst.frc.team2869.robot.util.other.Looper;

public class Robot extends IterativeRobot {
    public static final Cylinder cylinder = new Cylinder();
    DigitalInput lightSwitch;
    Spark floor;
    Input input;
    boolean ArmState;
    boolean armStateTest;
    Command autonomousCommand;
    boolean buttonToggle = false;
    //public static DoubleSolenoid armCylinder = new DoubleSolenoid(6,7);
    //public static Do5ubleSolenoid cubeGrabber = new DoubleSolenoid(4,5);
    private Looper mEnabledLooper = new Looper();


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
            AutoChooser.loadChooser();
        } catch (Throwable t) {
            CrashTracker.logThrowableCrash(t);
            throw t;
        }
    }

    @Override
    public void disabledInit() {
        AutoChooser.disableAuto();
    }

    @Override
    public void disabledPeriodic() {
        Scheduler.getInstance().run();
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
        Scheduler.getInstance().run();
    }

    @Override
    public void teleopInit() {
        AutoChooser.disableAuto();


        //OI.driverJoystick.GetAButton().toggleWhenPressed(new CubeGrab());
        //OI.driverJoystick.GetBButton().toggleWhenPressed(new Rotate(180));
        RobotState.mArmControlState = ArmControlState.MOTION_MAGIC;

        input.registerEnabledLoops(mEnabledLooper);
        mEnabledLooper.start();

    }

    /**
     * This function is called periodically during operator control
     */
    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        allPeriodic();
    }

    /*
     * This function is called periodically during test mode
     */
    @Override
    public void testPeriodic() {
        LiveWindow.run();
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
