package org.usfirst.frc.team2869.robot.auto.modes;

import edu.wpi.first.wpilibj.DriverStation;
import org.usfirst.frc.team2869.robot.AutoChooser;
import org.usfirst.frc.team2869.robot.Constants;
import org.usfirst.frc.team2869.robot.RobotState;
import org.usfirst.frc.team2869.robot.auto.actions.*;
import org.usfirst.frc.team2869.robot.util.auto.AutoModeBase;
import org.usfirst.frc.team2869.robot.util.auto.AutoModeEndedException;

import java.util.Arrays;

public class CenterSwitchMode extends AutoModeBase {

    public CenterSwitchMode() {

    }

    protected void routine() throws AutoModeEndedException {
        System.out.println("Starting Center Switch Mode");
        runAction(new ParallelAction(Arrays.asList(
                new DrivePathAction(1, false, false),
                new MoveArmAction(RobotState.ArmState.SWITCH_PLACE),
                new DelayAction(getTime(1) - 0.5, new RollerAction(0.45, Constants.ARM.AUTO_INTAKE_OUT_ROLLER_SPEED)))));

        runAction(new ParallelAction(Arrays.asList(
                new DrivePathAction(2, true, true),
                new DelayAction(0.3, new MoveArmAction(RobotState.ArmState.INTAKE))
        )));

        runAction(new ParallelAction(Arrays.asList(
                new DrivePathAction(3, false, true),
                new RollerAction(getTime(3), Constants.ARM.INTAKE_IN_ROLLER_SPEED, true)
        )));

        runAction(new DrivePathAction(4, true, false));

        runAction(new ParallelAction(Arrays.asList(
                new DrivePathAction(5, false, false),
                new DelayAction(0.5, new MoveArmAction(RobotState.ArmState.SWITCH_PLACE)),
                new DelayAction(getTime(5) - 0.475, new RollerAction(0.5, Constants.ARM.AUTO_INTAKE_OUT_ROLLER_SPEED / 1.5))
        )));

        runAction(new ParallelAction(Arrays.asList(
                new DrivePathAction(6, true, true),
                new DelayAction(0.5, new MoveArmAction(RobotState.ArmState.SECOND_INTAKE))
        )));

        runAction(new ParallelAction(Arrays.asList(
                new DrivePathAction(7, false, true),
                new RollerAction(getTime(7), Constants.ARM.INTAKE_IN_ROLLER_SPEED, true)
        )));

        runAction(new DrivePathAction(8, true, false));

        runAction(new ParallelAction(Arrays.asList(
                new DelayAction(0.5, new MoveArmAction(RobotState.ArmState.SWITCH_PLACE)),
                new DrivePathAction(9, false, false),
                new DelayAction(
                        getTime(9) - 0.65, new RollerAction(0.75, Constants.ARM.AUTO_INTAKE_OUT_ROLLER_SPEED / 1.5)))));
    }

    private double getTime(int pathNum) {
        return AutoChooser.autoPaths.get(
                "CS-" + Integer.toString(pathNum) + ((RobotState.matchData.switchPosition
                        == AutoChooser.GameObjectPosition.LEFT) ? "L" : "R") + ((RobotState.matchData.alliance
                        == DriverStation.Alliance.Blue) ? "B" : "R")).getTime();
    }


}