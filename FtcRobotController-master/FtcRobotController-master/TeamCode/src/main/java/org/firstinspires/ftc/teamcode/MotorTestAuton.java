package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

@Autonomous(name="AutonTest", group = "auton")
public class MotorTestAuton extends LinearOpMode
{
    X_Drive_New x_drive_new = null;
    Wobble_Grabber_New wobbleGrabber = null;

    @Override
    public void runOpMode()
    {
        x_drive_new = new X_Drive_New();
        wobbleGrabber = new Wobble_Grabber_New();

        x_drive_new.leftUpDrive  = hardwareMap.get(DcMotor.class, "left_drive1");
        x_drive_new.rightUpDrive = hardwareMap.get(DcMotor.class, "right_drive1");
        x_drive_new.leftDownDrive = hardwareMap.get(DcMotor.class, "left_drive2");
        x_drive_new.rightDownDrive = hardwareMap.get(DcMotor.class, "right_drive2");
        wobbleGrabber.grabberArm = hardwareMap.get(DcMotor.class, "armMotor");
        wobbleGrabber.grabberPeice = hardwareMap.get(Servo.class, "grabberServo");

        waitForStart();

        while(!wobbleGrabber.AutonSequence()) {}
        while(!x_drive_new.AutonMove("right", .5, .5, false)) {}
        while(!wobbleGrabber.AutonSequence2()) {}
        this.sleep(10000);

    }

}
