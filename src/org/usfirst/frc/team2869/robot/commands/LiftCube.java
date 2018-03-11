package org.usfirst.frc.team2869.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2869.robot.subsystems.CubeLift;

/**
 *
 */
public class LiftCube extends Command {
    boolean lift;

    public LiftCube(boolean lift) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        this.lift = lift;

    }

    // Called just before this Command runs the first time
    protected void initialize() {
        CubeLift.liftOrLower(lift);


    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
