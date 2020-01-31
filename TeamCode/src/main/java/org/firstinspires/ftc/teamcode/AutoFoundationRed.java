package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;


@Autonomous(name="AutoFoundationRed", group="Pushbot")
public class AutoFoundationRed extends LinearOpMode {

    HardwarePushbot robot = new HardwarePushbot();
    private ElapsedTime runtime = new ElapsedTime();

    static final double DRIVE_SPEED = 0.5;

    private DcMotor back_left = null;
    private DcMotor front_right = null;
    private DcMotor back_right = null;
    private DcMotor front_left = null;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        front_left = hardwareMap.dcMotor.get(UniversalConstants.LEFT1NAME);
        back_left = hardwareMap.dcMotor.get(UniversalConstants.LEFT2NAME);
        front_right = hardwareMap.dcMotor.get(UniversalConstants.RIGHT1NAME);
        back_right = hardwareMap.dcMotor.get(UniversalConstants.RIGHT2NAME);

        front_left.setDirection(DcMotor.Direction.FORWARD);
        back_left.setDirection(DcMotor.Direction.REVERSE);
        front_right.setDirection(DcMotor.Direction.FORWARD);
        back_right.setDirection(DcMotor.Direction.REVERSE);

        Servo rightFoundation = hardwareMap.servo.get("rightFoundation");
        Servo leftFoundation = hardwareMap.servo.get("leftFoundation");

        waitForStart();

        //This is the block for strafing to line up to the foundation
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 1.20)) {
            telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();

            front_left.setPower(DRIVE_SPEED);
            back_left.setPower(DRIVE_SPEED);
            front_right.setPower(DRIVE_SPEED);
            back_right.setPower(DRIVE_SPEED);

        }

        front_left.setPower(0);
        back_left.setPower(0);
        front_right.setPower(0);
        back_right.setPower(0);

        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(500);


        //this is for driving up to the foundation
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 0.60)) {
            telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();

            front_left.setPower(DRIVE_SPEED);
            back_left.setPower(-DRIVE_SPEED);
            front_right.setPower(-DRIVE_SPEED);
            back_right.setPower(DRIVE_SPEED);
            telemetry.update();
        }

        front_left.setPower(0);
        back_left.setPower(0);
        front_right.setPower(0);
        back_right.setPower(0);


        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(500);


        //this makes the foundation movers grab the foundation
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 1.15)) {

            rightFoundation.setPosition(1);
            leftFoundation.setPosition(0);
        }
        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(500);


        //this is for driving back from the foundation to the wall
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 1.30)) {
            telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();

            front_left.setPower(-DRIVE_SPEED);
            back_left.setPower(DRIVE_SPEED);
            front_right.setPower(DRIVE_SPEED);
            back_right.setPower(-DRIVE_SPEED);

            telemetry.update();
        }
        front_left.setPower(0);
        back_left.setPower(0);
        front_right.setPower(0);
        back_right.setPower(0);

        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(500);

        //this is for letting go of the foundation
            runtime.reset();
            while (opModeIsActive() && (runtime.seconds() < 1.00)) {
                rightFoundation.setPosition(0);
                leftFoundation.setPosition(1);
            }

        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(500);

            //for going to the line and stopping
            runtime.reset();
            while (opModeIsActive() && (runtime.seconds() < 1.70)) {
                telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
                telemetry.update();

                front_left.setPower(-DRIVE_SPEED);
                back_left.setPower(-DRIVE_SPEED);
                front_right.setPower(-DRIVE_SPEED);
                back_right.setPower(-DRIVE_SPEED);
            }
        }
    }

