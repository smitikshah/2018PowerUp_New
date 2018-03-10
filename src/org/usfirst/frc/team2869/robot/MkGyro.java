package org.usfirst.frc.team2869.robot;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.kauailabs.navx.frc.AHRS;
import com.kauailabs.navx.mxp.IMU;

public class MkGyro extends AHRS {

	public MkGyro(SPI.Port spi_port_id) {
		super(spi_port_id);
	}

	public double getFullYaw() {
		return getYaw() > 0 ? getYaw() - 360 : getYaw();
	}
}