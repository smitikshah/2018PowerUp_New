package org.usfirst.frc.team2869.robot.commands.modes;

import org.usfirst.frc.team2869.robot.Constants;
import org.usfirst.frc.team2869.robot.Constants.DRIVE;
import org.usfirst.frc.team2869.robot.commands.auto.actions.AutoModeBase;
import org.usfirst.frc.team2869.robot.commands.auto.actions.AutoModeEndedException;
import org.usfirst.frc.team2869.robot.commands.auto.actions.DrivePathAction;
import org.usfirst.frc.team2869.robot.trajectory.Path;
import org.usfirst.frc.team2869.robot.trajectory.Trajectory;
import org.usfirst.frc.team2869.robot.trajectory.TrajectoryGenerator;

public class TurnInPlaceMode extends AutoModeBase {

    private double angle;
    private Path path;

    public TurnInPlaceMode(double angle) {
        this.angle = angle;
    }

    @Override
    protected void routine() throws AutoModeEndedException {
        TrajectoryGenerator.Config config = new TrajectoryGenerator.Config();

        config.dt = Constants.kLooperDt;
        config.max_acc = DRIVE.MAX_ANG_ACC;
        config.max_jerk = DRIVE.MAX_ANG_JERK;
        config.max_vel = DRIVE.MAX_ANG_VEL;

        Trajectory right = TrajectoryGenerator.generate(config,
                TrajectoryGenerator.TrapezoidalStrategy, 0.0, 0,
                angle, 0.0, 0);

        Trajectory left = right.copy();
        left.scale(-1);
        path = new Path("Turn In Place", new Trajectory.Pair(left, right));

        runAction(new DrivePathAction(path));
    }


}
