package org.usfirst.frc.team2869.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team2869.robot.Constants;
import org.usfirst.frc.team2869.robot.Constants.ARM;
import org.usfirst.frc.team2869.robot.RobotState;
import org.usfirst.frc.team2869.robot.RobotState.ArmControlState;
import org.usfirst.frc.team2869.robot.RobotState.ArmState;
import org.usfirst.frc.team2869.robot.util.other.*;


public class Arm extends Subsystem {

    private final TalonSRX armTalon;
    private final VictorSPX leftIntakeRollerTalon;
    private final VictorSPX rightIntakeRollerTalon;
    private double setpoint = 0;
    private double maxVel = 0;
    private double gearRatio = 0;
    private double testMaxVel = 0;
    private double armPosEnable = 0;
    private SimPID armPID;

    private Arm() {
        armTalon = new TalonSRX(ARM.ARM_MASTER_TALON_ID);
        armTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.kPIDLoopIdx, Constants.kTimeoutMs);
        armTalon.setSensorPhase(true);
        armTalon.setInverted(false);

        armTalon.configNominalOutputForward(0, Constants.kTimeoutMs);
        armTalon.configNominalOutputReverse(0, Constants.kTimeoutMs);
        armTalon.configPeakOutputForward(0.15, Constants.kTimeoutMs);
        armTalon.configPeakOutputReverse(-0.15, Constants.kTimeoutMs);

        armTalon.selectProfileSlot(Constants.kSlotIdx, Constants.kPIDLoopIdx);
        armTalon.config_kF(Constants.kSlotIdx, ARM.ARM_F, Constants.kTimeoutMs);
        armTalon.config_kP(Constants.kSlotIdx, ARM.ARM_P, Constants.kTimeoutMs);
        armTalon.config_kI(Constants.kSlotIdx, ARM.ARM_I, Constants.kTimeoutMs);
        armTalon.config_kD(Constants.kSlotIdx, ARM.ARM_D, Constants.kTimeoutMs);

        armTalon.configMotionCruiseVelocity((int) ARM.MOTION_MAGIC_CRUISE_VEL, Constants.kTimeoutMs);
        armTalon.configMotionAcceleration((int) ARM.MOTION_MAGIC_ACCEL, Constants.kTimeoutMs);

        armTalon.setSelectedSensorPosition(0, Constants.kPIDLoopIdx, Constants.kTimeoutMs);
        armTalon.setNeutralMode(NeutralMode.Brake);

        leftIntakeRollerTalon = new VictorSPX(Constants.ARM.ARM_LEFT_INTAKE_ROLLER_ID);
        rightIntakeRollerTalon = new VictorSPX(Constants.ARM.ARM_Right_INTAKE_ROLLER_ID);

        leftIntakeRollerTalon.setInverted(Constants.ARM.LEFT_INTAKE_ROLLER_INVERT);
        rightIntakeRollerTalon.setInverted(Constants.ARM.RIGHT_INTAKE_ROLLER_INVERT);
        leftIntakeRollerTalon.setNeutralMode(NeutralMode.Brake);
        rightIntakeRollerTalon.setNeutralMode(NeutralMode.Brake);

        armPID = new SimPID(Constants.ARM.ARM_P, Constants.ARM.ARM_I, Constants.ARM.ARM_D, Constants.ARM.ARM_EBSILON);
        armPID.setMaxOutput(Constants.ARM.MAX_OUTPUT);
    }

    public static Arm getInstance() {
        return InstanceHolder.mInstance;
    }

    @Override
    public void outputToSmartDashboard() {
        if (Math.abs(armTalon.getSelectedSensorVelocity(Constants.kPIDLoopIdx)) > testMaxVel) {
            testMaxVel = Math.abs(armTalon.getSelectedSensorVelocity(Constants.kPIDLoopIdx));
        }
        SmartDashboard.putNumber("Arm Position", armTalon.getSelectedSensorPosition(Constants.kPIDLoopIdx));
        SmartDashboard.putNumber("Arm Velocity", armTalon.getSelectedSensorVelocity(Constants.kPIDLoopIdx));
        SmartDashboard.putNumber("Arm Setpoint", armTalon.getClosedLoopError(Constants.kPIDLoopIdx));
        SmartDashboard.putNumber("Arm Error", armTalon.getClosedLoopError(Constants.kPIDLoopIdx));
        SmartDashboard.putNumber("Arm Max Vel", testMaxVel);
        SmartDashboard.putString("Arm Control Mode", RobotState.mArmControlState.toString());
        SmartDashboard.putNumber("Arm Angle", getPosition());
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
                            zeroArm();
                            return;
                        case OPEN_LOOP:
                            return;
                        case PIDF:
                            setArmAngle(0);
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

    public void updateArmSetpoint() {
        armTalon.set(ControlMode.MotionMagic, MkMath.angleToNativeUnits(RobotState.mArmState.state));
        setpoint = RobotState.mArmState.state;
    }

    private void zeroArm() {
        if (armTalon.getOutputCurrent() > ARM.CURRENT_HARDSTOP_LIMIT) {
            RobotState.mArmControlState = ArmControlState.OPEN_LOOP;
            setOpenLoop(0);
            edu.wpi.first.wpilibj.Timer.delay(0.25);
            armTalon.setSelectedSensorPosition(0, Constants.kPIDLoopIdx, Constants.kTimeoutMs);
            RobotState.mArmState = ArmState.ZEROED;
            RobotState.mArmControlState = ArmControlState.MOTION_MAGIC;
            System.out.println(armTalon.getOutputCurrent());
        } else {
            setOpenLoop(ARM.ZEROING_POWER);
        }

    }

    private double getPosition() {
        return MkMath.nativeUnitsToAngle(armTalon.getSelectedSensorPosition(Constants.kPIDLoopIdx));
    }

    private double calcHoldPosPower(double armAngle) {
        double gravityMoment = Constants.ARM.COM_DIST * MkMath.cos(armAngle) * Constants.ARM.ARM_WEIGHT;
        double requiredMotorPower = gravityMoment / (3.8 * 550.0);
        return requiredMotorPower;
    }

    public void setArmAngle(double angle) {
        armPID.setDesiredValue(angle);
        //armPID.setDesiredValue(RobotState.mArmState.state);
        double currentArmAngle = getPosition();
        double output = armPID.calcPID(currentArmAngle) + calcHoldPosPower(currentArmAngle);
        armTalon.set(ControlMode.PercentOutput, output);
        System.out.println(currentArmAngle);
    }

    public void setEnable() {
        armPosEnable = getPosition();
        RobotState.mArmState = ArmState.ENABLE;
    }

    private void armSafetyCheck() {
        if (armTalon.getSensorCollection().getPulseWidthRiseToRiseUs() < 100) {
            RobotState.mArmControlState = ArmControlState.OPEN_LOOP;
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
