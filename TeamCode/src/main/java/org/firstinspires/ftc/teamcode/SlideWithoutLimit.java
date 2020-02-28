package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="SlideNoLimit", group="TeleOp")
public class SlideWithoutLimit<opModeIsActive> extends LinearOpMode {
    @Override
    public void runOpMode() {
        //slide motors
        DcMotor slide_left = hardwareMap.get(DcMotor.class, "slide_left");
        DcMotor slide_right = hardwareMap.get(DcMotor.class, "slide_right");
        slide_right.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();
        while (opModeIsActive()) {
            double slidePow = -gamepad2.left_stick_y;
            slide_left.setPower(slidePow);
            slide_right.setPower(slidePow);
        }
    }
}