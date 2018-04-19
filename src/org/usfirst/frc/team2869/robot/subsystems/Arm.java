package org.usfirst.frc.team2869.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team2869.robot.Constants;
import org.usfirst.frc.team2869.robot.Constants.ARM;
import org.usfirst.frc.team2869.robot.RobotState;
import org.usfirst.frc.team2869.robot.RobotState.ArmControlState;
import org.usfirst.frc.team2869.robot.RobotState.ArmState;
import org.usfirst.frc.team2869.robot.util.other.Loop;
import org.usfirst.frc.team2869.robot.util.other.Looper;
import org.usfirst.frc.team2869.robot.util.other.MkMath;
import org.usfirst.frc.team2869.robot.util.other.Subsystem;


public class Arm extends Subsystem {

    private final TalonSRX armTalon;
    private final VictorSPX leftIntakeRollerTalon;
    private final VictorSPX rightIntakeRollerTalon;
    private double setpoint = 0;
    private double armPosEnable = 0;
    private boolean disCon = false;
    private double startDis = 0;

    private Arm() {
        armTalon = new TalonSRX(ARM.ARM_MASTER_TALON_ID);
        armTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.kPIDLoopIdx, Constants.kTimeoutMs);
        armTalon.setSensorPhase(true);
        armTalon.setInverted(true);

        armTalon.configNominalOutputForward(0, Constants.kTimeoutMs);
        armTalon.configNominalOutputReverse(0, Constants.kTimeoutMs);
        armTalon.configPeakOutputForward(.9, Constants.kTimeoutMs);
        armTalon.configPeakOutputReverse(-.9, Constants.kTimeoutMs);

        armTalon.selectProfileSlot(Constants.kSlotIdx, Constants.kPIDLoopIdx);
        armTalon.config_kF(Constants.kSlotIdx, ARM.ARM_F, Constants.kTimeoutMs);
        armTalon.config_kP(Constants.kSlotIdx, ARM.ARM_P, Constants.kTimeoutMs);
        armTalon.config_kI(Constants.kSlotIdx, ARM.ARM_I, Constants.kTimeoutMs);
        armTalon.config_kD(Constants.kSlotIdx, ARM.ARM_D, Constants.kTimeoutMs);

        armTalon.configMotionCruiseVelocity((int) ARM.MOTION_MAGIC_CRUISE_VEL, Constants.kTimeoutMs);
        armTalon.configMotionAcceleration((int) ARM.MOTION_MAGIC_ACCEL, Constants.kTimeoutMs);
        armTalon.setNeutralMode(NeutralMode.Brake);

        leftIntakeRollerTalon = new VictorSPX(Constants.ARM.ARM_LEFT_INTAKE_ROLLER_ID);
        rightIntakeRollerTalon = new VictorSPX(Constants.ARM.ARM_Right_INTAKE_ROLLER_ID);

