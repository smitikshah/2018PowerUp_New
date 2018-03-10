package org.usfirst.frc.team2869.robot.subsystems;

import org.usfirst.frc.team2869.robot.Looper;
import org.usfirst.frc.team2869.robot.RobotMap;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Cylinder extends Subsystem {
    public static Solenoid leftCylinder = new Solenoid(0);
    public static Solenoid rightCylinder = new Solenoid(1);
    private static boolean isActivated = leftCylinder.get();
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	public static void toggleCylinders(){
		if(isActivated){
			leftCylinder.set(true);
			rightCylinder.set(true);
		} else {
			leftCylinder.set(false);
			rightCylinder.set(false);
		}
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    public void extendCylinder() {
    	
    }
    
    public void retractCylinder() {
    	
    }
	
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
			}
}

