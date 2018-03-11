package org.usfirst.frc.team2869.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team2869.robot.commands.Intake;
import org.usfirst.frc.team2869.robot.commands.LiftArm;
import org.usfirst.frc.team2869.robot.commands.LiftCube;
import org.usfirst.frc.team2869.robot.commands.Outtake;
import org.usfirst.frc.team2869.robot.subsystems.*;
import org.usfirst.frc.team2869.robot.subsystems.RobotState;
import org.usfirst.frc.team2869.robot.subsystems.RobotState.ArmControlState;
import org.usfirst.frc.team2869.robot.subsystems.RobotState.MatchState;
import org.usfirst.frc.team2869.robot.util.logging.CrashTracker;
import org.usfirst.frc.team2869.robot.util.other.Looper;

//import com.ctre.CANTalon;
//import com.ctre.CANTalon.*;
//import org.usfirst.frc.team2869.robot.commands.DriveDistance;
//import org.usfirst.frc.team2869.robot.commands.Rotate;
//import org.usfirst.frc.team2869.robot.commands.LeftCornerAutonomous;
//import org.usfirst.frc.team2869.robot.commands.MiddleAutonomous;
//import org.usfirst.frc.team2869.robot.commands.MoveClimber;
//import org.usfirst.frc.team2869.robot.commands.MoveRamp;
//import org.usfirst.frc.team2869.robot.commands.ReverseLift;
////import org.usfirst.frc.team2869.robot.commands.SpinClimber;
//import org.usfirst.frc.team2869.robot.commands.SpinIntake;
//import org.usfirst.frc.team2869.robot.commands.SpinLift;
//import org.usfirst.frc.team2869.robot.commands.SpinRoller;
//import org.usfirst.frc.team2869.robot.commands.SpinShooter;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
    public static final Cylinder cylinder = new Cylinder();
    public static OI oi;
    public static DriveTrain driveTrain;
    public static Arm arm;
    public static double moveValue, rotateValue;
    DigitalInput lightSwitch;
    Spark floor;
    Input input;
    boolean ArmState;
    boolean armStateTest;
    Command autonomousCommand;
    boolean buttonToggle = false;
    Timer timer = new Timer();
    private double MAX_VEL = 0; //Find Max Vel in native units per 100ms
    private double GEAR_RATIO = 0; //Put gear ratio from encoder to physical arm here
    private double TEST_MAX = 0;
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
        SmartDashboard.putNumber("Degrees", 0);
        oi = new OI();
        driveTrain = new DriveTrain();
        lightSwitch = new DigitalInput(1);
        input = new Input();
        arm = Arm.getInstance();
        try {
            AutoChooser.loadChooser();
        } catch (Throwable t) {
            CrashTracker.logThrowableCrash(t);
            throw t;
        }
		
		/*
		while(timer.get()<=1) {
		driveTrain.leftMotor01.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
		driveTrain.leftMotor02.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
		driveTrain.rightMotor01.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
		driveTrain.rightMotor02.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
		
				
		driveTrain.leftMotor01.setSelectedSensorPosition(0, 0, 10);
		driveTrain.leftMotor02.setSelectedSensorPosition(0, 0, 10);
		driveTrain.rightMotor01.setSelectedSensorPosition(0, 0, 10);
		driveTrain.rightMotor02.setSelectedSensorPosition(0, 0, 10);
	}
	*/

        //SmartDashboard.putDouble("P", .00005);
        //SmartDashboard.putDouble("I", 0);
        //SmartDashboard.putDouble("D", 0);
        //camera = CameraServer.getInstance();
        //camera.startAutomaticCapture().setResolution(256, 256);

        //SmartDashboard.putData("Auto chooser", chooser);
        //chooser.addObject("LeftCorner Autonomous", new LeftCornerAutonomous());
        //chooser.addObject("Middle Autonomous", new MiddleAutonomous());
        //chooser.addObject("RightCorner Autonomous", new RightCornerAutonomous());
        //chooser.addObject("Baseline Autonomous", new BaselineAutonomous());

