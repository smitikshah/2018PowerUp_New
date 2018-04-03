package org.usfirst.frc.team2869.robot;

import org.usfirst.frc.team2869.robot.util.other.MatchData;

public class RobotState {


    public static MatchState mMatchState = MatchState.DISABLED;
    public static DriveControlState mDriveControlState = DriveControlState.VELOCITY_SETPOINT;
    public static ArmControlState mArmControlState = ArmControlState.MOTION_MAGIC;
    public static ArmState mArmState = ArmState.ENABLE;
    public static MatchData matchData = MatchData.defaultMatch;

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
        OPEN_LOOP
    }

    public enum ArmState {
        ENABLE(-10),
        INTAKE(15),
        SECOND_INTAKE(42),
        SWITCH_PLACE(101),
        BACK_SWITCH_PLACE(180);

        public final double state;

        ArmState(final double state) {
            this.state = state;
        }
    }

}
