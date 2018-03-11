package org.usfirst.frc.team2869.robot.trajectory;

import org.usfirst.frc.team2869.robot.Constants;

public class PathFollower {

    protected TrajectoryFollower lFollower;
    protected TrajectoryFollower rFollower;
    protected Path mPath;

    public PathFollower(Path mPath, double distTol, double angTol) {
        this.mPath = mPath;
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

    public boolean onTarget() {
        return lFollower.onTarget() && rFollower.onTarget();
    }


}
