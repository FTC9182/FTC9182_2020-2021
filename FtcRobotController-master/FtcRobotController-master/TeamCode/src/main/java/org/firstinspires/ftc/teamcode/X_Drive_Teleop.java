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
import com.qualcomm.robotcore.hardware.DcMotorSimple;
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

@TeleOp(name="X-Drive", group="Development")

public class X_Drive_Teleop extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftUpDrive = null;
    private DcMotor rightUpDrive = null;
    private DcMotor leftDownDrive = null;
    private DcMotor rightDownDrive = null;

    public double wheelsPower = .5;

    @Override
    public void runOpMode() {
        leftUpDrive  = hardwareMap.get(DcMotor.class, "left_drive1");
        rightUpDrive = hardwareMap.get(DcMotor.class, "right_drive1");
        leftDownDrive = hardwareMap.get(DcMotor.class, "left_drive2");
        rightDownDrive = hardwareMap.get(DcMotor.class, "right_drive2");

        leftUpDrive.setDirection(DcMotor.Direction.FORWARD);
        rightUpDrive.setDirection(DcMotor.Direction.FORWARD);
        leftDownDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDownDrive.setDirection(DcMotor.Direction.FORWARD);

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {

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

            leftUpDrive.setPower(leftUpPower);
            rightUpDrive.setPower(rightUpPower);
            leftDownDrive.setPower(leftDownPower);
            rightDownDrive.setPower(rightDownPower);
        }
    }
}
