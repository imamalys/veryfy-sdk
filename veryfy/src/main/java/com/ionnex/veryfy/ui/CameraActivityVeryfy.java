package com.ionnex.veryfy.ui;

import static androidx.camera.core.ImageCapture.FLASH_MODE_OFF;
import static androidx.camera.core.ImageCapture.FLASH_MODE_ON;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Size;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;
import com.ionnex.veryfy.R;
import com.ionnex.veryfy.databinding.ActivityCameraBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class CameraActivityVeryfy extends AppCompatActivity {

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ImageCapture imageCapture;
    private boolean flashOn = false;
    private int lensFacing = CameraSelector.LENS_FACING_BACK;
    private ProcessCameraProvider cameraProvider;

    ActivityCameraBinding binding;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCameraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        lensFacing = getIntent().getBooleanExtra("selfie", false) ? CameraSelector.LENS_FACING_FRONT : CameraSelector.LENS_FACING_BACK;
        flashOn = getIntent().getBooleanExtra("flash", false);
        if (lensFacing == CameraSelector.LENS_FACING_FRONT ) {
            binding.surfaceView.getLayoutParams().height += 300;
            binding.ivGuideLine.setImageDrawable(getDrawable(R.drawable.card_guide_line));
        }
        cameraProviderFuture = ProcessCameraProvider.getInstance(CameraActivityVeryfy.this);
        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                startCameraX(cameraProvider);
                binding.ivCapture.setOnClickListener(v-> {
                    capture();
                });
                binding.ivRotate.setOnClickListener(v-> {
                    changeLens();
                });
                binding.ivClose.setOnClickListener(v-> {
                    finish();
                });
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, getExecutor());
    }

    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(CameraActivityVeryfy.this);
    }

    @SuppressLint("RestrictedApi")
    private void startCameraX(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build();
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(binding.surfaceView.getSurfaceProvider());
        imageCapture = new ImageCapture.Builder()
                .setDefaultResolution(new Size(lensFacing == CameraSelector.LENS_FACING_FRONT ? 2322 : 1980, lensFacing == CameraSelector.LENS_FACING_FRONT ? 3096 : 1080))
                .setMaxResolution(new Size(lensFacing == CameraSelector.LENS_FACING_FRONT ? 2322 : 1980, lensFacing == CameraSelector.LENS_FACING_FRONT ? 3096 : 1080))
                .setFlashMode(flashOn ? FLASH_MODE_ON : FLASH_MODE_OFF)
                .build();
        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
    }

    private void changeLens() {
        if (lensFacing == CameraSelector.LENS_FACING_BACK) {
            lensFacing = CameraSelector.LENS_FACING_FRONT;
        } else {
            lensFacing = CameraSelector.LENS_FACING_BACK;
        }
        startCameraX(cameraProvider);
    }

    private void capture() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, generateRandomFileName());
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        imageCapture.takePicture(new ImageCapture.OutputFileOptions.Builder(
                getContentResolver(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
        ).build(), getExecutor(), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                try (InputStream is = getContentResolver().openInputStream(outputFileResults.getSavedUri())) {
                    byte[] buffer = new byte[is.available()];
                    is.read(buffer);
                    try{
                        File myDir = createImageFile();
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        FileOutputStream fos = new FileOutputStream(myDir);
                        fos.write(buffer);
                        fos.flush();
                        fos.close();
                        Intent intent = new Intent();
                        intent.putExtra("file", myDir);
                        setResult(RESULT_OK, intent);
                        finish();
                    }catch (Exception exp){
                        exp.printStackTrace();
                    }
                }catch (Exception exp){
                    exp.printStackTrace();
                }
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {

            }
        });
    }

    private String generateRandomFileName() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        Random random = new Random();
        int randomNumber = random.nextInt(1000); // Untuk menambahkan angka acak di belakang nama file
        return "image_" + timeStamp + "_" + randomNumber + ".jpeg";
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getCacheDir();
        return File.createTempFile(imageFileName, ".png", storageDir);
    }
}

