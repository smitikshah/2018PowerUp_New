package org.usfirst.frc.team2869.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2869.robot.Robot;
import org.usfirst.frc.team2869.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2869.robot.trajectory.Path;
import org.usfirst.frc.team2869.robot.trajectory.Trajectory;

/**
 *
 */
public class Rotate extends Command {
    DriveTrain drive = Robot.driveTrain;
    double angle;

    public Rotate(double angle) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        this.angle = angle;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        drive.setDrivePath(new Path("Straight Path",
                new Trajectory.Pair(new Trajectory(1), new Trajectory(1))), 0, angle);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return drive.isPathFinished();
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
