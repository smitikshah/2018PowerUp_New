package org.usfirst.frc.team2869.robot.commands.modes;

import org.usfirst.frc.team2869.robot.commands.auto.actions.AutoModeBase;
import org.usfirst.frc.team2869.robot.commands.auto.actions.AutoModeEndedException;

/**
 * Fallback for when all autonomous modes do not work, resulting in a robot standstill
 */
public class StandStillMode extends AutoModeBase {

    @Override
    protected void routine() throws AutoModeEndedException {
        System.out.println("Starting Stand Still Mode... Done!");
    }
}
