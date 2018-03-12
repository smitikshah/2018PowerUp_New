package org.usfirst.frc.team2869.robot;

import org.usfirst.frc.team2869.robot.util.other.MatchData;

public class RobotState {

    public static SystemState mSystemState = SystemState.IDLE;
    public static MatchState mMatchState = MatchState.DISABLED;
    public static DriveControlState mDriveControlState = DriveControlState.VELOCITY_SETPOINT;
    public static ArmControlState mArmControlState = ArmControlState.MOTION_MAGIC;
    public static ArmState mArmState = ArmState.ZEROED;
    public static MatchData matchData = MatchData.defaultMatch;


    // Intenal state of the system
    public enum SystemState {
        IDLE
    }

    public enum MatchState {
        AUTO, TELEOP, DISABLED
    }

    public enum DriveControlState {
        OPEN_LOOP, // open loop voltage control
        VELOCITY_SETPOINT, // velocity PID control
        PATH_FOLLOWING, // used for autonomous driving
    }

    public enum ArmControlState {
        ZEROING,
        MOTION_MAGIC,
        OPEN_LOOP,
        PIDF
    }

    public enum ArmState {
        ENABLE(0),
        ZEROED(0),
        SWITCH_PLACE(40);

        public final double state;

        ArmState(final double state) {
            this.state = state;
        }
    }

}
