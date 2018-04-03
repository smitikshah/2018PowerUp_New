package org.usfirst.frc.team2869.robot.auto.modes;

import org.usfirst.frc.team2869.robot.AutoChooser;
import org.usfirst.frc.team2869.robot.Constants;
import org.usfirst.frc.team2869.robot.RobotState;
import org.usfirst.frc.team2869.robot.auto.actions.*;
import org.usfirst.frc.team2869.robot.util.auto.AutoModeBase;
import org.usfirst.frc.team2869.robot.util.auto.AutoModeEndedException;
import org.usfirst.frc.team2869.robot.util.logging.CrashTracker;

import java.util.Arrays;

public class CenterSwitchMode extends AutoModeBase {

    private AutoChooser.GameObjectPosition position;

    public CenterSwitchMode(AutoChooser.GameObjectPosition position) {
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
        CrashTracker.logMarker("Starting Center Switch Mode (Left Side)");
        runAction(new ParallelAction(Arrays
                .asList(
                        new DrivePathAction(AutoChooser.autoPaths.get("CS-1L"), false, false, false),
                        new MoveArmAction(RobotState.ArmState.SWITCH_PLACE),
                        new DelayAction(AutoChooser.autoPaths.get("CS-1L").getTime() - 0.25, new RollerAction(0.3, Constants.ARM.AUTO_INTAKE_OUT_ROLLER_SPEED))

                )));
    }

    private void rightRoutine() throws AutoModeEndedException {
        CrashTracker.logMarker("Starting Center Switch Mode (Right Side)");
        runAction(new ParallelAction(Arrays
                .asList(

                        new DrivePathAction(AutoChooser.autoPaths.get("CS-1R"), false, false, false),
                        new MoveArmAction(RobotState.ArmState.SWITCH_PLACE),
                        new DelayAction(AutoChooser.autoPaths.get("CS-1R").getTime() - 0.25, new RollerAction(0.3, Constants.ARM.AUTO_INTAKE_OUT_ROLLER_SPEED))

                )));
    }


}