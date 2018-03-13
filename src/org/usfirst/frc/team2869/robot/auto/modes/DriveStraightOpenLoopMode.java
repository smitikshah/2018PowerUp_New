package org.usfirst.frc.team2869.robot.auto.modes;


import org.usfirst.frc.team2869.robot.auto.actions.OpenLoopAction;
import org.usfirst.frc.team2869.robot.util.auto.AutoModeBase;
import org.usfirst.frc.team2869.robot.util.auto.AutoModeEndedException;

public class DriveStraightOpenLoopMode extends AutoModeBase {
    @Override
    protected void routine() throws AutoModeEndedException {
        runAction(new OpenLoopAction(2, -0.4, false));
        runAction(new OpenLoopAction(1.5, -0.2, true));
    }
}
