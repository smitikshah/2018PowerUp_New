package org.usfirst.frc.team2869.robot.subsystems;

import org.usfirst.frc.team2869.robot.Constants;
import org.usfirst.frc.team2869.robot.RobotState;
import org.usfirst.frc.team2869.robot.util.drivers.Xbox360;
import org.usfirst.frc.team2869.robot.util.other.*;

public class Input extends Subsystem {
    private final Xbox360 driverJoystick;
    private final Xbox360 operatorJoystick;

    public Input() {
        driverJoystick = new Xbox360(0);
        operatorJoystick = new Xbox360(1);
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
        DriveSignal sig = DriveHelper.SmitiDrive(driverJoystick.GetRightTrigger(), -driverJoystick.GetLeftTrigger(), driverJoystick.GetLeftX(), true);
        DriveTrain.getInstance().setOpenLoop(sig);
    }

    public synchronized void updateArmInput() {
        if (operatorJoystick.GetYButton().get()) {
            Claw.getInstance().setIntakeRollers(Constants.ARM.INTAKE_SPEED);
        } else if (operatorJoystick.GetAButton().get()) {
            Claw.getInstance().setIntakeRollers(Constants.ARM.OUTTAKE_SPEED);
        } else {
            Claw.getInstance().setIntakeRollers(0);
        }
    }

    public static class InstanceHolder {

        private static final Input mInstance = new Input();
    }
}
