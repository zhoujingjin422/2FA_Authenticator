package com.demo.example.authenticator.util;

import android.hardware.Camera;
import com.google.android.gms.vision.CameraSource;
import java.lang.reflect.Field;


public class CameraFocusFix {
    public static boolean cameraFocus(CameraSource cameraSource, String str) {
        Field[] declaredFields = CameraSource.class.getDeclaredFields();
        int length = declaredFields.length;
        int i = 0;
        while (i < length) {
            Field field = declaredFields[i];
            if (field.getType() == Camera.class) {
                field.setAccessible(true);
                try {
                    Camera camera = (Camera) field.get(cameraSource);
                    if (camera == null) {
                        return false;
                    }
                    Camera.Parameters parameters = camera.getParameters();
                    if (!parameters.getSupportedFocusModes().contains(str)) {
                        return false;
                    }
                    parameters.setFocusMode(str);
                    camera.setParameters(parameters);
                    return true;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                i++;
            }
        }
        return false;
    }
}
