package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name="Teleop_Omni", group="TeleOp")
public class Teleop_Omni extends LinearOpMode {

        @Override
        public void runOpMode() throws InterruptedException {
            waitForStart();
            while(opModeIsActive()){

                this.telemetry.update();

                telemetry.addData("Status", "Initialized");

                DcMotor front_left = hardwareMap.get(DcMotor.class,"front_left");
                DcMotor front_right = hardwareMap.get(DcMotor.class, "front_right");
                DcMotor back_left = hardwareMap.get(DcMotor.class, "back_left");
                DcMotor back_right = hardwareMap.get(DcMotor.class, "back_right");

                front_right.setDirection(DcMotorSimple.Direction.REVERSE);
                front_left.setDirection(DcMotorSimple.Direction.FORWARD);
                back_right.setDirection(DcMotorSimple.Direction.REVERSE);
                back_left.setDirection(DcMotorSimple.Direction.FORWARD);

                double rightfront = gamepad1.right_stick_y;
                double rightback = gamepad1.right_stick_y;
                double leftfront = gamepad1.left_stick_y;
                double leftback = gamepad1.left_stick_y;

                front_left.setPower(leftfront);
                front_right.setPower(rightfront);
                back_left.setPower(leftback);
                back_right.setPower(rightback);
                telemetry.addData("Status", "Running");
                telemetry.update();



            // Wait for the game to start (driver presses PLAY)
            waitForStart();

            // run until the end of the match (driver presses STOP)

                telemetry.addData("Status", "Running");


            }

        }
    }


