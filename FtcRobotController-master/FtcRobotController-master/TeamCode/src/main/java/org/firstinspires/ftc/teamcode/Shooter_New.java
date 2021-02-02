package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Shooter_New {

    int motorPowered = 0;

    ElapsedTime motorCooldown = new ElapsedTime();
    ElapsedTime servoDelay = new ElapsedTime();

    public Servo pusher = null;
    public DcMotor shooter = null;

    public void Init() { pusher.setPosition(0); }

    public void ShooterPower()
    {
        if(motorCooldown.milliseconds() >= 1000) {
            switch (motorPowered) {
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
        }
    }

    public void PusherPowerUp() {
        if (servoDelay.milliseconds() >= 1000) {
            pusher.setPosition(.35);
            servoDelay.reset();
        }
    }

    public void PusherPowerDown() {
        if (servoDelay.milliseconds() >= 1000) {
            pusher.setPosition(0);
            servoDelay.reset();
        }
    }
}
