package org.usfirst.frc.team2869.robot.commands;

import org.usfirst.frc.team2869.robot.subsystems.Arm;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class GrabLiftCube extends CommandGroup {

    public GrabLiftCube() {
    	new Arm();
    }
}
