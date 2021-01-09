package com.example.androidcamera;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    //imagen que se muestra y nombre del archivo en el que guardo la foto
    ImageView miniatura;
    String imageName = "Image01";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView = miniatura = findViewById(R.id.imageView);

        //listener del boton y camara para hacer la foto
        Button btPhoto = findViewById(R.id.btnTakeImage);
        btPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                }
            }
        });

        //boton de cargar la imagen con setimageuri.
        Button btLoad = findViewById(R.id.btnLoadImage);
        btLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                miniatura.setImageURI(Uri.parse(getFilesDir().getPath() + "/Image01.png"));

            }
        });
    }

    //poner la foto en la miniatura y guardar la imagen en el almacenamiento interno.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            miniatura.setImageBitmap(imageBitmap);

            saveImage(imageBitmap);
        }
    }

    //guardar la foto en el almacenamiento interno.
    public void saveImage(Bitmap bitmap) {

        File imageFile = new File(getApplicationContext().getFilesDir(), imageName + ".png");
        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            Toast.makeText(getApplicationContext(), "Image saved as " + imageName , Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "ERROR: Can't save the image.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

}