        leftIntakeRollerTalon.setInverted(Constants.ARM.LEFT_INTAKE_ROLLER_INVERT);
        rightIntakeRollerTalon.setInverted(Constants.ARM.RIGHT_INTAKE_ROLLER_INVERT);
        leftIntakeRollerTalon.setNeutralMode(NeutralMode.Brake);
        rightIntakeRollerTalon.setNeutralMode(NeutralMode.Brake);
        zeroAbsolute();
    }

    public static Arm getInstance() {
        return InstanceHolder.mInstance;
    }

    @Override
    public void outputToSmartDashboard() {
        SmartDashboard.putNumber("Arm Position", MkMath.nativeUnitsToAngle(armTalon.getSelectedSensorPosition(Constants.kPIDLoopIdx)));
        SmartDashboard.putNumber("Arm Velocity", MkMath.nativeUnitsToAngle(armTalon.getSelectedSensorVelocity(Constants.kPIDLoopIdx)));
        SmartDashboard.putNumber("Arm Setpoint", setpoint);
        SmartDashboard.putNumber("Arm Error", MkMath.nativeUnitsToAngle(armTalon.getClosedLoopError(Constants.kPIDLoopIdx)));
        SmartDashboard.putString("Arm Control Mode", RobotState.mArmControlState.toString());
        SmartDashboard.putNumber("Arm Angle", getPosition());
        SmartDashboard.putNumber("Arm Absolute", armTalon.getSensorCollection().getPulseWidthPosition());
        SmartDashboard.putNumber("Arm Output", armTalon.getMotorOutputPercent());
    }

    private double getRPM() {
        return ((armTalon.getSelectedSensorVelocity(0) * 60.0 * 10.0) / Constants.DRIVE.CODES_PER_REV)
                * ARM.GEAR_RATIO;
    }

    @Override
    public void checkSystem() {

    }

    public void setIntakeRollers(double output) {
        leftIntakeRollerTalon.set(ControlMode.PercentOutput, output);
        rightIntakeRollerTalon.set(ControlMode.PercentOutput, output);
    }

    @Override
    public void registerEnabledLoops(Looper enabledLooper) {
        Loop mLoop = new Loop() {

            @Override
            public void onStart(double timestamp) {
                synchronized (Arm.this) {
                    zeroAbsolute();
                    armPosEnable = getPosition();
                    RobotState.mArmState = ArmState.ENABLE;
                }
            }

            /**
             * Updated from mEnabledLoop in Robot.java
             * @param timestamp Time in seconds since code start
             */
            @Override
            public void onLoop(double timestamp) {
                synchronized (Arm.this) {
                    armSafetyCheck();
                    switch (RobotState.mArmControlState) {
                        case MOTION_MAGIC:
                            updateArmSetpoint();
                            return;
                        case ZEROING:
                            return;
                        case OPEN_LOOP:
                            return;
                        default:
                            System.out
                                    .println("Unexpected arm control state: " + RobotState.mArmControlState);
                            break;
                    }
                }
            }

            @Override
            public void onStop(double timestamp) {

            }
        };
        enabledLooper.register(mLoop);
    }

    private void updateArmSetpoint() {
        if (RobotState.mArmState.equals(ArmState.ENABLE)) {
            armTalon.set(ControlMode.MotionMagic, MkMath.angleToNativeUnits(armPosEnable));
        } else {
            armTalon.set(ControlMode.MotionMagic, MkMath.angleToNativeUnits(RobotState.mArmState.state));
        }
    }

    public void zeroAbsolute() {
        int pulseWidth = armTalon.getSensorCollection().getPulseWidthPosition();
        if (pulseWidth > 0) {
            pulseWidth = pulseWidth & 0xFFF;
        } else {
            pulseWidth += (-Math.round(((double) pulseWidth / 4096) - 0.50)) * 4096;
        }
        armTalon.setSelectedSensorPosition(pulseWidth + (-ARM.ARM_ZERO_POS), Constants.kPIDLoopIdx,
                Constants.kTimeoutMs);
    }

    private double getPosition() {
        return MkMath.nativeUnitsToAngle(armTalon.getSelectedSensorPosition(Constants.kPIDLoopIdx));
    }

    public boolean isEncoderConnected() {
        return armTalon.getSensorCollection().getPulseWidthRiseToRiseUs() > 100;
    }

    public void setEnable() {
        armPosEnable = getPosition();
        RobotState.mArmState = ArmState.ENABLE;
    }

    private void armSafetyCheck() {
        if (!isEncoderConnected()) {
            if (disCon) {
                if (Timer.getFPGATimestamp() - startDis > 0.5) {
                    RobotState.mArmControlState = ArmControlState.OPEN_LOOP;
                    disCon = false;
                    startDis = 0;
                }
            } else {
                disCon = true;
                startDis = Timer.getFPGATimestamp();
            }
            System.out.println("Arm Encoder Not Connected");
        } else {
            if (disCon) {
                disCon = false;
                startDis = 0;
                zeroAbsolute();
                Timer.delay(0.05);
                setEnable();
                RobotState.mArmControlState = ArmControlState.MOTION_MAGIC;
            }
        }
    }

    public void setOpenLoop(double output) {
        armTalon.set(ControlMode.PercentOutput, output);
        setpoint = output;
    }


    private static class InstanceHolder {

        private static final Arm mInstance = new Arm();
    }
}