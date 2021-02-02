/* Copyright (c) 2019 FIRST. All rights reserved.
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

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;


@Autonomous(name = "Tensorflow Test", group = "development")
public class CustomTensorFlowObjectDetection extends LinearOpMode {
    private static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Quad";
    private static final String LABEL_SECOND_ELEMENT = "Single";

    private static final String VUFORIA_KEY = "AbpX9OD/////AAABmS3CFVKCS01/sXG8NYr80JJPBuyElaY2VPPISPGGHiPIajaD5g3N93kOociZeKBGFqDpl07DPu7yU8xZefaNpNSmcrl9KKYXoUdH2UuIdd14AG/iTNweMOzpnXvkKWpW3B3vf3muCE/EUTHNgOcHAuYtCfVscQqm2LA+SfZG5U6kR6XHhRON5oXgrRnXNSek8cN9TrTVXNNAeI8Ygip9AaZS6/rLXQxXGVsFaNbOnMR1ooJMDb0aiFvq4bOZg0XZgEmjXquWkXqkD+6d4hApghss7owAODYkgePa0feY72Mf2xtjPGqEKuz8kCCdWBQ2LqnFqgSwRaYg0wV0BEOFKKPsWUCUDSDGs0Hnf5Q1WgAS";

    private VuforiaLocalizer vuforia;

    private TFObjectDetector tfod;

    boolean mainActive = false;

    @Override
    public void runOpMode() {
        initVuforia();
        initTfod();

        if (tfod != null) {
            tfod.activate();
        }

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();
        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {
                if (tfod != null && !mainActive) {
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    //telemetry.addData("tfod", tfod);
                    if (updatedRecognitions != null) {
                        telemetry.addData("# Object Detected", updatedRecognitions.size());
                        int i = 0;
                        telemetry.addData("recognition", updatedRecognitions);
                        telemetry.update();
                        for (Recognition recognition : updatedRecognitions) {
                            telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                        //telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                //recognition.getLeft(), recognition.getTop());
                        //telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                //recognition.getRight(), recognition.getBottom());

                          if(updatedRecognitions.size() == 1)
                          {
                              if(recognition.getLabel() == LABEL_SECOND_ELEMENT && recognition.getConfidence() >= .95)
                              {
                                  telemetry.addData("Run Mode:", 2);
                              }
                              else if(recognition.getLabel() == LABEL_FIRST_ELEMENT && recognition.getConfidence() >= .95)
                              {
                                  telemetry.addData("Run Mode:", 3);
                              }


                          }
                          if(updatedRecognitions.size() == 0)
                          {
                              telemetry.addData("Run Mode:", 1);
                          }
                          telemetry.addData("confidence", recognition.getConfidence());
                      }

                    }




                }


            }

            if(mainActive)
            {

            }
            telemetry.update();
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

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
            "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
       tfodParameters.minResultConfidence = 0.8f;
       tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
       tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }
}
