/* Created Mon Nov 28 16:30:42 EST 2016 */
package com.team5459.robot;

import org.strongback.Strongback;
import org.strongback.components.Motor;
import org.strongback.components.Solenoid;
import org.strongback.hardware.Hardware;
import org.strongback.components.ui.FlightStick;
import edu.wpi.first.wpilibj.IterativeRobot;
import org.strongback.SwitchReactor;
/**
 * This is the main code for protobot
 * @author filip
 */
public class Robot extends IterativeRobot {
    private Motor topRight;
    private Motor frontRight;
    private Motor backRight;
    private Motor topLeft;
    private Motor frontLeft;
    private Motor backLeft;
    private Solenoid shift;
    private GearboxThree right;
    private GearboxThree left;
    private TankDrive drive;
    private FlightStick rightStick;
    private FlightStick leftStick;
    private SwitchReactor reactor;
    
    @Override
    public void robotInit() {
         topRight = Hardware.Motors.talonSRX(0);
        frontRight = Hardware.Motors.talonSRX(1);
        backRight = Hardware.Motors.talonSRX(2);
        topLeft = Hardware.Motors.talonSRX(3);
        frontLeft = Hardware.Motors.talonSRX(4);
        backLeft = Hardware.Motors.talonSRX(5);
        /*
        * do not revers any motors because it is reversed 
        */
        shift = Hardware.Solenoids.doubleSolenoid(0, 1, Solenoid.Direction.RETRACTING);
        
        //topRight.invert();
        //right = Motor.compose(topRight, frontRight,backLeft);
        //drive = new TankDrive(right, left);
        //Do this for a replacement for the gearboxes^
        left = new GearboxThree(topLeft, frontLeft, backLeft, true);
        right = new GearboxThree(topRight, frontRight, backRight, false);
        rightStick = Hardware.HumanInterfaceDevices.logitechAttack3D(1);
        leftStick = Hardware.HumanInterfaceDevices.logitechAttack3D(0);
        reactor = Strongback.switchReactor();
    }
    
    @Override
    public void autonomousInit() {
    	Strongback.start();
    	//this starts the schedule
    }
    
    @Override
    public void autonomousPeriodic(){
    	
    	/*
    	 * This runs a command group
    	 */
    }
    
    @Override
    public void teleopInit() {
        Strongback.submit(new TankDrive(right, left, rightStick, leftStick));
        
    }

    @Override
    public void teleopPeriodic() {
        reactor.whileTriggered(rightStick.getTrigger(), () -> Strongback.submit(new ShiftUp(shift)));
        reactor.onTriggered(rightStick.getThumb(), () -> Strongback.submit(new ShiftDown(shift)));
    }

    @Override
    public void disabledInit() {
        // Tell Strongback that the robot is disabled so it can flush and kill commands.
        Strongback.disable();
    }

}
