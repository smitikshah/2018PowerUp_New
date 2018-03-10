package org.usfirst.frc.team2869.robot.commands.modes;

import org.usfirst.frc.team2869.robot.AutoChooser;
import org.usfirst.frc.team2869.robot.AutoChooser.GameObjectPosition;
import org.usfirst.frc.team2869.robot.commands.auto.actions.AutoModeBase;
import org.usfirst.frc.team2869.robot.commands.auto.actions.AutoModeEndedException;
import org.usfirst.frc.team2869.robot.commands.auto.actions.DrivePathAction;

public class CenterSwitchMode extends AutoModeBase {

	private GameObjectPosition position;

	public CenterSwitchMode(AutoChooser.GameObjectPosition position) {
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
		//runAction(new DrivePathAction(new SwitchPath()));
	}

	private void rightRoutine() throws AutoModeEndedException {
			//runAction(new DrivePathAction(new SwitchPath()));
	}


}
