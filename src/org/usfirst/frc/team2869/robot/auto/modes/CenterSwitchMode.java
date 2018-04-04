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

        runAction(new ParallelAction(Arrays.asList(
                new DrivePathAction(AutoChooser.autoPaths.get("CS-1L"), false, false, true),
                new MoveArmAction(RobotState.ArmState.SWITCH_PLACE),
                new DelayAction(AutoChooser.autoPaths.get("CS-1L").getTime() - 0.25, new RollerAction(0.45, Constants.ARM.AUTO_INTAKE_OUT_ROLLER_SPEED)))));

        runAction(new ParallelAction(Arrays.asList(
                new DrivePathAction(AutoChooser.autoPaths.get("CS-2L"), true, false, false),
                new DelayAction(0.3, new MoveArmAction(RobotState.ArmState.INTAKE))
        )));

        runAction(new ParallelAction(Arrays.asList(
                new DrivePathAction(AutoChooser.autoPaths.get("CS-3L"), false, false, true),
                new RollerAction(AutoChooser.autoPaths.get("CS-3L").getTime(), Constants.ARM.INTAKE_IN_ROLLER_SPEED, true)
        )));

        runAction(new DrivePathAction(AutoChooser.autoPaths.get("CS-4L"), true, false, true));

        runAction(new ParallelAction(Arrays.asList(
                new DrivePathAction(AutoChooser.autoPaths.get("CS-5L"), false, false, false),
                new DelayAction(0.5, new MoveArmAction(RobotState.ArmState.SWITCH_PLACE)),
                new DelayAction(AutoChooser.autoPaths.get("CS-5L").getTime() - 0.25, new RollerAction(0.5, Constants.ARM.AUTO_INTAKE_OUT_ROLLER_SPEED / 1.5))
        )));
    }

    private void rightRoutine() throws AutoModeEndedException {
        CrashTracker.logMarker("Starting Center Switch Mode (Right Side)");
        runAction(new ParallelAction(Arrays.asList(
                new DrivePathAction(AutoChooser.autoPaths.get("CS-1R"), false, false, true),
                new MoveArmAction(RobotState.ArmState.SWITCH_PLACE),
                new DelayAction(AutoChooser.autoPaths.get("CS-1R").getTime() - 0.25, new RollerAction(0.45, Constants.ARM.AUTO_INTAKE_OUT_ROLLER_SPEED)))));

        runAction(new ParallelAction(Arrays.asList(
                new DrivePathAction(AutoChooser.autoPaths.get("CS-2R"), true, false, false),
                new DelayAction(0.3, new MoveArmAction(RobotState.ArmState.INTAKE))
        )));

        runAction(new ParallelAction(Arrays.asList(
                new DrivePathAction(AutoChooser.autoPaths.get("CS-3R"), false, false, true),
                new RollerAction(AutoChooser.autoPaths.get("CS-3R").getTime(), Constants.ARM.INTAKE_IN_ROLLER_SPEED, true)
        )));

        runAction(new DrivePathAction(AutoChooser.autoPaths.get("CS-4R"), true, false, true));

        runAction(new ParallelAction(Arrays.asList(
                new DrivePathAction(AutoChooser.autoPaths.get("CS-5R"), false, false, false),
                new DelayAction(0.5, new MoveArmAction(RobotState.ArmState.SWITCH_PLACE)),
                new DelayAction(AutoChooser.autoPaths.get("CS-5R").getTime() - 0.25, new RollerAction(0.5, Constants.ARM.AUTO_INTAKE_OUT_ROLLER_SPEED / 1.5))
        )));
    }


}