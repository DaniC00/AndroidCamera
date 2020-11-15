package com.example.androidcamera;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.Toast;
import android.content.Intent;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.view.View;
import android.os.Bundle;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;


public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private EditText editLoadDialog;
    ImageView imgv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editLoadDialog = new EditText(this);

        Button btPhoto = findViewById(R.id.btCamera);
        btPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                try {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

                } catch (ActivityNotFoundException e) {
                    System.out.println("Error");
                }
            }
        });


        Button btLoad = findViewById(R.id.btLoad);
        btLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadDialog();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap captureImage = (Bitmap)data.getExtras().get("data");

            imgv = findViewById(R.id.imgView);
            imgv.setImageBitmap(captureImage);
            saveImage(captureImage, "image", 0);
        }
    }


    private AlertDialog makeDialog(String title, String message, EditText et, final String cancelMessage) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle(title);
        adb.setMessage(message);
        adb.setView(et);

        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (cancelMessage != null)
                {
                    Toast.makeText(getApplicationContext(),cancelMessage, Toast.LENGTH_LONG).show();
                }

            }
        });

        AlertDialog ad = adb.create();
        ad.setCanceledOnTouchOutside(false);
        return ad;

    }

    private void showLoadDialog()
    {
        editLoadDialog = new EditText(this);
        final AlertDialog alertd = makeDialog("Load Image", "Image name: ", editLoadDialog,null);
        alertd.show();

        alertd.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value =  editLoadDialog.getText().toString();
                try {
                    loadImage(value);
                    Toast.makeText(getApplicationContext(), "Loading image " + value, Toast.LENGTH_LONG).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "ERROR: The image doesn't exist.",Toast.LENGTH_LONG).show();
                }
                alertd.dismiss();
            }
        });
    }

    public void saveImage(Bitmap bp, String name, int i) {

        File imageFile = new File(getApplicationContext().getFilesDir(), name + i + ".png");
        if (!imageFile.exists())
        {
            try {
                FileOutputStream fos = new FileOutputStream(imageFile);
                bp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
                Toast.makeText(getApplicationContext(), "Image saved as " + name + i, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "ERROR: Can't save the image.", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            i++;
            saveImage(bp, name, i);
        }

    }

    private void loadImage(String imageName) throws FileNotFoundException {
        imgv = findViewById(R.id.imgView);
        imgv.setImageBitmap(getBitmap(imageName + ".png", getApplicationContext().getFilesDir()));
    }

    private Bitmap getBitmap(String filename, File dirPath) throws FileNotFoundException {
        File bitFile = new File(dirPath.getAbsolutePath(), filename);
        Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(bitFile));
        return bitmap;
    }

}