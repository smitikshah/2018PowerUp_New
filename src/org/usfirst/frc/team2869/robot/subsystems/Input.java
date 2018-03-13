package org.usfirst.frc.team2869.robot.subsystems;

import org.usfirst.frc.team2869.robot.Constants;
import org.usfirst.frc.team2869.robot.RobotState;
import org.usfirst.frc.team2869.robot.util.drivers.MkXboxController;
import org.usfirst.frc.team2869.robot.util.drivers.MkXboxControllerButton;
import org.usfirst.frc.team2869.robot.util.other.*;

public class Input extends Subsystem {
    private final MkXboxController driverJoystick = new MkXboxController(0);
    private final MkXboxController operatorJoystick = new MkXboxController(1);
    private final MkXboxControllerButton driveModeChangeButton = driverJoystick.getButton(1, "Change Drive Mode");

    private final MkXboxControllerButton armIntakeButton = operatorJoystick.getButton(MkXboxController.ABUTTON, "Arm Intake");
    private final MkXboxControllerButton armSecondIntakeButton = operatorJoystick.getButton(MkXboxController.XBUTTON, "Arm Second Intake");
    private final MkXboxControllerButton armPlaceButton = operatorJoystick.getButton(MkXboxController.YBUTTON, "Arm Place");
    private final MkXboxControllerButton armChangeModeButton = operatorJoystick.getButton(MkXboxController.BACK_BUTTON, "Arm Change Mode");
    //    private final MkXboxControllerButton armPortalButton = operatorJoystick.getButton(MkXboxController.BBUTTON, "Arm Portal Mode");


    private final MkXboxControllerButton intakeOut = operatorJoystick.getButton(MkXboxController.LEFT_BUMPER, "Intake Roller Out");
    private final MkXboxControllerButton intakeIn = operatorJoystick.getButton(MkXboxController.RIGHT_BUMPER, "Intake Roller In");

    public Input() {

    }

    public static Input getInstance() {
        return InstanceHolder.mInstance;
    }


    @Override
    public void outputToSmartDashboard() {

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
                        updateDriveInput();
                        updateArmInput();
                    }
                }
            }

            @Override
            public void onStop(double timestamp) {
            }
        };
        enabledLooper.register(mLoop);
    }

    public synchronized void updateDriveInput() {
        DriveSignal sig = DriveHelper.SmitiDrive(driverJoystick.getRawAxis(MkXboxController.RIGHT_TRIGGER), -driverJoystick.getRawAxis(MkXboxController.LEFT_TRIGGER), driverJoystick.getRawAxis(MkXboxController.LEFT_XAXIS), true);
      /*  if (driveModeChangeButton.isPressed()) {
            RobotState.mDriveControlState =
                    RobotState.mDriveControlState.equals(RobotState.DriveControlState.OPEN_LOOP)
                            ? RobotState.DriveControlState.VELOCITY_SETPOINT : RobotState.DriveControlState.OPEN_LOOP;
        } */
        if (RobotState.mDriveControlState == RobotState.DriveControlState.VELOCITY_SETPOINT) {
            DriveTrain.getInstance().setVelocitySetpoint(sig, 0, 0);
        } else {
            DriveTrain.getInstance().setOpenLoop(sig);
        }
    }

    public synchronized void updateArmInput() {
        switch (RobotState.mArmControlState) {
            case MOTION_MAGIC:
                if (armIntakeButton.isPressed()) {
                    RobotState.mArmState = RobotState.ArmState.INTAKE;
                } else if (armSecondIntakeButton.isPressed()) {
                    RobotState.mArmState = RobotState.ArmState.SECOND_INTAKE;
                } else if (armPlaceButton.isPressed()) {
                    RobotState.mArmState = RobotState.ArmState.SWITCH_PLACE;
                }
                if (armChangeModeButton.isPressed()) {
                    RobotState.mArmControlState = RobotState.ArmControlState.OPEN_LOOP;
                }
                break;
            case OPEN_LOOP:
                Arm.getInstance()
                        .setOpenLoop(MkMath
                                .handleDeadband(operatorJoystick.getRawAxis(MkXboxController.LEFT_YAXIS),
                                        Constants.INPUT.OPERATOR_DEADBAND));
                if (armChangeModeButton.isPressed()) {
                    Arm.getInstance().setEnable();
                    RobotState.mArmControlState = RobotState.ArmControlState.MOTION_MAGIC;
                }
                break;
            default:
                System.out
                        .println("Unexpected arm control state: " + RobotState.mArmControlState);
                break;
        }


        if (intakeOut.isHeld()) {
            Arm.getInstance().setIntakeRollers(Constants.ARM.INTAKE_OUT_ROLLER_SPEED);
        } else if (intakeIn.isHeld()) {
            Arm.getInstance().setIntakeRollers(Constants.ARM.INTAKE_IN_ROLLER_SPEED);
        } else {
            Arm.getInstance().setIntakeRollers(0);
        }
    }

    public static class InstanceHolder {

        private static final Input mInstance = new Input();
    }
}
