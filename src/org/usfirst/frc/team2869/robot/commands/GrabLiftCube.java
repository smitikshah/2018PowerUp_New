package org.usfirst.frc.team2869.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team2869.robot.subsystems.Arm;

/**
 *
 */
public class GrabLiftCube extends CommandGroup {

    public GrabLiftCube() {
        new Arm();
    }
}
