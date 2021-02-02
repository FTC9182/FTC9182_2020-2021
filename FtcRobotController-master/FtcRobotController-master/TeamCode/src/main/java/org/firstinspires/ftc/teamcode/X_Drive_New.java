package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

public class X_Drive_New{

    private ElapsedTime cooldown0 = new ElapsedTime();

    public DcMotor leftUpDrive = null;
    public DcMotor rightUpDrive = null;
    public DcMotor leftDownDrive = null;
    public DcMotor rightDownDrive = null;

    public double wheelsPowerBoost = .5;

    double leftUpPower;
    double rightUpPower;
    double leftDownPower;
    double rightDownPower;

    double leftUpPower2 = 0;
    double rightUpPower2 = 0;
    double leftDownPower2 = 0;
    double rightDownPower2 = 0;

    double turnPower1;
    double turnPower2;

    int powerMode= 0;

    void PowerMotors(double boostNumber)
    {
        leftUpDrive.setPower(leftUpPower * boostNumber);
        rightUpDrive.setPower(rightUpPower * boostNumber);
        leftDownDrive.setPower(leftDownPower * boostNumber);
        rightDownDrive.setPower(rightDownPower * boostNumber);
    }

    void AssignPowerValues(Gamepad gamepad1)
    {
        leftUpPower = gamepad1.left_stick_y;
        rightUpPower = -gamepad1.left_stick_y;
        leftDownPower = gamepad1.left_stick_y;
        rightDownPower = -gamepad1.left_stick_y;

        if(Math.abs(gamepad1.left_stick_x) > .4f)
        {
            leftUpPower2 = -gamepad1.left_stick_x;
            rightUpPower2 = -gamepad1.left_stick_x;
            leftDownPower2 = gamepad1.left_stick_x;
            rightDownPower2 = gamepad1.left_stick_x;

            leftUpPower += leftUpPower2;
            rightUpPower += rightUpPower2;
            leftDownPower += leftDownPower2;
            rightDownPower += rightDownPower2;
        }

        turnPower1 = gamepad1.right_stick_x;
        turnPower2 = gamepad1.right_stick_x;

        if(Math.abs(turnPower1) >= .1f)
        {
            leftUpPower = turnPower1;
            rightUpPower = turnPower1;
            leftDownPower = turnPower2;
            rightDownPower = turnPower2;
        }
    }

    double PowerBoostValue(String buttonPressed)
    {
        if (cooldown0.milliseconds() >= 200)
        {
            switch (buttonPressed)
            {
                case "up":
                    switch(powerMode)
                    {
                        case 0:
                            wheelsPowerBoost = .1;
                            powerMode++;
                            break;
                        case 1:
                            wheelsPowerBoost = .5;
                            powerMode++;
                            break;
                        case 2:
                            wheelsPowerBoost = .75;
                            powerMode++;
                            break;
                        case 3:
                            wheelsPowerBoost = 1;
                            powerMode = 3;
                            break;
                    }
                    cooldown0.reset();
                    break;
                case "down":
                    switch(powerMode)
                    {
                        case 0: wheelsPowerBoost = .1;
                            powerMode = 0;
                            break;
                        case 1: wheelsPowerBoost = .5;
                            powerMode--;
                            break;
                        case 2: wheelsPowerBoost = .75;
                            powerMode--;
                            break;
                        case 3: wheelsPowerBoost = 1;
                            powerMode--;
                            break;
                    }
                    cooldown0.reset();
                    break;
                default:
                    break;
            }
        }
        return wheelsPowerBoost;
    }

    boolean AutonMove(String direction, Double power, Double time, Boolean noTime)
    {
        ElapsedTime timer = new ElapsedTime();

        switch (direction)
        {
            case "forward":
                timer.reset();

                leftUpDrive.setPower(-power);
                leftDownDrive.setPower(-power);
                rightUpDrive.setPower(power);
                rightDownDrive.setPower(power);

                break;
            case "backward":
                timer.reset();

                leftUpDrive.setPower(power);
                leftDownDrive.setPower(power);
                rightUpDrive.setPower(-power);
                rightDownDrive.setPower(-power);

                break;
            case "left":
                timer.reset();

                leftUpDrive.setPower(power);
                leftDownDrive.setPower(-power);
                rightUpDrive.setPower(power);
                rightDownDrive.setPower(-power);

                break;
            case "right":
                timer.reset();

                leftUpDrive.setPower(-power);
                leftDownDrive.setPower(power);
                rightUpDrive.setPower(-power);
                rightDownDrive.setPower(power);

                break;
            case "forward_right":
                timer.reset();

                leftUpDrive.setPower(power);
                leftDownDrive.setPower(0);
                rightUpDrive.setPower(0);
                rightDownDrive.setPower(-power);

                break;
            case "backward_right":
                timer.reset();

                leftUpDrive.setPower(0);
                leftDownDrive.setPower(-power);
                rightUpDrive.setPower(power);
                rightDownDrive.setPower(0);

                break;
            case "forward_left":
                timer.reset();

                leftUpDrive.setPower(0);
                leftDownDrive.setPower(power);
                rightUpDrive.setPower(-power);
                rightDownDrive.setPower(0);

                break;
            case "backward_left":
                timer.reset();

                leftUpDrive.setPower(-power);
                leftDownDrive.setPower(0);
                rightUpDrive.setPower(0);
                rightDownDrive.setPower(power);

                break;
            case "clockwise_turn":
                timer.reset();

                leftUpDrive.setPower(power);
                leftDownDrive.setPower(power);
                rightUpDrive.setPower(power);
                rightDownDrive.setPower(power);

                break;
            case "counter_turn":
                timer.reset();

                leftUpDrive.setPower(-power);
                leftDownDrive.setPower(-power);
                rightUpDrive.setPower(-power);
                rightDownDrive.setPower(-power);

                break;
        }

        if (!noTime){
            for(;;)
            {
                if(timer.seconds() >= time) { break; }
            }
            leftUpDrive.setPower(0);
            leftDownDrive.setPower(0);
            rightUpDrive.setPower(0);
            rightDownDrive.setPower(0);
        }

        return true;
    }

    boolean AutonStop()
    {
        rightUpDrive.setPower(0);
        rightDownDrive.setPower(0);
        leftUpDrive.setPower(0);
        leftDownDrive.setPower(0);

        return true;
    }
}


