import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PathGen {

    public static final HashMap<String, Path> robotPaths = new HashMap<>();
    public static final Trajectory.Config fastConfig = new Trajectory.Config(
            Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_HIGH,
            0.005, 145, 135/4, 1500);
    public static final Trajectory.Config defaultConfig = new Trajectory.Config(
            Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config
            .SAMPLES_HIGH, 0.005, 140, 95/4, 850); 

    public static final Trajectory.Config slowConfig = new Trajectory.Config(
            Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_HIGH,
            0.005, 100, 85/4, 700);

    public static final double BLUE_LEFT_SWITCH_TO_SIDE_WALL = 0;
    public static final double BLUE_RIGHT_SWITCH_TO_SIDE_WALL = 0;
    public static final double BLUE_SWITCH_TO_WALL = 140;
    public static final double BLUE_SWITCH_X_OFFSET = BLUE_SWITCH_TO_WALL - 140;
    public static final double BLUE_SWITCH_Y_OFFSET =
            (BLUE_RIGHT_SWITCH_TO_SIDE_WALL - BLUE_LEFT_SWITCH_TO_SIDE_WALL) / 2;

    static {

        robotPaths.put("CS-1", new Path(new Waypoint[]{
                new Waypoint(22, -7, Pathfinder.d2r(0)),
                new Waypoint(121, -59, Pathfinder.d2r(0))
        }, fastConfig, true));

        robotPaths.put("CS-2", new Path(new Waypoint[]{
                new Waypoint(121, -59, Pathfinder.d2r(0)),
                new Waypoint(40, 0, Pathfinder.d2r(0)),
        }, defaultConfig)); //for 3 cube fastconfig

        robotPaths.put("CS-3", new Path(new Waypoint[]{
                new Waypoint(40, 0, Pathfinder.d2r(0)),
                new Waypoint(85, 0, Pathfinder.d2r(0)),
        }, defaultConfig));

        robotPaths.put("CS-4", new Path(new Waypoint[]{
                new Waypoint(85, 0, Pathfinder.d2r(0)),
                new Waypoint(40, 0, Pathfinder.d2r(0)),
        }, fastConfig));

        robotPaths.put("CS-5", new Path(new Waypoint[]{
                new Waypoint(40, 0, Pathfinder.d2r(0)),
                new Waypoint(121, -59, Pathfinder.d2r(0)),
        }, fastConfig));

        robotPaths.put("CS-6", new Path(new Waypoint[]{
                new Waypoint(121, -59, Pathfinder.d2r(0)),
                new Waypoint(40, 0, Pathfinder.d2r(0)),
        }, fastConfig));

        robotPaths.put("CS-7", new Path(new Waypoint[]{
                new Waypoint(40, 0, Pathfinder.d2r(0)),
                new Waypoint(85, 0, Pathfinder.d2r(0)),
        }, defaultConfig));

        robotPaths.put("CS-8", new Path(new Waypoint[]{
                new Waypoint(85, 0, Pathfinder.d2r(0)),
                new Waypoint(40, 0, Pathfinder.d2r(0)),
        }, fastConfig));

        robotPaths.put("CS-9", new Path(new Waypoint[]{
                new Waypoint(40, 0, Pathfinder.d2r(0)),
                new Waypoint(121, -59, Pathfinder.d2r(0)),
        }, fastConfig));

        robotPaths.put("DriveStraight", new Path(new Waypoint[]{
                new Waypoint(23, 156, 0),
                new Waypoint(127, 156, 0),
        }, defaultConfig));

    }

    public static void main(String[] args) {
        double tiL = 0;
        double tiR = 0;
        for (Map.Entry<String, Path> container : robotPaths.entrySet()) {
            if (container.getValue().isBothSides()) {
                Path bTraj = container.getValue();

                bTraj.setOffset(BLUE_SWITCH_X_OFFSET, BLUE_SWITCH_Y_OFFSET);

                File leftBPathFile = new File("paths/" + container.getKey() + "L.csv").getAbsoluteFile();
                File rightBPathFile = new File("paths/" + container.getKey() + "R.csv").getAbsoluteFile();
                Trajectory leftBTraj = Pathfinder
                        .generate(bTraj.getLeftPoints(), bTraj.getConfig());
                Trajectory rightBTraj = Pathfinder
                        .generate(bTraj.getPoints(), bTraj.getConfig());

                Pathfinder.writeToCSV(leftBPathFile, leftBTraj);
                Pathfinder.writeToCSV(rightBPathFile, rightBTraj);

                System.out.println(
                        "Path: " + container.getKey() + " Time: " + leftBTraj.length() * 0.005 + " Sec");
                if (container.getKey().charAt(0) == 'C') {
                    tiL += leftBTraj.length() * 0.005;
                    tiR += rightBTraj.length() * 0.005;
                }
            } else {
                File pathFile = new File("paths/" + container.getKey() + ".csv").getAbsoluteFile();
                Trajectory trajectory = Pathfinder
                        .generate(container.getValue().getPoints(), container.getValue().getConfig());
                Pathfinder.writeToCSV(pathFile, trajectory);
                System.out.println(
                        "Path: " + container.getKey() + " Time: " + trajectory.length() * 0.005 + " Sec");
            }
        }
        System.out.println("Left: " + tiL + " Right: " + tiR);
    }

    static class Path {

        Waypoint[] points;
        Trajectory.Config config;
        boolean first;
        boolean bothSides;

        public Path(Waypoint[] points, Trajectory.Config config, boolean first, boolean bothSides) {
            this.points = points;
            this.config = config;
            this.first = first;
            this.bothSides = bothSides;
        }

        public Path(Waypoint[] points, Trajectory.Config config, boolean first) {
            this(points, config, first, true);
        }

        public Path(Waypoint[] points, Trajectory.Config config) {
            this(points, config, false, true);
        }


        public void setOffset(double x, double y) {
            if (first) {
                for (int i = 1; i < points.length; i++) {
                    points[i].x = points[i].x + x;
                    points[i].y = points[i].y + y;
                }
            } else {
                for (Waypoint waypoint : points) {
                    waypoint.x = waypoint.x + x;
                    waypoint.y = waypoint.y + y;
                }
            }
        }

        public Waypoint[] getLeftPoints(double x, double y) {
            Waypoint[] waypoints = new Waypoint[points.length];
            for (int i = 0; i < waypoints.length; i++) {
                waypoints[i] = new Waypoint(points[i].x, points[i].y, points[i].angle);
            }
            if (first) {
                for (int i = 1; i < waypoints.length; i++) {
                    waypoints[i].y *= -1.0;
                    waypoints[i].angle *= -1.0;
                    waypoints[i].x += x;
                    waypoints[i].y += y;
                }
            } else {
                for (Waypoint waypoint : waypoints) {
                    waypoint.y *= -1.0;
                    waypoint.angle *= -1.0;
                    waypoint.x += x;
                    waypoint.y += y;
                }
            }
            return waypoints;
        }

        public Waypoint[] getPoints(double x, double y) {
            Waypoint[] waypoints = new Waypoint[points.length];
            for (int i = 0; i < waypoints.length; i++) {
                waypoints[i] = new Waypoint(points[i].x, points[i].y, points[i].angle);
            }
            if (first) {
                for (int i = 1; i < waypoints.length; i++) {
                    waypoints[i].x += x;
                    waypoints[i].y += y;
                }
            } else {
                for (Waypoint waypoint : waypoints) {
                    waypoint.x += x;
                    waypoint.y += y;
                }
            }
            return waypoints;
        }

        public Waypoint[] getPoints() {
            return points;
        }

        public boolean isBothSides() {
            return bothSides;
        }

        public Waypoint[] getLeftPoints() {
            Waypoint[] waypoints = new Waypoint[points.length];
            for (int i = 0; i < waypoints.length; i++) {
                waypoints[i] = new Waypoint(points[i].x, points[i].y, points[i].angle);
            }
            if (first) {
                for (int i = 1; i < waypoints.length; i++) {
                    waypoints[i].y *= -1.0;
                    waypoints[i].angle *= -1.0;
                }
            } else {
                for (Waypoint waypoint : waypoints) {
                    waypoint.y *= -1.0;
                    waypoint.angle *= -1.0;
                }
            }
            return waypoints;
        }

        public Trajectory.Config getConfig() {
            return config;
        }

    }
    
   
    /* if I want to go slow copy this code
     * 
     *     public static final Trajectory.Config fastConfig = new Trajectory.Config(
            Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_HIGH,
            0.005, 120, 95, 800);
    public static final Trajectory.Config defaultConfig = new Trajectory.Config(
            Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config
            .SAMPLES_HIGH,  0.005, 80, 40, 500);
     */

}