package day01.huy.imagechoosing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/** A basic Camera preview class */

public class CameraFrontActivity extends AppCompatActivity {

    private Camera mCamera;
    private CameraPreview mPreview;

    public static final int MEDIA_TYPE_IMAGE = 1;

    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_front);

        // Create an instance of Camera


        mCamera = getCameraInstance();
        mCamera.setDisplayOrientation(90);

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = findViewById(R.id.camera_front_preview);
        preview.addView(mPreview);

        final Camera.PictureCallback mPicture = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                Uri picUri = Uri.fromFile(pictureFile);
                if (pictureFile == null){
                    return;
                }

                try {
                    byte[] pictureBytes;
                    Bitmap thePicture = BitmapFactory.decodeByteArray(data, 0, data.length);
                    Matrix m = new Matrix();
                    m.postRotate(270);
                    thePicture = Bitmap.createBitmap(thePicture, 0, 0, thePicture.getWidth(), thePicture.getHeight(), m, true);

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    thePicture.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    pictureBytes = bos.toByteArray();

                    FileOutputStream fs = new FileOutputStream(pictureFile);
                    fs.write(pictureBytes);
                    fs.close();
                } catch (FileNotFoundException e) {
                    Log.d("MyCameraApp", "file not found");
                } catch (IOException e) {
                    Log.d("MyCameraApp", "error");
                }
                getImageUri(picUri);
            }



        };

        Button captureButton =  findViewById(R.id.button_front_capture);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        mCamera.takePicture(null, null, mPicture);
                    }
                }
        );

    }



    public void getImageUri(Uri picUri){
        Intent intent = new Intent(CameraFrontActivity.this, CameraFrontReceiveActivity.class);
        intent.putExtra("picUri", picUri);
        startActivity(intent);
        finish();
    }


    public static Camera getCameraInstance(){
        Camera cam = null;
        try {
            int cameraCount = 0;
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            cameraCount = Camera.getNumberOfCameras();
            for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
                Camera.getCameraInfo(camIdx, cameraInfo);
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    try {
                        cam = Camera.open(camIdx);
                    } catch (RuntimeException e) {
                        Log.e("", "Camera failed to open: " + e.getLocalizedMessage());
                    }
                }
            }
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return cam; // returns null if camera is unavailable
    }



    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();// release the camera for other applications
            mCamera = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }
}

