package io.github.eyinfo.chatinput.camera;


import io.github.eyinfo.chatinput.listener.CameraEventListener;
import io.github.eyinfo.chatinput.listener.OnCameraCallbackListener;

public interface CameraSupport {
    CameraSupport open(int cameraId, int width, int height, boolean isFacingBack, float cameraQuality);

    void release();

    void takePicture();

    void setCameraCallbackListener(OnCameraCallbackListener listener);

    void setCameraEventListener(CameraEventListener listener);

    void startRecordingVideo();

    void cancelRecordingVideo();

    String finishRecordingVideo();
}
