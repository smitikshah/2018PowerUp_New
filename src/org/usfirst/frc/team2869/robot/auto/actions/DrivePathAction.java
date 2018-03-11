package org.usfirst.frc.team2869.robot.auto.actions;

import org.usfirst.frc.team2869.robot.Constants.DRIVE;
import org.usfirst.frc.team2869.robot.auto.trajectory.Path;
import org.usfirst.frc.team2869.robot.subsystems.DriveTrain;

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
