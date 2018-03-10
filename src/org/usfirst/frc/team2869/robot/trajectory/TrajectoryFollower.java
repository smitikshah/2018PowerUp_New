package org.usfirst.frc.team2869.robot.trajectory;

import edu.wpi.first.wpilibj.Timer;

import org.usfirst.frc.team2869.robot.trajectory.Trajectory.Segment;

/**
 * PID + Feedforward controller for following a Trajectory.
 *
 * 
 */
public class TrajectoryFollower {

	private double kp_;
	private double kAng_;
	private double kv_;
	private double ka_;
	private double last_error_;
	private double current_heading = 0;
	private int current_segment;
	private boolean firstRun = false;
	private Trajectory profile_;
	private double Dt;
	private double last_Ang_error;
	private double _DistTol;
	private double _AngTol;

	public TrajectoryFollower(Trajectory profile) {

		profile_ = profile;
	}

	public void configure(double kp, double ka, double kAng, double distTol,
			double angTol) {
		kp_ = kp;
		kAng_ = kAng;
		ka_ = ka;
		_DistTol = distTol;
		_AngTol = Math.toRadians(angTol);
		reset();
	}

	public void reset() {
		last_error_ = 0.0;
		current_segment = 0;
		firstRun = false;
	}

	public TrajectoryStatus calculate(double dist, double vel, double heading) {
		if (firstRun) {
			firstRun = true;
			Dt = Timer.getFPGATimestamp();
		}
		double currentTime = Timer.getFPGATimestamp();
		current_segment = (int) (customRound(currentTime - Dt) / 0.005);
		System.out.println(current_segment);
		if (current_segment < profile_.getNumSegments()) {
			Trajectory.Segment segment = profile_.getSegment(current_segment);
			double error = segment.pos - dist;
			double angError = segment.heading - heading;
			if (angError > 180) {
				angError = angError - 360;
			} else if (angError < -180) {
				angError = angError + 360;
			}
			double velError = segment.vel - vel;
			double desired = (angError * kAng_) + segment.vel;
			double output = desired + (kp_ * error) + (ka_ * segment.acc);

			last_error_ = error;
			last_Ang_error = angError;
			current_heading = segment.heading;
			return new TrajectoryStatus(segment, error, velError,
					angError, output);
		} else {
			return TrajectoryStatus.NEUTRAL;
		}
	}

	public double getHeading() {
		return current_heading;
	}

	public boolean isFinishedTrajectory() {
		return current_segment >= profile_.getNumSegments();
	}

	private double customRound(double num) {
		return Math.round(num * 200) / 200.0;
	}

	public double getLastError() {
		return last_error_;
	}

	public boolean onTarget() {
		return last_error_ < _DistTol && last_Ang_error < _AngTol;
	}

	private Trajectory.Segment interpolateSegments(int currentSeg, double time) {
		if (currentSeg == 0) {
			return profile_.getSegment(currentSeg);
		}
		Trajectory.Segment firstSeg = profile_.getSegment(currentSeg - 1);
		Trajectory.Segment lastSeg = profile_.getSegment(currentSeg);
		double pos, vel, acc, jerk, heading, dt, x, y;
		double firstTime = firstSeg.dt * (currentSeg - 1);
		double lastTime = lastSeg.dt * (currentSeg);
		pos = (((time - firstTime) * (lastSeg.pos - firstSeg.pos)) / (lastTime - firstTime))
				+ firstSeg.pos;
		vel = (((time - firstTime) * (lastSeg.vel - firstSeg.vel)) / (lastTime - firstTime))
				+ firstSeg.vel;
		acc = (((time - firstTime) * (lastSeg.acc - firstSeg.acc)) / (lastTime - firstTime))
				+ firstSeg.acc;
		jerk = (((time - firstTime) * (lastSeg.jerk - firstSeg.jerk)) / (lastTime - firstTime))
				+ firstSeg.jerk;
		heading = (((time - firstTime) * (lastSeg.heading - firstSeg.heading)) / (lastTime - firstTime))
				+ firstSeg.heading;
		dt = firstSeg.dt;
		x = (((time - firstTime) * (lastSeg.x - firstSeg.x)) / (lastTime - firstTime)) + firstSeg.x;
		y = (((time - firstTime) * (lastSeg.y - firstSeg.y)) / (lastTime - firstTime)) + firstSeg.y;
		System.out.println(new Trajectory.Segment(pos, vel, acc, jerk, heading, dt, x, y).toString());
		return new Trajectory.Segment(pos, vel, acc, jerk, heading, dt, x, y);
	}


}
