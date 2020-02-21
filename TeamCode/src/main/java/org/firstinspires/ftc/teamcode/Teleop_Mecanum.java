/*
ADB guide can be found at:
https://ftcprogramming.wordpress.com/2015/11/30/building-ftc_app-wirelessly/
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServoImpl;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.Arrays;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="Mecanum_TeleOp", group="TeleOp")
public class Teleop_Mecanum<opModeIsActive> extends LinearOpMode {

    private static DcMotor front_left, back_left, front_right, back_right;
    private double slowSpeed = 0.25;

    @Override
    //this is the init loop
    public void runOpMode() {
        //this declares the motors
        //chassis motors
        front_left = hardwareMap.dcMotor.get(UniversalConstants.LEFT1NAME);
        back_left = hardwareMap.dcMotor.get(UniversalConstants.LEFT2NAME);
        front_right = hardwareMap.dcMotor.get(UniversalConstants.RIGHT1NAME);
        back_right = hardwareMap.dcMotor.get(UniversalConstants.RIGHT2NAME);
        front_right.setDirection(DcMotorSimple.Direction.FORWARD);
        front_left.setDirection(DcMotorSimple.Direction.REVERSE);
        back_right.setDirection(DcMotorSimple.Direction.FORWARD);
        back_left.setDirection(DcMotorSimple.Direction.REVERSE);
        //slide motors
        DcMotor slide_left = hardwareMap.get(DcMotor.class, "slide_left");
        DcMotor slide_right = hardwareMap.get(DcMotor.class, "slide_right");
        slide_right.setDirection(DcMotorSimple.Direction.REVERSE);
        slide_left.setDirection(DcMotorSimple.Direction.FORWARD);

        //this declares the servos
        Servo rightFoundation = hardwareMap.servo.get("rightFoundation");
        Servo leftFoundation = hardwareMap.servo.get("leftFoundation");
        Servo intakeArm = hardwareMap.servo.get("intakeArm");
        Servo intakeGrabber = hardwareMap.servo.get("intakeGrabber");

            //this actually stops the slides when they reach the limit
            slide_right.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
            slide_left.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);

        //this is after you hit start
        waitForStart();
        while (opModeIsActive()) {

            //this assigns joysticks for the slides
            double leftslide = gamepad2.left_stick_y;
            double rightslide = gamepad2.left_stick_y;
            slide_left.setPower(leftslide);
            slide_right.setPower(rightslide);

            //this assigns variables for the slide limit and finds the position of it
            double slideCountsPerInch = 2240; //ticks per one rotation of the motor for a rev 40:1 hd hex motor
            double slidePulleyDiameter = 0.1968503937007874015748031496063;       //diameter in inches of the spool/pulley that has string on it
            double finalGearRatio = 1;          //Gear ratio between motor and final output axle (if no gear ratio, just set equal to 1)
            double ticksPerHangingRev = slideCountsPerInch * finalGearRatio;  //Calculates the ticks per rotaion of the OUTPUT AXLE, not the motor.  If gear ratio is 1:1, this will be the same as hangingMotorCountsPerInch
            double ticksPerHangingInch = (ticksPerHangingRev / (slidePulleyDiameter * 3.14159265)); //Calculates how many ticks of the motor's output axle it takes to make the slide go up 1 inch
            double hangingLimit = 6;
            if (slide_right.getCurrentPosition() <= hangingLimit * ticksPerHangingInch && slide_right.getCurrentPosition() >= 0)
                if (slide_left.getCurrentPosition() <= hangingLimit * ticksPerHangingInch && slide_left.getCurrentPosition() >= 0)

                //this assigns buttons to all of the servos
                {
                    
                    if (gamepad1.a) {
                        leftFoundation.setPosition(0);
                        rightFoundation.setPosition(1);
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
                }

            //assigns sticks for driving
            arcadeMecanum(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        }
    }
        public void arcadeMecanum ( double drive, double strafe, double turn){
            //if the left bumper is being pressed, use slowmode
            if (gamepad1.left_bumper) {
                strafe *= slowSpeed;
                drive *= slowSpeed;
                turn *= slowSpeed;
            }

            //this allows the robot to turn, strafe, and drive
            double leftFront = -drive - strafe - turn;
            double rightFront = -drive + strafe + turn;
            double leftBack = -drive + strafe - turn;
            double rightBack = -drive - strafe + turn;

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
