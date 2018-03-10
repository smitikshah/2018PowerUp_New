package org.usfirst.frc.team2869.robot.commands;
import org.usfirst.frc.team2869.robot.Constants.DRIVE;
import org.usfirst.frc.team2869.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2869.robot.trajectory.Path;
import org.usfirst.frc.team2869.robot.trajectory.Trajectory;

import edu.wpi.first.wpilibj.command.Command;

public class DrivePathAction extends Command{

	private final Path path;

	public DrivePathAction(Path path) {
		this.path = path;
	}

	public boolean isFinished() {
		return DriveTrain.getInstance().isPathFinished();
	}

	public void update() {

	}

	public void done() {

	}

	public void start() {
		DriveTrain.getInstance().setDrivePath(path, DRIVE.PATH_DIST_TOL, DRIVE.PATH_ANGLE_TOL);
	}
}
