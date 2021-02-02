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

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.internal.android.dx.command.dump.DotDumper;

public class Wobble_Grabber_New{

    private ElapsedTime cooldown0 = new ElapsedTime();
    private ElapsedTime cooldown1 = new ElapsedTime();
    private ElapsedTime cooldown2 = new ElapsedTime();

    public Servo grabberPeice = null;
    public DcMotor grabberArm = null;

    int powerMode= 0;
    int holdActivation = 0;
    int pastPos = 0;

    double power = .1;

    void GrabberInit()
    {
        grabberArm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }


    public void GrabberPeice() {
        if (cooldown0.milliseconds() > 750) {
            switch (holdActivation) {
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
    }

    void SwitchArmPowerUp()
    {
        if(cooldown1.milliseconds() > 200)
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
                    powerMode = 3;
                    break;
            }
            cooldown1.reset();
        }
    }

    void SwitchArmPowerDown()
    {
        if(cooldown1.milliseconds() > 200)
        {
            switch(powerMode)
            {
                case 0: power = .1;
                    powerMode = 0;
                    break;
                case 1: power = .5;
                    powerMode--;
                    break;
                case 2: power = .75;
                    powerMode--;
                    break;
                case 3: power = 1;
                    powerMode--;
                    break;
            }
            cooldown1.reset();
        }
    }

    void GrabberArm(String buttonLabel)
    {
        if(buttonLabel == "right_bumper")
        {
        grabberArm.setPower(power);
        }
        else if(buttonLabel == "left_bumper")
        {
        grabberArm.setPower(-power);
        }
        else
        {
        grabberArm.setPower(0);
        }
    }

    public boolean AutonSequence()
    {
        ElapsedTime timer = new ElapsedTime();
        power = .625;
        grabberPeice.setPosition(1);
        grabberArm.setPower(power);
        while(!DoTimer(timer, 750 )) {}
        grabberPeice.setPosition(0);
        grabberArm.setPower(0);
        while(!DoTimer(timer, 2250)) {}
        //grabberPeice.setPosition(0);
        //while(!DoTimer(timer, 1000)) {}
        return true;
    }

    public boolean AutonSequence2()
    {
        ElapsedTime timer = new ElapsedTime();
        power = .3;
        grabberArm.setPower(-power);
        while(!DoTimer(timer, 500)) {}
        grabberPeice.setPosition(1);
        while(!DoTimer(timer, 4000)) {}
        grabberArm.setPower(0);
        while(!DoTimer(timer, 750)) {}

        return true;
    }

    boolean DoTimer(ElapsedTime timer, int time)
    {
        timer.reset();
        for(;;)
        {
            if(timer.milliseconds() >= time) break;
        }
        return true;
    }
}
