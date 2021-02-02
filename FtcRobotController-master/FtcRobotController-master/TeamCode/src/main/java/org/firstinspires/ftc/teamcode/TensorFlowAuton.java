package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.SwitchableLight;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

@Autonomous(name="TensorFlowAuton", group = "auton")
public class TensorFlowAuton extends LinearOpMode
{
    //VUFORIA STUFF
    private static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Quad";
    private static final String LABEL_SECOND_ELEMENT = "Single";
    private static final String VUFORIA_KEY = "AbpX9OD/////AAABmS3CFVKCS01/sXG8NYr80JJPBuyElaY2VPPISPGGHiPIajaD5g3N93kOociZeKBGFqDpl07DPu7yU8xZefaNpNSmcrl9KKYXoUdH2UuIdd14AG/iTNweMOzpnXvkKWpW3B3vf3muCE/EUTHNgOcHAuYtCfVscQqm2LA+SfZG5U6kR6XHhRON5oXgrRnXNSek8cN9TrTVXNNAeI8Ygip9AaZS6/rLXQxXGVsFaNbOnMR1ooJMDb0aiFvq4bOZg0XZgEmjXquWkXqkD+6d4hApghss7owAODYkgePa0feY72Mf2xtjPGqEKuz8kCCdWBQ2LqnFqgSwRaYg0wV0BEOFKKPsWUCUDSDGs0Hnf5Q1WgAS";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    //VUFORIA STUFF

    boolean mainActive = false;
    int runMode = 1;

    X_Drive_New x_drive_new = null;
    GoodColorSensor goodColorSensor = null;
    Wobble_Grabber_New wobbleGrabber = null;

    ElapsedTime tensorFlowTimer = new ElapsedTime();

    @Override
    public void runOpMode()
    {
        initVuforia();
        initTfod();

        initColorSensor();

        if (tfod != null) { tfod.activate(); }

        x_drive_new = new X_Drive_New();
        goodColorSensor = new GoodColorSensor();
        wobbleGrabber = new Wobble_Grabber_New();

        x_drive_new.leftUpDrive  = hardwareMap.get(DcMotor.class, "left_drive1");
        x_drive_new.rightUpDrive = hardwareMap.get(DcMotor.class, "right_drive1");
        x_drive_new.leftDownDrive = hardwareMap.get(DcMotor.class, "left_drive2");
        x_drive_new.rightDownDrive = hardwareMap.get(DcMotor.class, "right_drive2");
        wobbleGrabber.grabberArm = hardwareMap.get(DcMotor.class, "armMotor");
        wobbleGrabber.grabberPeice = hardwareMap.get(Servo.class, "grabberServo");
        goodColorSensor.colorSensor = hardwareMap.get(NormalizedColorSensor.class, "bottomcolor");

        wobbleGrabber.GrabberInit();
        telemetry.addData("ok to start", "yes");
        telemetry.update();
        waitForStart();

        while(!x_drive_new.AutonMove("left", .5, 1.25, false))  {}
        while(!x_drive_new.AutonMove("forward", .5, .75, false)) {}

        //Tensor Flow Detection
        tensorFlowTimer.reset();
        while(!mainActive)
        {
            if (tfod != null && !mainActive) {
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.clear();
                    telemetry.addData("# Object Detected", updatedRecognitions.size());
                    int i = 0;
                    telemetry.addData("recognition", updatedRecognitions);
                    telemetry.update();
                    for (Recognition recognition : updatedRecognitions) {


                        //If it sees a ring
                        //if(updatedRecognitions.size() <= 0)
                        //{
                            if(recognition.getLabel() == LABEL_SECOND_ELEMENT && recognition.getConfidence() >= .8)
                            {
                                runMode = 2;
                            }
                            else if(recognition.getLabel() == LABEL_FIRST_ELEMENT && recognition.getConfidence() >= .8)
                            {
                                runMode = 3;
                            }
                        //}
                        //5-second timer

                        //Confidence telemetry
                        telemetry.addData("# Object Detected", updatedRecognitions.size());
                        telemetry.addData("recognition", updatedRecognitions);
                        telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                        telemetry.addData("best option", runMode);
                        telemetry.addData("confidence", recognition.getConfidence());
                        telemetry.update();
                    }

                    if(tensorFlowTimer.seconds() >= 3)
                    {
                        telemetry.update();
                        mainActive = true;
                        telemetry.addData("run mode", mainActive);
                    }
                }
            }
        }

        telemetry.clear();
        telemetry.addData("main start", "true");
        telemetry.addData("runMode", runMode);
        telemetry.update();

        switch (runMode)
        {
            //goto B
            case 2:
                while(!x_drive_new.AutonMove("left", .5, 2.0, false));
                //while(RunSensor() != "blue") {}
                //x_drive_new.AutonStop();
                while (!wobbleGrabber.AutonSequence());
                x_drive_new.AutonMove("right", .5, 1.0, false);
                //while(RunSensor() != "white") {}
                //x_drive_new.AutonStop();

            //goto C
            case 3:
                int i = 0;
                while(!x_drive_new.AutonMove("backward", .5, 1.0, false)) {}
                while(!x_drive_new.AutonMove("left", .5, 3.0, false)) {}
                this.sleep(1000);
                while (!wobbleGrabber.AutonSequence());
                while(!x_drive_new.AutonMove("right", .5, .5, false)) {}
                while(!wobbleGrabber.AutonSequence2());
                while(!x_drive_new.AutonMove("backward", .5, 1.5, false)) {}

                //goto A
            default:
                while(!x_drive_new.AutonMove("forward", .5, 1.0, false)) {}
                while(!x_drive_new.AutonMove("left", .5, .5, false)) {}
                while (!wobbleGrabber.AutonSequence());
        }

        if (tfod != null) {
            tfod.shutdown();
        }
    }

    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }

    private void initColorSensor()
    {
        /*if (goodColorSensor.colorSensor instanceof SwitchableLight) {
            ((SwitchableLight)goodColorSensor.colorSensor).enableLight(true);
        }*/
    }

    private String RunSensor()
    {
        final float[] hsvValues = new float[3];
        goodColorSensor.colorSensor.setGain(goodColorSensor.gain);
        NormalizedRGBA colors = goodColorSensor.colorSensor.getNormalizedColors();
        Color.colorToHSV(colors.toColor(), hsvValues);

        telemetry.addData("r", goodColorSensor.colorSensor.getNormalizedColors().red);
        telemetry.addData("g", goodColorSensor.colorSensor.getNormalizedColors().green);
        telemetry.addData("b", goodColorSensor.colorSensor.getNormalizedColors().blue);
        telemetry.addData("color", Color.HSVToColor(hsvValues));
        telemetry.update();

        switch (Color.HSVToColor(hsvValues))
        {
            case Color.RED:
                return "blue";
            case Color.WHITE:
                return "white";
            default:
                return "no";
        }
    }

}
