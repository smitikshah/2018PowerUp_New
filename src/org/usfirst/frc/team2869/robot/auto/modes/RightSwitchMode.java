package org.usfirst.frc.team2869.robot.auto.modes;

import org.usfirst.frc.team2869.robot.AutoChooser;
import org.usfirst.frc.team2869.robot.Constants;
import org.usfirst.frc.team2869.robot.auto.actions.DelayAction;
import org.usfirst.frc.team2869.robot.auto.actions.DrivePathAction;
import org.usfirst.frc.team2869.robot.auto.actions.RollerAction;
import org.usfirst.frc.team2869.robot.auto.actions.WaitAction;
import org.usfirst.frc.team2869.robot.util.auto.AutoModeBase;
import org.usfirst.frc.team2869.robot.util.auto.AutoModeEndedException;
import org.usfirst.frc.team2869.robot.util.logging.CrashTracker;

public class RightSwitchMode extends AutoModeBase {

    private AutoChooser.GameObjectPosition position;

    public RightSwitchMode(AutoChooser.GameObjectPosition position) {
        this.position = position;
    }

    @Override
    protected void routine() throws AutoModeEndedException {
        switch (position) {
            case LEFT:
                leftRoutine();
                break;
            case RIGHT:
                rightRoutine();
                break;
            default:
                CrashTracker.logMarker("Invalid Auto Position");
        }
    }

    private void leftRoutine() throws AutoModeEndedException {
        CrashTracker.logMarker("Starting Right Switch Mode (Left Side)");
        runAction(new DrivePathAction(AutoChooser.autoPaths.get("DriveStraight"), false, false, false));
        runAction(new RollerAction(0.5, Constants.ARM.INTAKE_OUT_ROLLER_SPEED));
    }

    private void rightRoutine() throws AutoModeEndedException {
        CrashTracker.logMarker("Starting Right Switch Mode (Right Side)");

        runAction(new DrivePathAction(AutoChooser.autoPaths.get("DriveStraight"), false, false, false));
        runAction(new RollerAction(0.5, Constants.ARM.INTAKE_OUT_ROLLER_SPEED));
    }


}
