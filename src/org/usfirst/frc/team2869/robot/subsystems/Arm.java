package org.usfirst.frc.team2869.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team2869.robot.Constants;
import org.usfirst.frc.team2869.robot.Constants.ARM;
import org.usfirst.frc.team2869.robot.RobotState;
import org.usfirst.frc.team2869.robot.RobotState.ArmControlState;
import org.usfirst.frc.team2869.robot.RobotState.ArmState;
import org.usfirst.frc.team2869.robot.util.drivers.MkMath;
import org.usfirst.frc.team2869.robot.util.other.Loop;
import org.usfirst.frc.team2869.robot.util.other.Looper;
import org.usfirst.frc.team2869.robot.util.other.Subsystem;


public class Arm extends Subsystem {

    private final TalonSRX armTalon;

    private double setpoint = 0;
    private double maxVel = 0;
    private double gearRatio = 0;
    private double testMaxVel = 0;

    private Arm() {
        armTalon = new TalonSRX(ARM.ARM_MASTER_TALON_ID);
        armTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.kPIDLoopIdx, Constants.kTimeoutMs);
        armTalon.setSensorPhase(true);
        armTalon.setInverted(false);
        /* set the peak and nominal outputs */
        armTalon.configNominalOutputForward(0, Constants.kTimeoutMs);
        armTalon.configNominalOutputReverse(0, Constants.kTimeoutMs);
        armTalon.configPeakOutputForward(1, Constants.kTimeoutMs);
        armTalon.configPeakOutputReverse(-1, Constants.kTimeoutMs);

        /* set closed loop gains in slot0 - see documentation */
        armTalon.selectProfileSlot(Constants.kSlotIdx, Constants.kPIDLoopIdx);
        armTalon.config_kF(0, 0.2, Constants.kTimeoutMs);
        armTalon.config_kP(0, 0.2, Constants.kTimeoutMs);
        armTalon.config_kI(0, 0, Constants.kTimeoutMs);
        armTalon.config_kD(0, 0, Constants.kTimeoutMs);
        /* set acceleration and vcruise velocity - see documentation */
        armTalon.configMotionCruiseVelocity(15000, Constants.kTimeoutMs);
        armTalon.configMotionAcceleration(6000, Constants.kTimeoutMs);
        /* zero the sensor */
        armTalon.setSelectedSensorPosition(0, Constants.kPIDLoopIdx, Constants.kTimeoutMs);
        armTalon.setNeutralMode(NeutralMode.Brake);
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
    }

    @Override
    public void checkSystem() {

    }

    @Override
    public void registerEnabledLoops(Looper enabledLooper) {
        Loop mLoop = new Loop() {

            @Override
            public void onStart(double timestamp) {
                synchronized (Arm.this) {

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
