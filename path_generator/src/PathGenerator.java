import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PathGenerator {


    public static final HashMap<String, Path> robotPaths = new HashMap<>();
    public static final Trajectory.Config defaultConfig = new Trajectory.Config(
            Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_HIGH, 0.005, 145, 95, 500);
    public static final Trajectory.Config slowConfig = new Trajectory.Config(
            Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_HIGH, 0.005, 100, 65, 120);
    public static final Trajectory.Config slowerConfig = new Trajectory.Config(
            Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_HIGH, 0.005, 100, 60, 100);
    public static final double SWITCH_X_OFFSET = 0;
    public static final double SWITCH_Y_OFFSET = 0;

    static {
        robotPaths.put("CS-1R",
                new Path(new Waypoint[]{
                        new Waypoint(22, -7, Pathfinder.d2r(0)),
                        new Waypoint(121, -58, Pathfinder.d2r(0))
                }, defaultConfig, false));

        robotPaths.put("CS-1L",
                new Path(new Waypoint[]{
                        new Waypoint(22, -7, Pathfinder.d2r(0)),
                        new Waypoint(121, 58, Pathfinder.d2r(0)),
                }, defaultConfig, false));

        robotPaths.put("DriveStraight",
                new Path(new Waypoint[]{
                        new Waypoint(23, 156, 0),
                        new Waypoint(127, 156, 0)
                }, defaultConfig, false));
    }

    public static void main(String[] args) {
        for (Map.Entry<String, Path> container : robotPaths.entrySet()) {
            container.getValue().setOffset(SWITCH_X_OFFSET, SWITCH_Y_OFFSET);
            if (container.getValue().bothSides) {
                File leftPathFile = new File("paths/" + container.getKey() + "L.csv").getAbsoluteFile();
                File rightPathFile = new File("paths/" + container.getKey() + "R.csv").getAbsoluteFile();
                Trajectory leftTraj = Pathfinder.generate(container.getValue().getPoints(), container.getValue().getConfig());
                Trajectory rightTraj = Pathfinder.generate(container.getValue().getRightPoints(), container.getValue().getConfig());
                Pathfinder.writeToCSV(leftPathFile, leftTraj);
                Pathfinder.writeToCSV(rightPathFile, rightTraj);
                System.out.println("Path: " + container.getKey() + " Time: " + leftTraj.length() * 0.005 + " Sec");
            } else {
                File pathFile = new File("paths/" + container.getKey() + ".csv").getAbsoluteFile();
                Trajectory trajectory = Pathfinder.generate(container.getValue().getPoints(), container.getValue().getConfig());
                Pathfinder.writeToCSV(pathFile, trajectory);
                System.out.println("Path: " + container.getKey() + " Time: " + trajectory.length() * 0.005 + " Sec");
            }
        }
    }


    static class Path {

        Waypoint[] points;
        Trajectory.Config config;
        boolean bothSides;

        public Path(Waypoint[] points, Trajectory.Config config, boolean bothSides) {
            this.points = points;
            this.config = config;
            this.bothSides = bothSides;
        }

        public Path(Waypoint[] points, Trajectory.Config config) {
            this(points, config, true);
        }

        public void setOffset(double x, double y) {
            for (Waypoint waypoint : points) {
                waypoint.y = waypoint.y + y;
                waypoint.x = waypoint.x + x;
            }
        }


        public Waypoint[] getPoints() {
            return points;

        }

        public Waypoint[] getRightPoints() {
            Waypoint[] waypoints = points.clone();
            for (Waypoint waypoint : waypoints) {
                waypoint.y = -waypoint.y;
                waypoint.angle = -waypoint.angle;
            }
            return waypoints;
        }

        public boolean getBothSides() {
            return bothSides;
        }

        public Trajectory.Config getConfig() {
            return config;
        }

    }

}
