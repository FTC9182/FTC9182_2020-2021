package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Intake_New {

    private int i = 0;
    double power = 1;

    public DcMotor intakeMotor = null;
    ElapsedTime timer = new ElapsedTime();


    void powerSwitch() {
        if (timer.milliseconds() >= 2000) {
            switch (i) {
                case 0:
                    intakeMotor.setPower(power);
                    timer.reset();
                    i = 1;
                    break;
                case 1:
                    intakeMotor.setPower(0);
                    timer.reset();
                    i = 0;
                    break;
            }
        }
    }
}
