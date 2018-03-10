package org.usfirst.frc.team2869.robot.subsystems;

import org.usfirst.frc.team2869.robot.Looper;
import org.usfirst.frc.team2869.robot.RobotMap;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class PressureSensor extends Subsystem {
	AnalogInput pressureSensor = new AnalogInput(0);
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public double getPressure() {
    	return pressureSensor.getAverageVoltage() * RobotMap.pressureConstant;
    }

}

