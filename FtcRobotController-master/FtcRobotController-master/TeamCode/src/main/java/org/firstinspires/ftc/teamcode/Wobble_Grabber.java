/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Wobble Grab", group="old")

public class Wobble_Grabber extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime cooldown0 = new ElapsedTime();
    private ElapsedTime cooldown1 = new ElapsedTime();
    private ElapsedTime cooldown2 = new ElapsedTime();

    private Servo grabberPeice = null;
    private DcMotor grabberArm = null;

    int powerMode= 0;
    int holdActivation = 0;
    int pastPos = 0;

    double power = .5;

    @Override
    public void runOpMode() {

        grabberPeice = hardwareMap.get(Servo.class, "grabberServo");
        grabberArm = hardwareMap.get(DcMotor.class, "armMotor");

        grabberArm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //grabberPeice.setPosition(0);


        waitForStart();


        while (opModeIsActive()) {
            if(gamepad1.a == true && cooldown0.milliseconds() > 750)
            {
                switch (holdActivation)
                {
                    case 0:
                        grabberPeice.setPosition(50);
                        holdActivation++;
                        break;
                    case 1:
                        grabberPeice.setPosition(0);
                        holdActivation = 0;
                        break;
                }
                cooldown0.reset();
            }

            if(gamepad1.dpad_up == true && cooldown1.milliseconds() > 200)
            {
                switch(powerMode)
                {
                    case 0: power = .1;
                        powerMode++;
                        break;
                    case 1: power = .5;
                        powerMode++;
                        break;
                    case 2: power = .75;
                        powerMode++;
                        break;
                    case 3: power = 1;
                        powerMode = 0;
                        break;
                }

                cooldown1.reset();

            }
            /*
            if(gamepad1.right_trigger > .5)
            {
                grabberArm.setPower(gamepad1.right_trigger * power);
            }
            else if(gamepad1.left_trigger > .5)
            {
                grabberArm.setPower(gamepad1.left_trigger * -power);

            }
            else
            {

                grabberArm.setPower(0);
                grabberArm.setTargetPosition(pastPos);
                pastPos = grabberArm.getCurrentPosition();
            }
            */

            if(gamepad1.right_bumper == true)
            {
                grabberArm.setPower(power);
            }
            else if(gamepad1.left_bumper == true)
            {
                grabberArm.setPower(-power);

                //if(grabberArm.getCurrentPosition() <= 30)
                //{
                  //  grabberArm.setPower(0);
                //}
            }
            else
            {
                grabberArm.setPower(0);
            }





            telemetry.addData("Grabber Arm Power", grabberArm.getPower());
            telemetry.addData("left bumper", gamepad1.left_bumper);
            telemetry.addData("right bumper", gamepad1.right_bumper);
            telemetry.addData("Encoder", grabberArm.getCurrentPosition());
            telemetry.update();
        }
    }
}
