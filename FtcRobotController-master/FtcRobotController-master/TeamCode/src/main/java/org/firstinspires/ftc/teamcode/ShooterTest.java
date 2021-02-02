package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Shooter Test", group = "development")
public class ShooterTest extends LinearOpMode{

    int motorPowered = 0;

    ElapsedTime motorCooldown = new ElapsedTime();
    ElapsedTime servoDelay = new ElapsedTime();

    Servo pusher = null;
    DcMotor shooter = null;

    @Override
    public void runOpMode()
    {

        pusher = hardwareMap.get(Servo.class, "pusher servo");
        shooter = hardwareMap.get(DcMotor.class, "shooter motor");

        pusher.setPosition(0);
        waitForStart();

        while(opModeIsActive())
        {

            if(gamepad1.right_trigger >= .5f && servoDelay.seconds() >= 1)
            {
                pusher.setPosition(.35);
                servoDelay.reset();
            }
            else if(gamepad1.left_trigger >= .5f && servoDelay.seconds() >= 1)
            {
                pusher.setPosition(0);
                servoDelay.reset();
            }

            if(gamepad1.x && motorCooldown.seconds() >= 1)
            {
                switch (motorPowered)
                {
                    case 0:
                        shooter.setPower(-.85);
                        motorPowered = 1;

                        break;
                    case 1:
                        shooter.setPower(0);
                        motorPowered = 0;
                        break;
                    default:
                        break;
                }
                motorCooldown.reset();
            }
        }
    }
}
