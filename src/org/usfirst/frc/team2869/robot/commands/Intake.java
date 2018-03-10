package org.usfirst.frc.team2869.robot.commands;

import org.usfirst.frc.team2869.robot.subsystems.Arm;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Intake extends Command {
	VictorSPX leftIntake = Arm.getInstance().leftIntakeRollerTalon;
	VictorSPX rightIntake = Arm.getInstance().rightIntakeRollerTalon;
	private static final double speed = -.5;
    public Intake() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	leftIntake.set(ControlMode.PercentOutput, speed);
    	rightIntake.set(ControlMode.PercentOutput, -speed);
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
    	leftIntake.set(ControlMode.PercentOutput, 0);
    	rightIntake.set(ControlMode.PercentOutput, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	leftIntake.set(ControlMode.PercentOutput, 0);
    	rightIntake.set(ControlMode.PercentOutput, 0);
    }
}
