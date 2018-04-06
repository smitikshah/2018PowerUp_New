package org.usfirst.frc.team2869.robot.auto.actions;

import edu.wpi.first.wpilibj.Timer;
import org.usfirst.frc.team2869.robot.subsystems.Arm;
import org.usfirst.frc.team2869.robot.util.auto.Action;

public class RollerAction implements Action {

    private double time;
    private double speed;
    private Timer timer;
    private boolean end;

    public RollerAction(double time, double speed) {
        this(time, speed, false);
    }

    public RollerAction(double time, double speed, boolean end) {
        this.speed = speed;
        this.time = time;
        timer = new Timer();
        this.end = end;
    }


    @Override
    public boolean isFinished() {
        return timer.get() >= time;
    }

    @Override
    public void update() {
        Arm.getInstance().setIntakeRollers(speed);
    }

    @Override
    public void done() {
        if (end) {
            Arm.getInstance().setIntakeRollers(0.1 * 1.5);
        } else {
            Arm.getInstance().setIntakeRollers(0);
        }

    }

    @Override
    public void start() {
        timer.start();
    }
}