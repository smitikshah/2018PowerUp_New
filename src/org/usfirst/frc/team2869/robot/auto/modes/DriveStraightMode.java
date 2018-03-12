package org.usfirst.frc.team2869.robot.auto.modes;


import org.usfirst.frc.team2869.robot.AutoChooser;
import org.usfirst.frc.team2869.robot.auto.actions.DrivePathAction;
import org.usfirst.frc.team2869.robot.util.auto.AutoModeBase;
import org.usfirst.frc.team2869.robot.util.auto.AutoModeEndedException;

public class DriveStraightMode extends AutoModeBase {

    @Override
    protected void routine() throws AutoModeEndedException {

        runAction(new DrivePathAction(AutoChooser.autoPaths.get("DriveStraight"), false, false, false));

    }
}
