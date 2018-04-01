package org.usfirst.frc.team2869.robot.auto.modes;

import org.usfirst.frc.team2869.robot.AutoChooser;
import org.usfirst.frc.team2869.robot.Constants;
import org.usfirst.frc.team2869.robot.RobotState;
import org.usfirst.frc.team2869.robot.auto.actions.OpenLoopFollowHeading;
import org.usfirst.frc.team2869.robot.auto.actions.RollerAction;
import org.usfirst.frc.team2869.robot.util.auto.AutoModeBase;
import org.usfirst.frc.team2869.robot.util.auto.AutoModeEndedException;
import org.usfirst.frc.team2869.robot.util.logging.CrashTracker;

public class CenterSwitchOpenLoopGyro extends AutoModeBase {

    private AutoChooser.GameObjectPosition position;

    public CenterSwitchOpenLoopGyro(AutoChooser.GameObjectPosition position) {
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


    protected void leftRoutine() throws AutoModeEndedException {
        System.out.println("Ran Left Open Loop Switch Auto");
        RobotState.mArmState = RobotState.ArmState.SWITCH_PLACE;
        runAction(new OpenLoopFollowHeading(0.5, 1.90, 0.75, 1.25, -85, 15));s
        runAction(new RollerAction(0.45, Constants.ARM.INTAKE_OUT_ROLLER_SPEED));
    }

    protected void rightRoutine() throws AutoModeEndedException {
        System.out.println("Ran Right Open Loop Switch Auto");
        RobotState.mArmState = RobotState.ArmState.SWITCH_PLACE;
        runAction(new OpenLoopFollowHeading(0.5, 1.75, 0.75, 1, 70, 0));
        runAction(new RollerAction(0.45, Constants.ARM.INTAKE_OUT_ROLLER_SPEED));
    }
}
