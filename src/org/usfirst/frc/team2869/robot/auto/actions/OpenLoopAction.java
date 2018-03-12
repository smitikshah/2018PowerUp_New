package org.usfirst.frc.team2869.robot.auto.actions;

import edu.wpi.first.wpilibj.Timer;
import org.usfirst.frc.team2869.robot.RobotState;
import org.usfirst.frc.team2869.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2869.robot.util.auto.Action;
import org.usfirst.frc.team2869.robot.util.other.DriveSignal;

public class OpenLoopAction implements Action {

    private double time;
    private double speed;
    private Timer timer;
    private boolean stop;

    public OpenLoopAction(double time, double speed, boolean stop) {
        this.speed = speed;
        this.time = time;
        timer = new Timer();
        this.stop = stop;
    }

    /**
     * Returns whether or not the code has finished execution. When implementing this interface, this method is used by
     * the runAction method every cycle to know when to stop running the action
     *
     * @return boolean
     */
    @Override
    public boolean isFinished() {
        return timer.get() >= time;
    }

    /**
     * Called by runAction in AutoModeBase iteratively until isFinished returns true. Iterative logic lives in this
     * method
     */
    @Override
    public void update() {
        DriveTrain.getInstance().setOpenLoop(new DriveSignal(speed, speed));
    }

    /**
     * Run code once when the action finishes, usually for clean up
     */
    @Override
    public void done() {
        if (stop) {
            DriveTrain.getInstance().setOpenLoop(DriveSignal.BRAKE);
        }

    }

    /**
     * Run code once when the action is started, for set up
     */
    @Override
    public void start() {
        timer.start();
        RobotState.mDriveControlState = RobotState.DriveControlState.OPEN_LOOP;
    }


}
