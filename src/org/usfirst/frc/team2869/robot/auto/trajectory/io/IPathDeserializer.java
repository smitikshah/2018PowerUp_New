package org.usfirst.frc.team2869.robot.auto.trajectory.io;

import org.usfirst.frc.team2869.robot.auto.trajectory.Path;

/**
 * Interface for methods that deserializes a Path or Trajectory.
 */
public interface IPathDeserializer {

    public Path deserialize(String serialized);
}
