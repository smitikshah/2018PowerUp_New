package org.usfirst.frc.team2869.robot.commands.modes;

import java.io.IOException;

import org.usfirst.frc.team2869.robot.AutoChooser;
import org.usfirst.frc.team2869.robot.AutoChooser.GameObjectPosition;
import org.usfirst.frc.team2869.robot.commands.auto.actions.AutoModeBase;
import org.usfirst.frc.team2869.robot.commands.auto.actions.AutoModeEndedException;
import org.usfirst.frc.team2869.robot.commands.auto.actions.DeserializePath;
import org.usfirst.frc.team2869.robot.commands.auto.actions.DrivePathAction;
public class RightSwitchMode extends AutoModeBase {

	private GameObjectPosition position;

	public RightSwitchMode(AutoChooser.GameObjectPosition position) {
		this.position = position;
	}

	@Override
	protected void routine() throws AutoModeEndedException {
		switch (position) {
			case LEFT:
				leftRoutine();
				break;
			case RIGHT:
				rightRoutine();
				break;
		}
	}

	private void leftRoutine() throws AutoModeEndedException {
		try {
		runAction(new DrivePathAction(DeserializePath.getPathFromFile("CenterRightScale")));
		
	} catch (IOException e) {
		System.err.println("Caught IOException: " + e.getMessage());
	}
	}

	private void rightRoutine() throws AutoModeEndedException {
		try {
			runAction(new DrivePathAction(DeserializePath.getPathFromFile("CenterLeftScale")));
			
		} catch (IOException e) {
			System.err.println("Caught IOException: " + e.getMessage());
		}
	}


}
