package org.usfirst.frc.team2869.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.AnalogInput;
import org.usfirst.frc.team2869.robot.Constants;
import org.usfirst.frc.team2869.robot.util.other.Loop;
import org.usfirst.frc.team2869.robot.util.other.Looper;
import org.usfirst.frc.team2869.robot.util.other.Subsystem;

public class Claw extends Subsystem {
    private final VictorSPX leftIntakeRollerTalon;
    private final VictorSPX rightIntakeRollerTalon;
    private final AnalogInput pressureSensor;

    private Claw() {
        leftIntakeRollerTalon = new VictorSPX(Constants.ARM.ARM_LEFT_INTAKE_ROLLER_ID);
        rightIntakeRollerTalon = new VictorSPX(Constants.ARM.ARM_Right_INTAKE_ROLLER_ID);

        leftIntakeRollerTalon.setInverted(Constants.ARM.LEFT_INTAKE_ROLLER_INVERT);
        rightIntakeRollerTalon.setInverted(Constants.ARM.RIGHT_INTAKE_ROLLER_INVERT);
        leftIntakeRollerTalon.setNeutralMode(NeutralMode.Brake);
        rightIntakeRollerTalon.setNeutralMode(NeutralMode.Brake);
        pressureSensor = new AnalogInput(0);
    }

    public static Claw getInstance() {
        return InstanceHolder.mInstance;
    }

    @Override
    public void outputToSmartDashboard() {

    }

    @Override
    public void registerEnabledLoops(Looper enabledLooper) {
        Loop mLoop = new Loop() {

            @Override
            public void onStart(double timestamp) {
                synchronized (Claw.this) {

                }
            }

            /**
             * Updated from mEnabledLoop in Robot.java
             * Controls drivetrain during Path Following and Turn In Place and logs
             * Drivetrain data in all modes
             * @param timestamp In Seconds Since Code Start
             */
            @Override
            public void onLoop(double timestamp) {
                synchronized (Claw.this) {

                }
            }

            @Override
            public void onStop(double timestamp) {

            }
        };
        enabledLooper.register(mLoop);
    }

    @Override
    public void checkSystem() {

    }

    public void setIntakeRollers(double output) {
        leftIntakeRollerTalon.set(ControlMode.PercentOutput, output);
    }


    private static class InstanceHolder {

        private static final Claw mInstance = new Claw();
    }
}
