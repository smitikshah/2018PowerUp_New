package org.usfirst.frc.team2869.robot.trajectory;

import org.usfirst.frc.team2869.robot.trajectory.Trajectory.Segment;

public class TrajectoryStatus {


    public static TrajectoryStatus NEUTRAL = new TrajectoryStatus(new Segment(0, 0, 0, 0, 0, 0, 0, 0),
            0, 0, 0, 0);

    private double output;
    private Segment seg;
    private double posError;
    private double velError;
    private double angError;

    public TrajectoryStatus(Segment seg, double posError, double velError, double angError,
                            double output) {
        this.seg = seg;
        this.output = output;
        this.posError = posError;
        this.velError = velError;
        this.angError = angError;
    }

    public double getOutput() {
        return output;
    }

    public double getPosError() {
        return posError;
    }

    public double getVelError() {
        return velError;
    }

    public double getAngError() {
        return angError;
    }

    public Segment getSeg() {
        return seg;
    }

}
