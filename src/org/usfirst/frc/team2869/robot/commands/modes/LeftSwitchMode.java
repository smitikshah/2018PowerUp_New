package org.usfirst.frc.team2869.robot.commands.modes;

import org.usfirst.frc.team2869.robot.AutoChooser;
import org.usfirst.frc.team2869.robot.AutoChooser.GameObjectPosition;
import org.usfirst.frc.team2869.robot.commands.auto.actions.AutoModeBase;
import org.usfirst.frc.team2869.robot.commands.auto.actions.AutoModeEndedException;

public class LeftSwitchMode extends AutoModeBase {

	private GameObjectPosition position;

	public LeftSwitchMode(AutoChooser.GameObjectPosition position) {
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

	}

	private void rightRoutine() throws AutoModeEndedException {

	}


}
