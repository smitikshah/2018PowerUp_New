package org.usfirst.frc.team2869.robot.commands.auto.actions;

import org.usfirst.frc.team2869.robot.util.other.SynchronousPIDF;
import org.usfirst.frc.team2869.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2869.robot.util.other.DriveSignal;


public class TurnInPlaceAction implements Action {

    double Dt;
    private SynchronousPIDF pid;

    public TurnInPlaceAction() {
        pid = new SynchronousPIDF(0.0025, 0, 0.0025 * 50.0);
        pid.setContinuous(true);
        pid.setInputRange(-180, 180);
        pid.setOutputRange(-1, 1);
        pid.setSetpoint(90);
        Dt = System.nanoTime();
    }

    @Override
    public boolean isFinished() {
        //	return Drive.getInstance().isPathFinished();
        return pid.onTarget(1);
    }

    @Override
    public void update() {
        double out = pid.calculate(DriveTrain.getInstance().getYaw(), (System.nanoTime() - Dt) * 1e9);
        DriveTrain.getInstance().setOpenLoop(new DriveSignal(-out, out));
        Dt = System.nanoTime();
    }

    @Override
    public void done() {

    }

    @Override
    public void start() {

        //	RobotState.mDriveControlState = DriveControlState.TURN_IN_PLACE;
//		Drive.getInstance().setDrivePath(path, DRIVE.PATH_DIST_TOL, DRIVE.PATH_ANGLE_TOL);
    }
}
