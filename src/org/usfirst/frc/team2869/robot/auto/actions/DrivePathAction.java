package org.usfirst.frc.team2869.robot.auto.actions;


import edu.wpi.first.wpilibj.DriverStation;
import org.usfirst.frc.team2869.robot.AutoChooser;
import org.usfirst.frc.team2869.robot.Constants;
import org.usfirst.frc.team2869.robot.RobotState;
import org.usfirst.frc.team2869.robot.auto.trajectory.Path;
import org.usfirst.frc.team2869.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2869.robot.util.auto.Action;

public class DrivePathAction implements Action {

    private final Path path;
    private boolean done;
    private boolean brakeMode;

    public DrivePathAction(Path path, boolean dir, boolean flip, boolean brakeMode) {
        this.path = path.copyPath();
        this.brakeMode = brakeMode;
        if (dir) {
            this.path.invert();
        }
        done = false;
    }

    public DrivePathAction(int pathNum, boolean dir, boolean brakeMode) {
        this(AutoChooser.autoPaths.get(
                "CS-" + Integer.toString(pathNum) + ((RobotState.matchData.switchPosition
                        == AutoChooser.GameObjectPosition.LEFT) ? "L" : "R") + ((RobotState.matchData.alliance
                        == DriverStation.Alliance.Blue) ? "B" : "R")), dir, false, brakeMode);
    }

    @Override
    public boolean isFinished() {
        if (done) {
            return true;
        }
        if (DriveTrain.getInstance().isPathFinished()) {
            done = true;
            return true;
        }
        return false;
    }

    @Override
    public void update() {

    }

    @Override
    public void done() {
        RobotState.mDriveControlState = RobotState.DriveControlState.VELOCITY_SETPOINT;
    }

    @Override
    public void start() {
        DriveTrain.getInstance().setDrivePath(path, Constants.DRIVE.PATH_DIST_TOL, Constants.DRIVE.PATH_ANGLE_TOL, brakeMode);
    }
}
