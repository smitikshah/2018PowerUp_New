package org.usfirst.frc.team2869.robot.trajectory.io;

import org.usfirst.frc.team2869.robot.trajectory.Path;
/**
 * Interface for methods that serialize a Path or Trajectory.
 *
 * 
 */
public interface IPathSerializer {

	public String serialize(Path path);
}