//		driveTrain.leftMotor02.changeControlMode(TalonControlMode.Follower);
//		driveTrain.leftMotor02.set(driveTrain.leftMotor1.getDeviceID());
//		driveTrain.rightMotor02.changeControlMode(TalonControlMode.Follower);
//		driveTrain.rightMotor02.set(driveTrain.rightMotor1.getDeviceID());j


    }

    /**
     * This function is called once each time the robot enters Disabled mode.
     * You can use it to reset any subsystem information you want to clear when
     * the robot is disabled.
     */
    @Override
    public void disabledInit() {
        if (autonomousCommand != null)
            if (autonomousCommand.isRunning())
                autonomousCommand.cancel();
    }

    @Override
    public void disabledPeriodic() {
        Scheduler.getInstance().run();
    }


    /**
     * class NAVX
     * left setpoint .8 + (angle difference)*0.01
     * right setpoint .8 - (angle difference)*0.01;
     *
     */
    /**
     * This autonomous (along with the chooser code above) shows how to select
     * between different autonomous modes using the dashboard. The sendable
     * chooser code works with the Java SmartDashboard. If you prefer the
     * LabVIEW Dashboard, remove all of the chooser code and uncomment the
     * getString code to get the auto name from the text box below the Gyro
     * <p>
     * You can add additional auto modes by adding additional commands to the
     * chooser code above (like the commented example) or additional comparisons
     * to the switch structure below with additional strings & commands.
     */

    public void autonomousInit() {
        timer.reset();
        timer.start();
        @SuppressWarnings("unused")
        String gameData;
        gameData = DriverStation.getInstance().getGameSpecificMessage();
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

    /**
     * This function is called periodically during autonomous
     */
    @Override
    public void autonomousPeriodic() {
        allPeriodic();
        Scheduler.getInstance().run();
    }

    @Override
    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        //boolean armStateTest = false;
        //Assign all buttons
        OI.driverJoystick.GetRightBumper().whileHeld(new LiftArm());
        OI.driverJoystick.GetRightBumper().whileHeld(new LiftCube(false));

        OI.driverJoystick.GetLeftBumper().whileHeld(new LiftArm());
        OI.driverJoystick.GetLeftBumper().whileHeld(new LiftCube(true));

        OI.operatorJoystick.GetYButton().whileHeld(new Intake());
        OI.operatorJoystick.GetAButton().whileHeld(new Outtake());

        //OI.driverJoystick.GetAButton().toggleWhenPressed(new CubeGrab());
        //OI.driverJoystick.GetBButton().toggleWhenPressed(new Rotate(180));
        RobotState.mArmControlState = ArmControlState.MOTION_MAGIC;

        if (autonomousCommand != null)
            autonomousCommand.cancel();
        if (AutoChooser.getAutoModeExecuter() != null)
            AutoChooser.getAutoModeExecuter().stop();
        input.registerEnabledLoops(mEnabledLooper);


    }

    /**
     * This function is called periodically during operator control
     */
    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        allPeriodic();
        //input.updateDriveInput();
        driveTrain.updateRacingDrive(OI.driverJoystick.GetRightTrigger(), -OI.driverJoystick.GetLeftTrigger(), OI.driverJoystick.GetLeftX(), true);


        //input.updateDriveInput();
        //driveTrain.d(OI.driverJoystick.GetAllTriggers(),-OI.driverJoystick.GetLeftX());

        //driveTrain.setOpenLoop(new DriveSignal(OI.driverJoystick.GetAllTriggers(),OI.driverJoystick.GetLeftX()));
        //OI.driverJoystick.GetBButton().toggleWhenPressed(new Rotate(180));
        if (ArmState != armStateTest) {
            if (armStateTest = true) {
                System.out.println("Arm Up");
                armStateTest = false;
            }
        } else {

        }
//		driveTrain.drive.arcadeDrive(OI.driverJoystick.GetAllTriggers(),-OI.driverJoystick.GetLeftX());
//		
//		
//		if (OI.driverJoystick.) {
//			buttonToggle = !buttonToggle;
//		}
//		System.out.println(buttonToggle);
//		System.out.println(RobotMap.leftMotor1.getSpeed());

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
