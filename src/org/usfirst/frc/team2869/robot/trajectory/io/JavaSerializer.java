package org.usfirst.frc.team2869.robot.trajectory.io;

import org.usfirst.frc.team2869.robot.trajectory.Path;
import org.usfirst.frc.team2869.robot.trajectory.Trajectory;

;

/**
 * Serialize a path to a Java file that can be compiled into a J2ME project.
 */
public class JavaSerializer implements IPathSerializer {

    /**
     * Generate a Java source code file from a Path
     * <p>
     * For example output, see the unit test.
     *
     * @param path The path to serialize.
     * @return A complete Java file as a string.
     */
    public String serialize(Path path) {
        String contents = "package frc.team1836.robot.auto.paths;\n\n";
        contents += "import frc.team254.lib.trajectory.Path;\n";
        contents += "import frc.team254.lib.trajectory.io.TextFileDeserializer;\n\n";
        contents += "public class " + path.getName() + " extends Path {\n";
        path.goLeft();
        contents += serializeTrajectory("kLeftWheel",
                path.getLeftWheelTrajectory());
        contents += serializeTrajectory("kRightWheel",
                path.getRightWheelTrajectory());

        contents += "  public " + path.getName() + "() {\n";
        contents += "    this.name_ = \"" + path.getName() + "\";\n";
        contents += "    this.go_left_pair_ = new Trajectory.Pair(kLeftWheel, kRightWheel);\n";
        contents += "  }\n\n";

        contents += "}\n";
        return contents;
    }

    private String serializeTrajectory(String name, Trajectory traj) {
        String contents =
                "  private final Trajectory " + name + " = new Trajectory( new Trajectory.Segment[] {\n";
        for (int i = 0; i < traj.getNumSegments(); ++i) {
            Trajectory.Segment seg = traj.getSegment(i);
            contents += "    new Trajectory.Segment("
                    + seg.pos + ", " + seg.vel + ", " + seg.acc + ", "
                    + seg.jerk + ", " + seg.heading + ", " + seg.dt + ", "
                    + seg.x + ", " + seg.y + "),\n";
        }
        contents += "  });\n\n";
        return contents;
    }

}
