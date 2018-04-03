package org.usfirst.frc.team2869.robot;

/**
 * UNLESS OTHERWISE NOTED BY RAW/NATIVE/RPM,
 * ALL POSITION UNITS ARE IN INCHES and DEGREES
 * ALL VELOCITY UNITS ARE IN INCHES PER SECOND and DEGREES PER SECOND
 * DIST DENOTES POSITION AND ANG DENOTES ANGLE
 * ID TYPICALLY DENOTES A CAN ID
 * ALL PID CONSTANTS SENT TO THE TALON ARE IN NATIVE UNITS
 */
public final class Constants {

    public static final int kSlotIdx = 0;
    public static final int kPIDLoopIdx = 0;
    public static final int kTimeoutMs = 10;
    public static final double kLooperDt = 0.005;
    public static final double PI = 3.14159265359;

    public static class DRIVE {
        public static final int LEFT_MASTER_ID = 1;
        public static final int LEFT_SLAVE_ID = 5;
        public static final int RIGHT_MASTER_ID = 3;
        public static final int RIGHT_SLAVE_ID = 2;

        public static final boolean LEFT_MASTER_INVERT = false;
        public static final boolean LEFT_SLAVE_INVERT = false;
        public static final boolean RIGHT_MASTER_INVERT = true;
        public static final boolean RIGHT_SLAVE_INVERT = true;

        public static final boolean LEFT_INVERT_SENSOR = true;
        public static final boolean RIGHT_INVERT_SENSOR = true;

        public static final double CODES_PER_REV = 4096.0;
        public static final double WHEEL_DIAMETER = 5.98;
        public static final double CIRCUMFERENCE = WHEEL_DIAMETER * PI;
        public static final double TURN_IN_PLACE_CIRCUMFERENCE = 104.1;

        public static final double MIN_TEST_POS = 500;
        public static final double MIN_TEST_VEL = 100;

        public static final double PATH_DIST_TOL = 0.25;
        public static final double PATH_ANGLE_TOL = 0.25;

        public static final double DRIVE_FOLLOWER_P = 0;
        public static final double DRIVE_FOLLOWER_A = 0;
        public static final double DRIVE_FOLLOWER_ANG = 0;

        public static final double LEFT_RPM_MAX = 500.0; //Observed Max Speed for Drivetrain in RPM
        public static final double RIGHT_RPM_MAX = 500.0; //Observed Max Speed for Drivetrain in RPM

        public static final double MAX_VEL =
                (LEFT_RPM_MAX / 60) * (CIRCUMFERENCE); // Max Speed in Inches per second
        public static final double DRIVE_P = 2 * (0.1 * 1023.0) / (700);
        public static final double DRIVE_I = DRIVE_P / 100.0;
        public static final double DRIVE_D = 5 * DRIVE_P;
        public static final double LEFT_DRIVE_F = (1023.0 / ((LEFT_RPM_MAX / 60.0 / 10.0)
                * 4096.0)); //Feedforwrd Term for Drivetrain using MAX Motor Units / Max Speed in Native Units Per 100ms
        public static final double RIGHT_DRIVE_F = (1023.0 / ((RIGHT_RPM_MAX / 60.0 / 10.0)
                * 4096.0)); //Feedforwrd Term for Drivetrain using MAX Motor Units / Max Speed in Native Units Per 100ms
        public static final double PATH_WHEELBASE = 33.75;
        public static final double OpenLoopFollower = -0.075;
    }

    public static class ARM {

        public static final int ARM_MASTER_TALON_ID = 6;
        public static final int ARM_LEFT_INTAKE_ROLLER_ID = 10;
        public static final int ARM_Right_INTAKE_ROLLER_ID = 11;
        public static final double RPM_MAX = 20.75; //RPM Max of Arm
        public static final double GEAR_RATIO = 1.0;
        public static final double MAX_RAW_VEL =
                ((RPM_MAX / 60.0 / 10.0) * 4096.0) / GEAR_RATIO; // Degrees per second
        public static final double ARM_P = 25 * ((0.1 * 1023.0) / (318)); //7.5 deg or 1390 units
        public static final double ARM_I = 0;//ARM_P / 100.0;
        public static final double ARM_D = ARM_P * 75;
        public static final double ARM_F = (1023.0 / MAX_RAW_VEL);

        public static final double MOTION_MAGIC_CRUISE_VEL = MAX_RAW_VEL * 0.975;
        public static final double MOTION_MAGIC_ACCEL = MAX_RAW_VEL * 10;

        public static final double INTAKE_IN_ROLLER_SPEED = 1;
        public static final double INTAKE_OUT_ROLLER_SPEED = -.5;
        public static final double AUTO_INTAKE_OUT_ROLLER_SPEED = -.65;
        public static final boolean LEFT_INTAKE_ROLLER_INVERT = true;
        public static final boolean RIGHT_INTAKE_ROLLER_INVERT = false;

        public static final int ARM_ZERO_POS = 538;
    }

    public static class AUTO {
        public static final String pathPath = "/home/lvuser/paths/";
        public static final String[] autoNames = {"DriveStraightL", "CS-1L", "CS-1R", "CS-2L", "CS-2R", "CS-3L", "CS-3R", "CS-4L", "CS-4R", "CS-5L", "CS-5R"};
    }

    public static class INPUT {

        public static final double OPERATOR_DEADBAND = 0.1;
        public static final double kThrottleDeadband = 0.0;
        public static final double kWheelDeadband = 0.0045;
    }


}
