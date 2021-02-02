package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="Full", group="Development")

public class BestTeleop extends LinearOpMode {

    Wobble_Grabber_New wobbleGrabber = null;
    X_Drive_New driveTrain = null;
    Intake_New intake = null;
    Shooter_New shooter = null;

    double wheelBoostValue = 0;

    @Override
    public void runOpMode(){
        wobbleGrabber = new Wobble_Grabber_New();
        driveTrain = new X_Drive_New();
        intake = new Intake_New();
        shooter = new Shooter_New();

        wobbleGrabber.grabberArm = hardwareMap.get(DcMotor.class, "armMotor");
        wobbleGrabber.grabberPeice = hardwareMap.get(Servo.class, "grabberServo");
        driveTrain.leftUpDrive  = hardwareMap.get(DcMotor.class, "left_drive1");
        driveTrain.rightUpDrive = hardwareMap.get(DcMotor.class, "right_drive1");
        driveTrain.leftDownDrive = hardwareMap.get(DcMotor.class, "left_drive2");
        driveTrain.rightDownDrive = hardwareMap.get(DcMotor.class, "right_drive2");
        intake.intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        shooter.pusher = hardwareMap.get(Servo.class, "pusher servo");
        shooter.shooter = hardwareMap.get(DcMotor.class, "shooter motor");

        waitForStart();
        wobbleGrabber.GrabberInit();
        shooter.Init();
        /*used inputs
        Gamepad1:
            dpad up: drive power up
            dpad down: drive power down

            left stick x: left--right
            left stick y: forward--back
            right stick x: turn
        Gamepad2:
            dpad up: arm power up
            dpad down: arm power down

            A: open grabber
            B: power intake
            Y: power shooter

            right bumper: arm out
            left bumper: arm in

            right trigger: pusher out
            left trigger: pusher back
        */
        while(opModeIsActive())
        {

            //Wobble Grabber Code
            if(gamepad2.dpad_up) { wobbleGrabber.SwitchArmPowerUp(); } //Arm Power Up
            else if(gamepad2.dpad_down) { wobbleGrabber.SwitchArmPowerDown(); } //Arm Power Down

            if(gamepad2.a) { wobbleGrabber.GrabberPeice(); } //Open Arm Grabber

            if(gamepad2.right_bumper) { wobbleGrabber.GrabberArm("right_bumper"); } //Moves Arm
            else if(gamepad2.left_bumper) { wobbleGrabber.GrabberArm("left_bumper"); } //Moves Arm Opposite Direction

            //Drive Train Code
            if(gamepad1.dpad_down) wheelBoostValue = driveTrain.PowerBoostValue("down");
            else if (gamepad1.dpad_up)  wheelBoostValue = driveTrain.PowerBoostValue("up");
            driveTrain.AssignPowerValues(gamepad1);

            driveTrain.PowerMotors(wheelBoostValue);

            //Intake Code
            if(gamepad2.b) { intake.powerSwitch(); }

            //Shooter Code
            if(gamepad2.y) { shooter.ShooterPower(); }

            if(gamepad2.right_trigger >= .5f)
            {
                shooter.PusherPowerUp();
            }
            else if(gamepad2.left_trigger >= .5f)
            {
                shooter.PusherPowerDown();
            }

            //Telemetry
            telemetry.addData("Grabber Arm Power", wobbleGrabber.power);
            telemetry.addData("Drive Motors Powe", wheelBoostValue);
            telemetry.update();
        }
    }
}
