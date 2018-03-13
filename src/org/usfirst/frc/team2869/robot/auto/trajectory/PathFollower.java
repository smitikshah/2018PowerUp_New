package org.usfirst.frc.team2869.robot.auto.trajectory;

import org.usfirst.frc.team2869.robot.Constants;
import org.usfirst.frc.team2869.robot.util.other.TrajectoryStatus;

public class PathFollower {

    protected TrajectoryFollower lFollower;
    protected TrajectoryFollower rFollower;
    private Path path;

    public PathFollower(Path mPath, double distTol, double angTol) {
        path = mPath;
        lFollower = new TrajectoryFollower(mPath.getLeftWheelTrajectory());
        lFollower.configure(Constants.DRIVE.DRIVE_FOLLOWER_P,
                Constants.DRIVE.DRIVE_FOLLOWER_A, Constants.DRIVE.DRIVE_FOLLOWER_ANG, distTol, angTol);
        rFollower = new TrajectoryFollower(mPath.getRightWheelTrajectory());
        rFollower.configure(Constants.DRIVE.DRIVE_FOLLOWER_P,
                Constants.DRIVE.DRIVE_FOLLOWER_A, -Constants.DRIVE.DRIVE_FOLLOWER_ANG, distTol, angTol);
    }


    public TrajectoryStatus getLeftVelocity(double dist, double vel, double angle) {
        return lFollower.calculate(dist, vel, angle);
    }

    public TrajectoryStatus getRightVelocity(double dist, double vel, double angle) {
        return rFollower.calculate(dist, vel, angle);
    }

    public boolean getFinished() {
        return lFollower.isFinishedTrajectory() && rFollower.isFinishedTrajectory();
    }

    public double getEndHeading() {
        return path.getEndHeading();
    }
}
