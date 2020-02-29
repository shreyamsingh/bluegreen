package com.example.maptest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.net.URI;

public class DisplayActivity extends AppCompatActivity {
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        try {
            Intent intent = getIntent();
            Uri imgUri = MainActivity.getURI().get(Integer.parseInt(MapsActivity.EXTRA_MESSAGE));
            img.setImageURI(android.net.Uri.parse(imgUri.toString()));
        }
        catch (Exception e) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}
