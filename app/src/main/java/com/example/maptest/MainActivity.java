package com.example.maptest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
        public static final String EXTRA_MESSAGE = "com.example.blueblue.MESSAGE";
        public static final ArrayList<String> s = new ArrayList<String>();
        ImageView imageView;
        TextView textView;
        static Uri selectedImage;
        InputStream in;
        String loc = "";
        String arr = "";
        static String path = "";
        static ArrayList<String> datetime = new ArrayList<String>();
        static ArrayList<Uri> uris = new ArrayList<Uri>();
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            Button btnCamera = (Button)findViewById(R.id.btnCamera);
            btnCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 0);
                }
            });
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            //Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            //imageView.setImageBitmap(bitmap);
            selectedImage = data.getData();
            uris.add(selectedImage);
            path = selectedImage.getPath();
            try {
                in = getContentResolver().openInputStream(selectedImage);
                ExifInterface exifInterface = new ExifInterface(in);
                String longref = "";
                String latref ="";
                longref = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
                latref =  exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
                datetime.add(exifInterface.getAttribute(ExifInterface.TAG_DATETIME));
                System.out.println(datetime.get(0));
                if(latref.equals("S"))
                    loc += "-" + convertToDegree(exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE)) + " ";
                else
                    loc += "" + convertToDegree(exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE)) + " ";
                if(longref.equals("W"))
                    loc += "-" + convertToDegree(exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)) + " ";
                else
                    loc += "" + convertToDegree(exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)) + " ";
                s.add(loc);
                for(int i = 0; i < s.size(); i++)
                    arr += s.get(i);
            }
            catch(Exception e) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }


        }

        private Double convertToDegree(String stringDMS) {
            Double result = null;
            String[] DMS = stringDMS.split(",", 3);

            String[] stringD = DMS[0].split("/", 2);
            Double D0 = new Double(stringD[0]);
            Double D1 = new Double(stringD[1]);
            Double FloatD = D0 / D1;

            String[] stringM = DMS[1].split("/", 2);
            Double M0 = new Double(stringM[0]);
            Double M1 = new Double(stringM[1]);
            Double FloatM = M0 / M1;

            String[] stringS = DMS[2].split("/", 2);
            Double S0 = new Double(stringS[0]);
            Double S1 = new Double(stringS[1]);
            Double FloatS = S0 / S1;

            result = new Double(FloatD + (FloatM / 60) + (FloatS / 3600));

            return result;

        }

        public void goToMap(View view) {
            try {
                Intent intent = new Intent(this, MapsActivity.class);
                intent.putExtra(EXTRA_MESSAGE, loc);
                startActivity(intent);
            }
            catch (Exception e) {
                System.out.println("hihihhihihihihi");
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }

        public static String getPath() {
            return path;
        }
        public static ArrayList<String> getDate() {
            System.out.println(datetime.get(0));
            return datetime;
        }
        public static ArrayList<Uri> getURI() {
            return uris;
        }
        public String getRealPathFromURI(Uri uri) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }