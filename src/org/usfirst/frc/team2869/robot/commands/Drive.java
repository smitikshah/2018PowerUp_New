package org.usfirst.frc.team2869.robot.commands;

import org.usfirst.frc.team2869.robot.Robot;
import org.usfirst.frc.team2869.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.TimedCommand;

/**
 *
 */
public class Drive extends Command {
	long timeStarted;
	long timeToRun;
    public Drive(long time) {
        this.timeToRun = time;
        
        //requires(driveTrain);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	timeStarted = System.currentTimeMillis();
    }

    // Called repeatedly when this Command is scheduled to run
    @SuppressWarnings("deprecation")
	protected void execute() {
    	//Robot.driveTrain.drive.drive(.5, 0);
    	System.out.println("Running Drive");
    }

    // Called once after timeout
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return System.currentTimeMillis() - timeStarted >= timeToRun;
	}
}
