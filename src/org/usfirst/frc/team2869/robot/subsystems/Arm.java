package org.usfirst.frc.team2869.robot.subsystems;

import org.usfirst.frc.team2869.robot.Constants;
import org.usfirst.frc.team2869.robot.Constants.ARM;
import org.usfirst.frc.team2869.robot.Loop;
import org.usfirst.frc.team2869.robot.Looper;
import org.usfirst.frc.team2869.robot.MkMath;
import org.usfirst.frc.team2869.robot.Subsystem;
import org.usfirst.frc.team2869.robot.commands.SimPID;
import org.usfirst.frc.team2869.robot.subsystems.RobotState.ArmControlState;
import org.usfirst.frc.team2869.robot.subsystems.RobotState.ArmState;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Arm extends Subsystem {

    public final TalonSRX armTalon;
    public final VictorSPX leftIntakeRollerTalon;
    public final VictorSPX rightIntakeRollerTalon;
    public final Encoder armEncoder;
    private double setpoint = 0;
    private double maxVel = 0;
    private double gearRatio = 0;
    private double testMaxVel = 0;
    
    private SimPID armPID;

    public Arm() {
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

        leftIntakeRollerTalon = new VictorSPX(Constants.ARM.ARM_LEFT_INTAKE_ROLLER_ID);
        rightIntakeRollerTalon = new VictorSPX(Constants.ARM.ARM_Right_INTAKE_ROLLER_ID);
        //rightIntakeRollerTalon.set(ControlMode.Follower, Constants.ARM.ARM_LEFT_INTAKE_ROLLER_ID);
        //leftIntakeRollerTalon.setInverted(true);
        leftIntakeRollerTalon.setNeutralMode(NeutralMode.Brake);
        rightIntakeRollerTalon.setNeutralMode(NeutralMode.Brake);
        
        armEncoder = new Encoder(Constants.ARM.ARM_ENCODER_PORT_A, Constants.ARM.ARM_ENCODER_PORT_B);
        armEncoder.setDistancePerPulse(Constants.ARM.DEGREES_PER_PULSE);
        
        armPID = new SimPID(Constants.ARM.ARM_P, Constants.ARM.ARM_I, Constants.ARM.ARM_D, Constants.ARM.ARM_EBSILON);
        armPID.setMaxOutput(Constants.ARM.MAX_OUTPUT);
    }

    public static Arm getInstance() {
        return InstanceHolder.mInstance;
    }

    @Override
    public void writeToLog() {

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
    public void stop() {
        setpoint = 0;
    }

    @Override
    public void zeroSensors() {
        armTalon.setSelectedSensorPosition(0, Constants.kPIDLoopIdx, Constants.kTimeoutMs);
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
                        case PIDF:
                        	setArmAngle(0);
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
                stop();
            }
        };
        enabledLooper.register(mLoop);
    }
    
    private double calcHoldPosPower(double armAngle){
    	double gravityMoment = Constants.ARM.COM_DIST * MkMath.cos(armAngle) * Constants.ARM.ARM_WEIGHT;
    	double requiredMotorPower = gravityMoment/(3.8 * 550.0);
    	return requiredMotorPower;
    }
    
    public void setArmAngle(double angle){
    	armPID.setDesiredValue(angle);
    	//armPID.setDesiredValue(RobotState.mArmState.state);
		double currentArmAngle = armEncoder.get();
		double output = armPID.calcPID(currentArmAngle) + calcHoldPosPower(currentArmAngle);
		armTalon.set(ControlMode.PercentOutput, output);
		System.out.println(currentArmAngle);
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

    public void setIntakeRollers(double output) {
        leftIntakeRollerTalon.set(ControlMode.PercentOutput, output);
    }

    private static class InstanceHolder {

        private static final Arm mInstance = new Arm();
    }
}