package org.usfirst.frc.team2869.robot.util.drivers;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;

public class MkGyro extends AHRS {

    public MkGyro(SPI.Port spi_port_id) {
        super(spi_port_id);
    }

    public double getFullYaw() {
        return getYaw() > 0 ? getYaw() - 360 : getYaw();
    }
}
