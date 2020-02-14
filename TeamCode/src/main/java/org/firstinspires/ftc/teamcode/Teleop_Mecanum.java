/*
ADB guide can be found at:
https://ftcprogramming.wordpress.com/2015/11/30/building-ftc_app-wirelessly/
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServoImpl;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.Arrays;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="Mecanum_TeleOp", group="TeleOp")
public class Teleop_Mecanum<opModeIsActive> extends LinearOpMode {

    //This gives deadzones for the motors.
    private static final double TRIGGERTHRESHOLD = .2;
    private static final double ACCEPTINPUTTHRESHOLD = .15;

    //Emphasis on current controller reading (vs current motor power) on the drive train
    private static final double SCALEDPOWER = 1;

    private static DcMotor front_left, back_left, front_right, back_right;
    private double slowSpeed = 0.25;
    private double strafeSpeed = 1;
    private double driveSpeed = 1;

    @Override
    public void runOpMode() {
        //this declares the motors
        front_left = hardwareMap.dcMotor.get(UniversalConstants.LEFT1NAME);
        back_left = hardwareMap.dcMotor.get(UniversalConstants.LEFT2NAME);
        front_right = hardwareMap.dcMotor.get(UniversalConstants.RIGHT1NAME);
        back_right = hardwareMap.dcMotor.get(UniversalConstants.RIGHT2NAME);
        front_right.setDirection(DcMotorSimple.Direction.FORWARD);
        front_left.setDirection(DcMotorSimple.Direction.REVERSE);
        back_right.setDirection(DcMotorSimple.Direction.FORWARD);
        back_left.setDirection(DcMotorSimple.Direction.REVERSE);
        DcMotor slide_left = hardwareMap.get(DcMotor.class, "slide_left");
        DcMotor slide_right = hardwareMap.get(DcMotor.class, "slide_right");
        slide_right.setDirection(DcMotorSimple.Direction.REVERSE);
        slide_left.setDirection(DcMotorSimple.Direction.FORWARD);

        //this declares the servos
       Servo rightFoundation = hardwareMap.servo.get("rightFoundation");
       Servo leftFoundation = hardwareMap.servo.get("leftFoundation");
       Servo intakeArm = hardwareMap.servo.get("intakeArm");
       Servo intakeGrabber = hardwareMap.servo.get("intakeGrabber");
       
       waitForStart();
        //init loop
       while(opModeIsActive()) {

           //this assigns buttons to the slides
           double leftslide = gamepad2.left_stick_y;
           double rightslide = gamepad2.left_stick_y;
           slide_left.setPower(leftslide);
           slide_right.setPower(rightslide);

           //this assigns variables for the slide limit
           double hangingMotorCountsPerInch = 2240; //ticks per one rotation of the motor for a rev 40:1 hd hex motor
           double hangingPulleyDiameter = 0.1968503937007874015748031496063;       //diameter in inches of the spool/pulley that has string on it
           double hangingGearRatio = 60/40;          //Gear ratio between motor and final output axle (if no gear ratio, just set equal to 1)
           double ticksPerHangingRev = hangingMotorCountsPerInch * hangingGearRatio;  //Calculates the ticks per rotaion of the OUTPUT AXLE, not the motor.  If gear ratio is 1:1, this will be the same as hangingMotorCountsPerInch
           double ticksPerHangingInch =  (ticksPerHangingRev/(hangingPulleyDiameter * 3.14159265)); //Calculates how many ticks of the motor's output axle it takes to make the slide go up 1 inch

           double hangingLimit = 30; //amount of inches extension you want at the very top.  Recommend .25-.5 inches lower than actual full extension, just to be safe.

               //this assigns buttons to all of the servos
           if (gamepad1.a) {
               rightFoundation.setPosition(1);
               leftFoundation.setPosition(0);
           }
           if (gamepad1.x) {
               rightFoundation.setPosition(0.5);
               leftFoundation.setPosition(1);
           }
           if (gamepad2.a) {
               intakeArm.setPosition(0);
           }
           if (gamepad2.x) {
               intakeGrabber.setPosition(0);
           }
           if (gamepad2.y) {
               intakeArm.setPosition(1);
           }
           if (gamepad2.b) {
               intakeGrabber.setPosition(0.5);
           }
           //Use the larger trigger value to scale down the inputs.
           arcadeMecanum(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
       }
    }
    // y - forwards
    // x - side
    // c - rotation
    public void arcadeMecanum(double drive, double strafe, double turn) {
        //if the left bumper is being pressed, use slowmode
        if (gamepad1.left_bumper) {
            strafe *= slowSpeed;
            drive *= slowSpeed;
            turn *= slowSpeed;
        }

        //this allows the robot to turn, strafe, and drive
        double leftFront = -drive - strafe - turn;
        double rightFront = -drive + strafe + turn;
        double leftBack = -drive  + strafe - turn;
        double rightBack = -drive - strafe  + turn;

        double[] pows = {leftFront, rightFront, leftBack, rightBack};
        Arrays.sort(pows);
        if (pows[3] > 1) {
            leftFront /= pows[3];
            rightFront /= pows[3];
            leftBack /= pows[3];
            rightBack /= pows[3];
        }
        front_left.setPower(leftFront);
        front_right.setPower(rightFront);
        back_left.setPower(leftBack);
        back_right.setPower(rightBack);
    }
}