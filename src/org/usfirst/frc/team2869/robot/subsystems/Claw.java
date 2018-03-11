package org.usfirst.frc.team2869.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import org.usfirst.frc.team2869.robot.Constants;
import org.usfirst.frc.team2869.robot.util.other.Looper;
import org.usfirst.frc.team2869.robot.util.other.Subsystem;

public class Claw extends Subsystem {
    public final VictorSPX leftIntakeRollerTalon;
    public final VictorSPX rightIntakeRollerTalon;

    public Claw() {
        leftIntakeRollerTalon = new VictorSPX(Constants.ARM.ARM_LEFT_INTAKE_ROLLER_ID);
        rightIntakeRollerTalon = new VictorSPX(Constants.ARM.ARM_Right_INTAKE_ROLLER_ID);

        leftIntakeRollerTalon.setInverted(Constants.ARM.LEFT_INTAKE_ROLLER_INVERT);
        rightIntakeRollerTalon.setInverted(Constants.ARM.RIGHT_INTAKE_ROLLER_INVERT);
        leftIntakeRollerTalon.setNeutralMode(NeutralMode.Brake);
        rightIntakeRollerTalon.setNeutralMode(NeutralMode.Brake);
    }
    public static Claw getInstance() {
        return InstanceHolder.mInstance;
    }
    @Override
    public void outputToSmartDashboard() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void zeroSensors() {

    }

    @Override
    public void checkSystem() {

    }

    public void setIntakeRollers(double output) {
        leftIntakeRollerTalon.set(ControlMode.PercentOutput, output);
    }


    @Override
    public void registerEnabledLoops(Looper enabledLooper) {

    }
    private static class InstanceHolder {

        private static final Claw mInstance = new Claw();
    }
}
