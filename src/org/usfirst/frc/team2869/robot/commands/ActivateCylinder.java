package org.usfirst.frc.team2869.robot.commands;

//import org.usfirst.frc.team2869.robot.Robot;
//import org.usfirst.frc.team2869.robot.subsystems.Cylinder;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ActivateCylinder extends Command {
	DoubleSolenoid.Value startValue;
    public ActivateCylinder() {
        //requires(Robot.cylinder);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	//startValue = Cylinder.toggleCylinders();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	/*
    	
    	if(startValue == DoubleSolenoid.Value.kForward)
    		Cylinder.leftCylinder.set(DoubleSolenoid.Value.kReverse);
    		Cylinder.rightCylinder.set(DoubleSolenoid.Value.kReverse);
    	else
    		Cylinder.leftCylinder.set(DoubleSolenoid.Value.kForward);
    		Cylinder.rightCylinder.set(DoubleSolenoid.Value.kReverse);
    		*/
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
       return null != null;
    	//return Cylinder.leftCylinder.get() != startValue;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
