package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;


@Autonomous(name="AutoFoundationBlue", group="Pushbot")
public class AutoFoundationBlue extends LinearOpMode {

    HardwarePushbot robot = new HardwarePushbot();
    private ElapsedTime runtime = new ElapsedTime();

    static final double CRATER_SPEED = 1;
    static final double STRAFE_SPEED = 1;

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
        runtime.reset();

        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 0.35)) {
            telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();

            front_left.setPower(-CRATER_SPEED);
            back_left.setPower(-CRATER_SPEED);
            front_right.setPower(-CRATER_SPEED);
            back_right.setPower(-CRATER_SPEED);

        }

            runtime.reset();
            while (opModeIsActive() && (runtime.seconds() < 1)) {
                telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
                telemetry.update();

                front_left.setPower(CRATER_SPEED);
                back_left.setPower(CRATER_SPEED);
                front_right.setPower(CRATER_SPEED);
                back_right.setPower(CRATER_SPEED);

            }

                //this is where we put the servo moving code

                runtime.reset();
                while (opModeIsActive() && (runtime.seconds() < 1)) {
                    telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
                    telemetry.update();

                    front_left.setPower(-CRATER_SPEED);
                    back_left.setPower(-CRATER_SPEED);
                    front_right.setPower(-CRATER_SPEED);
                    back_right.setPower(-CRATER_SPEED);
                }

                runtime.reset();
                while (opModeIsActive() && (runtime.seconds() < 0.35)) {
                    telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
                    telemetry.update();

                    front_left.setPower(-CRATER_SPEED);
                    back_left.setPower(-CRATER_SPEED);
                    front_right.setPower(-CRATER_SPEED);
                    back_right.setPower(-CRATER_SPEED);
                }



            }
        }