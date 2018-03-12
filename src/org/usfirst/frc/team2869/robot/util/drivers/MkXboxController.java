package org.usfirst.frc.team2869.robot.util.drivers;

import edu.wpi.first.wpilibj.XboxController;

import java.util.HashMap;

public class MkXboxController extends XboxController {

    public static final int LEFT_XAXIS = 0;
    public static final int LEFT_YAXIS = 1;
    public static final int RIGHT_XAXIS = 4;
    public static final int RIGHT_YAXIS = 5;
    public static final int LEFT_TRIGGER = 2;
    public static final int RIGHT_TRIGGER = 3;
    //Buttons
    public static final int ABUTTON = 1;
    public static final int BBUTTON = 2;
    public static final int XBUTTON = 3;
    public static final int YBUTTON = 4;
    public static final int LEFT_BUMPER = 5;
    public static final int RIGHT_BUMPER = 6;
    public static final int BACK_BUTTON = 7;
    public static final int START_BUTTON = 8;

    private final HashMap<Integer, MkXboxControllerButton> buttons;

    /**
     * Create a new MkJoystick.
     */
    public MkXboxController(final int port) {
        super(port);

        buttons = new HashMap<Integer, MkXboxControllerButton>();
    }

    /**
     * Gets a button of the joystick. Creates a new Button object if one did not already exist.
     *
     * @param button The raw button number of the button to get
     * @return The button
     */
    public MkXboxControllerButton getButton(final int button, final String name) {
        if (!buttons.containsKey(button)) {
            buttons.put(button, new MkXboxControllerButton(this, button, name));
        }
        return buttons.get(button);
    }

}
