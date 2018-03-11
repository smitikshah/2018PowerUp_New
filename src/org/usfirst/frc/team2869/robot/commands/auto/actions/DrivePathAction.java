package org.usfirst.frc.team2869.robot.commands.auto.actions;

import org.usfirst.frc.team2869.robot.Constants.DRIVE;
import org.usfirst.frc.team2869.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2869.robot.trajectory.Path;

public class DrivePathAction implements Action {

    private final Path path;

    public DrivePathAction(Path path) {
        this.path = path;
    }

    @Override
    public boolean isFinished() {
        return DriveTrain.getInstance().isPathFinished();
    }

    @Override
    public void update() {

    }

    @Override
    public void done() {

    }

    @Override
    public void start() {
        DriveTrain.getInstance().setDrivePath(path, DRIVE.PATH_DIST_TOL, DRIVE.PATH_ANGLE_TOL);
    }
}
