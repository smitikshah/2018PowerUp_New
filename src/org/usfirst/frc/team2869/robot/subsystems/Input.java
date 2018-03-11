package org.usfirst.frc.team2869.robot.subsystems;

import org.usfirst.frc.team2869.robot.OI;
import org.usfirst.frc.team2869.robot.RobotState;
import org.usfirst.frc.team2869.robot.RobotState.DriveControlState;
import org.usfirst.frc.team2869.robot.util.drivers.Xbox360;
import org.usfirst.frc.team2869.robot.util.other.*;

public class Input extends Subsystem {


    //private final Joystick operatorJoystick = new Joystick(1);


    private final Xbox360 driverJoystick = OI.driverJoystick;
    //private final Xbox360 JoystickButton clawButton = OI.driverJoystick.GetXButton();
    //private final JoystickButton armBumperButton = operatorJoystick.getButton(4, "Arm Bumper");
    //private final JoystickButton armSwitchButton = operatorJoystick.getButton(6, "Arm Switch");
    //private final JoystickButton armSwitchReverseButton = operatorJoystick
    //.getButton(1, "Arm Switch Reverse");
    //private final MkJoystickButton armChangeModeButton = operatorJoystick
    //.getButton(8, "Arm Change Mode");
    //private final MkJoystickButton armZeroButton = operatorJoystick.getButton(9, "Arm Zero");
    //private final MkJoystickButton intakeRollerIn = operatorJoystick
    //.getButton(3,
    //"Intake Roller In");
    //private final MkJoystickButton intakeRollerOut = operatorJoystick
    //.getButton(5,
    //"Intake Roller Out");


    public Input() {

    }

    public static Input getInstance() {
        return InstanceHolder.mInstance;
    }


    @Override
    public void outputToSmartDashboard() {

    }

    @Override
    public void stop() {

    }

    public void zeroSensors() {

    }

    @Override
    public void checkSystem() {

    }

    @Override
    public void registerEnabledLoops(Looper enabledLooper) {
        Loop mLoop = new Loop() {

            @Override
            public void onStart(double timestamp) {
                synchronized (Input.this) {

                }
            }

            @Override
            public void onLoop(double timestamp) {
                synchronized (Input.this) {
                    if (RobotState.mMatchState.equals(RobotState.MatchState.TELEOP)) {
                        updateDriveInput(); // Could be an issue 2-20-18
                        //updateArmInput();
                    }
                }
            }

            @Override
            public void onStop(double timestamp) {
            }
        };
        enabledLooper.register(mLoop);
    }

    public void updateDriveInput() {
        double a = DriveHelper
                .cheesyDrive((driverJoystick.GetAllTriggers()),
                        (driverJoystick.GetLeftX()), true).getLeft();
        double b = DriveHelper
                .cheesyDrive((driverJoystick.GetAllTriggers()),
                        (driverJoystick.GetLeftX()), true).getRight();
        DriveSignal sig = new DriveSignal(a * .5, b * .5);
        if (RobotState.mDriveControlState == DriveControlState.VELOCITY_SETPOINT) {
            DriveTrain.getInstance().setVelocitySetpoint(sig);
            //	System.out.println("Rotate: " + -driverJoystick.getRawAxis(0) + "Throttle: " + -driverJoystick.getRawAxis(2) + driverJoystick.getRawAxis(3));
        } else if (RobotState.mDriveControlState == DriveControlState.OPEN_LOOP) {
            DriveTrain.getInstance().setOpenLoop(sig);
        }


    }

    public static class InstanceHolder {

        private static final Input mInstance = new Input();
    }
}
