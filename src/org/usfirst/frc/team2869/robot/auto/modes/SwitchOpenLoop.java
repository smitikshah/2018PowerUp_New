package org.usfirst.frc.team2869.robot.auto.modes;


import org.usfirst.frc.team2869.robot.RobotState.ArmState;
import org.usfirst.frc.team2869.robot.auto.actions.CurveOpenLoopAction;
import org.usfirst.frc.team2869.robot.auto.actions.MoveArmAction;
import org.usfirst.frc.team2869.robot.auto.actions.OpenLoopAction;
import org.usfirst.frc.team2869.robot.auto.actions.RollerAction;
import org.usfirst.frc.team2869.robot.auto.actions.WaitAction;
import org.usfirst.frc.team2869.robot.util.auto.AutoModeBase;
import org.usfirst.frc.team2869.robot.util.auto.AutoModeEndedException;

public class SwitchOpenLoop extends AutoModeBase {

    @Override
    protected void routine() throws AutoModeEndedException {

        runAction(new CurveOpenLoopAction(0.95, 1, false));
        runAction(new OpenLoopAction(0.6, 0.35, true));
        //runAction(new WaitAction(0.1));
        //runAction(new MoveArmAction(ArmState.INTAKE));
        //runAction(new RollerAction(2, 1));
        //runAction(new WaitAction(0.1));
        //runAction(new MoveArmAction(ArmState.SWITCH_PLACE));
        //runAction(new WaitAction(0.1));
        //runAction(new RollerAction(2, -1));


    }
}
