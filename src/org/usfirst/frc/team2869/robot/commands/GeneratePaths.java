package org.usfirst.frc.team2869.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.*;

/**
 *
 */
public class GeneratePaths extends Command {

    public GeneratePaths() {
        Waypoint[] points = new Waypoint[]{
                new Waypoint(-4, -1, Pathfinder.d2r(-45)),
                new Waypoint(-2, -2, 0),
                new Waypoint(-4, -1, 0)

        };
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
