package com.ikvych.cocktail.service;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ikvych.cocktail.model.Drink;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageLoadService {

    private Application application;

    public ImageLoadService(Application application) {
        this.application = application;
    }

    public void downloadImageAndSaveNewPath(Drink drink, Object lock) {
        Glide.with(application)
                .asBitmap()
                .load(drink.getStrDrinkThumb())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        String newPath = saveImage(resource);
                        if (newPath != null) {
                            drink.setStrDrinkThumb(newPath);
                        }
                        if (lock != null) {
                            synchronized (lock) {
                                lock.notify();
                            }
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }

                });
    }

    private String saveImage(Bitmap image) {
        String savedImagePath = null;

        String imageFileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";
        File storageDir = application.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        boolean success = storageDir != null;
        if (success && !storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return savedImagePath;
    }
}
