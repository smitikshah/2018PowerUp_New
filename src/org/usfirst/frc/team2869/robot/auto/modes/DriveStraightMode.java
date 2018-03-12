package org.usfirst.frc.team2869.robot.auto.modes;

import org.usfirst.frc.team2869.robot.util.auto.AutoModeBase;
import org.usfirst.frc.team2869.robot.util.auto.AutoModeEndedException;
import org.usfirst.frc.team2869.robot.auto.actions.DeserializePath;
import org.usfirst.frc.team2869.robot.auto.actions.DrivePathAction;

import java.io.IOException;

public class DriveStraightMode extends AutoModeBase {

    protected void routine() throws AutoModeEndedException {
        try {
            runAction(new DrivePathAction(DeserializePath.getPathFromFile("StraightPath")));
            //new ParallelAction(Arrays.asList(
            //,
            //		new MoveArmAction(ArmState.SWITCH_PLACE))
            //	));

            //runAction(new RollerAction(0.50, 1));
        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
        }
    }
}
