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

    private final MkXboxControllerButton armUpButton = driverJoystick.getButton(1, "Arm Up");
    private final MkXboxControllerButton armDownButton = driverJoystick.getButton(2, "Arm Down");

    private final MkXboxControllerButton intakeOut = operatorJoystick.getButton(MkXboxController.YBUTTON, "Intake Roller Out");
    private final MkXboxControllerButton intakeIn = operatorJoystick.getButton(MkXboxController.ABUTTON, "Intake Roller In");

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
        if (driveModeChangeButton.isPressed()) {
            RobotState.mDriveControlState =
                    RobotState.mDriveControlState.equals(RobotState.DriveControlState.OPEN_LOOP)
                            ? RobotState.DriveControlState.VELOCITY_SETPOINT : RobotState.DriveControlState.OPEN_LOOP;
        }
        if (RobotState.mDriveControlState == RobotState.DriveControlState.VELOCITY_SETPOINT) {
            DriveTrain.getInstance().setVelocitySetpoint(sig, 0, 0);
        } else {
            DriveTrain.getInstance().setOpenLoop(sig);
        }
    }

    public synchronized void updateArmInput() {
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
