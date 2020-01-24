/*
ADB guide can be found at:
https://ftcprogramming.wordpress.com/2015/11/30/building-ftc_app-wirelessly/
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.Arrays;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="Mecanum_TeleOp", group="TeleOp")
public class Teleop_Mecanum<opModeIsActive> extends OpMode {
    //This gives deadzones for the motors.
    private static final double TRIGGERTHRESHOLD = .2;
    private static final double ACCEPTINPUTTHRESHOLD = .15;

    //Emphasis on current controller reading (vs current motor power) on the drive train
    private static final double SCALEDPOWER = 1;


    private static DcMotor front_left, back_left, front_right, back_right;

    @Override
    public void init() {


        //this gives motors names using universal constants(another class, look at the Java Class
        //called UniversalConstants for the code).
        front_left = hardwareMap.dcMotor.get(UniversalConstants.LEFT1NAME);
        back_left = hardwareMap.dcMotor.get(UniversalConstants.LEFT2NAME);
        front_right = hardwareMap.dcMotor.get(UniversalConstants.RIGHT1NAME);
        back_right = hardwareMap.dcMotor.get(UniversalConstants.RIGHT2NAME);

        //this allows the robot drive
        front_right.setDirection(DcMotorSimple.Direction.FORWARD);
        front_left.setDirection(DcMotorSimple.Direction.REVERSE);
        back_right.setDirection(DcMotorSimple.Direction.FORWARD);
        back_left.setDirection(DcMotorSimple.Direction.REVERSE);

        DcMotor slide_left = hardwareMap.get(DcMotor.class, "slide_left");
        DcMotor slide_right = hardwareMap.get(DcMotor.class, "slide_right");

        slide_right.setDirection(DcMotorSimple.Direction.REVERSE);
        slide_left.setDirection(DcMotorSimple.Direction.FORWARD);

        double leftslide = gamepad2.left_stick_y;
        double rightslide = gamepad2.left_stick_y;

        slide_left.setPower(leftslide);
        slide_right.setPower(rightslide);

    }


    @Override
    public void loop() {

        Servo rightFoundation = hardwareMap.servo.get("rightFoundation");
        Servo leftFoundation = hardwareMap.servo.get("leftFoundation");
        Servo intakeArm = hardwareMap.servo.get("intakeArm");
        Servo intakeGrabber = hardwareMap.servo.get("intakeGrabber");

        if (gamepad1.a) {
            rightFoundation.setPosition(1);
            leftFoundation.setPosition(0.5);
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
            intakeArm.setPosition(1);
        }
        if (gamepad2.b) {
            intakeGrabber.setPosition(0.5);
        }

        //This part assigns buttons/joysticks for driving
        double inputY = Math.abs(gamepad1.left_stick_y) > ACCEPTINPUTTHRESHOLD ? gamepad1.left_stick_y : 0;
        double inputX = Math.abs(gamepad1.left_stick_x) > ACCEPTINPUTTHRESHOLD ? -gamepad1.left_stick_x : 0;
        double inputC = Math.abs(gamepad1.right_stick_y) > ACCEPTINPUTTHRESHOLD ? -gamepad1.right_stick_y : 0;
        double BIGGERTRIGGER = gamepad1.left_trigger > gamepad1.right_trigger ? gamepad1.left_trigger : gamepad1.right_trigger;

        if (BIGGERTRIGGER > TRIGGERTHRESHOLD) { //If we have enough pressure on a trigger
            if ((Math.abs(inputY) > Math.abs(inputX)) && (Math.abs(inputY) > Math.abs(inputC))) { //If our forwards motion is the largest motion vector
                inputY /= 5 * BIGGERTRIGGER; //slow down our power inputs
                inputX /= 5 * BIGGERTRIGGER; //slow down our power inputs
                inputC /= 5 * BIGGERTRIGGER; //slow down our power inputs
            } else if ((Math.abs(inputC) > Math.abs(inputX)) && (Math.abs(inputC) > Math.abs(inputY))) { //and if our turing motion is the largest motion vector
                inputY /= 4 * BIGGERTRIGGER; //slow down our power inputs
                inputX /= 4 * BIGGERTRIGGER; //slow down our power inputs
                inputC /= 4 * BIGGERTRIGGER; //slow down our power inputs
            } else if ((Math.abs(inputX) > Math.abs(inputY)) && (Math.abs(inputX) > Math.abs(inputC))) { //and if our strafing motion is the largest motion vector
                inputY /= 3 * BIGGERTRIGGER; //slow down our power inputs
                inputX /= 3 * BIGGERTRIGGER; //slow down our power inputs
                inputC /= 3 * BIGGERTRIGGER; //slow down our power inputs

            }
        }
        //Use the larger trigger value to scale down the inputs.
        arcadeMecanum(inputY, inputX, inputC, front_left, front_right, back_left, back_right);
    }

    // y - forwards
    // x - side
    // c - rotation
    public static void arcadeMecanum(double y, double x, double c, DcMotor frontLeft, DcMotor frontRight, DcMotor backLeft, DcMotor backRight) {
        double leftFrontVal = y + x + c;
        double rightFrontVal = y - x - c;
        double leftBackVal = y - x + c;
        double rightBackVal = y + x - c;
        //Adding Vels for strafing, driving, and turning.
        double strafeVel;
        double driveVel;
        double turnVel;
        {
            driveVel = 0;
            strafeVel = 0;
            turnVel = 0;
            //this part allows the robot to turn with the mecanums, taking our certain variables so
            //it can turn.
            double leftFrontVel = -driveVel - strafeVel + turnVel;
            double rightFrontVel = -driveVel + strafeVel - turnVel;
            double leftRearVel = -driveVel + strafeVel + turnVel;
            double rightRearVel = -driveVel - strafeVel - turnVel;
            double[] vels = {leftFrontVel, rightFrontVel, leftRearVel, rightRearVel};

            Arrays.sort(vels);
            if (vels[3] > 1) {
                leftFrontVel /= vels[3];
                rightFrontVel /= vels[3];
                leftRearVel /= vels[3];
                rightRearVel /= vels[3];
            }
            frontLeft.setPower(leftFrontVel);
            frontRight.setPower(rightFrontVel);
            backLeft.setPower(leftRearVel);
            backRight.setPower(rightRearVel);
        }

        double[] wheelPowers = {rightFrontVal, leftFrontVal, leftBackVal, rightBackVal};
        Arrays.sort(wheelPowers);
        if (wheelPowers[3] > 1) {
            leftFrontVal /= wheelPowers[3];
            rightFrontVal /= wheelPowers[3];
            leftBackVal /= wheelPowers[3];
            rightBackVal /= wheelPowers[3];
        }
        double scaledPower = SCALEDPOWER;
        //This puts power to the wheels in the correct way to make it turn.
        front_left.setPower(leftFrontVal * scaledPower + frontLeft.getPower() * (+scaledPower));
        back_left.setPower(leftBackVal * scaledPower + backLeft.getPower() * (+scaledPower));
        front_right.setPower(rightFrontVal * scaledPower + frontRight.getPower() * (1 - scaledPower));
        back_right.setPower(rightBackVal * scaledPower + backRight.getPower() * (1 - scaledPower));
    }
}