package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import java.util.Arrays;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="Mecanum_TeleOp", group="TeleOp")
public class Teleop_Mecanum<opModeIsActive> extends LinearOpMode {

    //this assigns variables for the slide limit and finds the position of it
    double slideCountsPerInch = 2240; //ticks per one rotation of the motor for a rev 40:1 hd hex motor
    double hangingLimit = 9.0; //distance in inches the slide can go up at max

    private static DcMotor front_left, back_left, front_right, back_right;
    //this give variables for slow modes
    private double slowSpeed = 0.25;
    private com.qualcomm.robotcore.util.Range Range;

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
        //this actually stops the slides when they reach the limit
        slide_right.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
        slide_left.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
        slide_right.setDirection(DcMotorSimple.Direction.REVERSE);
        slide_left.setDirection(DcMotorSimple.Direction.FORWARD);
        slide_right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide_right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //this declares the servos
        Servo rightFoundation = hardwareMap.servo.get("rightFoundation");
        Servo leftFoundation = hardwareMap.servo.get("leftFoundation");
        Servo intakeGrabber = hardwareMap.servo.get("intakeGrabber");
        Servo intakeArm = hardwareMap.servo.get("intakeArm");

        //this is after you hit start
        waitForStart();
        while (opModeIsActive()) {

            double slidePow = -gamepad2.left_stick_y;
            if (slide_right.getCurrentPosition() >= hangingLimit * slideCountsPerInch) {
                slidePow = Math.min(slidePow, 0);
            } else if (slide_right.getCurrentPosition() <= 0) {
                slidePow = Math.max(slidePow, 0);
            }
            slide_left.setPower(slidePow);
            slide_right.setPower(slidePow);

        //this assigns buttons to all of the servos
        {

            if (gamepad1.a) {
                leftFoundation.setPosition(0);
                rightFoundation.setPosition(1);
            }
            if (gamepad1.x) {
                rightFoundation.setPosition(0);
                leftFoundation.setPosition(1);
            }
            if (gamepad2.a) {
                intakeArm.setPosition(0);
            }
            if (gamepad2.x) {
                intakeGrabber.setPosition(0);
            }
            if (gamepad2.y) {
                intakeArm.setPosition(0.5);
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
            //if the left bumper is being pressed, robot drives slowly
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
