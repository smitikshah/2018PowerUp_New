package org.usfirst.frc.team2869.robot.auto.modes;


import org.usfirst.frc.team2869.robot.Constants;
import org.usfirst.frc.team2869.robot.RobotState;
import org.usfirst.frc.team2869.robot.auto.actions.CurveOpenLoopAction;
import org.usfirst.frc.team2869.robot.auto.actions.OpenLoopAction;
import org.usfirst.frc.team2869.robot.auto.actions.RollerAction;
import org.usfirst.frc.team2869.robot.auto.actions.WaitAction;
import org.usfirst.frc.team2869.robot.util.auto.AutoModeBase;
import org.usfirst.frc.team2869.robot.util.auto.AutoModeEndedException;

public class SwitchOpenLoop extends AutoModeBase {

    @Override
    protected void routine() throws AutoModeEndedException {

        runAction(new CurveOpenLoopAction(0.95, -1, false));
        runAction(new OpenLoopAction(0.6, -0.35, true));
        runAction(new WaitAction(0.1));


    }
}
