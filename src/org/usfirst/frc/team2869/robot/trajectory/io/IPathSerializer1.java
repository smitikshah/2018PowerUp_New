package org.usfirst.frc.team2869.robot.trajectory.io;

import org.usfirst.frc.team2869.robot.trajectory.Trajectory;

/**
 * Interface for methods that serialize a Path or Trajectory.
 *
 * 
 */
public interface IPathSerializer1 {

	public String serialize(Trajectory traj);
}
