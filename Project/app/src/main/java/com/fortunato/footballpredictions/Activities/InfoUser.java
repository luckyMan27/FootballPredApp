package com.fortunato.footballpredictions.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.fortunato.footballpredictions.Networks.LoadImage;
import com.fortunato.footballpredictions.R;
import com.google.firebase.auth.FirebaseAuth;

public class InfoUser extends AppCompatActivity {

    private FirebaseAuth userAuth;

    private String name;
    private String email;

    private TextView name_user;
    private TextView email_user;

    private ImageView userImg;

    private Bitmap userBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_user);

        userAuth = FirebaseAuth.getInstance();

        name_user = findViewById(R.id.textInfoUserName);
        email_user = findViewById(R.id.textInfoUserEmail);
        userImg = findViewById(R.id.img_info_user);

        if(userAuth == null) return;

        LoadImage loadImage = null;
        if(userAuth.getCurrentUser().getPhotoUrl() != null){
            loadImage = new LoadImage(userAuth.getCurrentUser().getPhotoUrl().toString(), null, InfoUser.this);
            loadImage.start();
            Log.i("prova", "image");
        }
        try {
            if (loadImage != null) {
                loadImage.join();
                userImg.setImageBitmap(userBitmap);
            }
            name = "User Name: \n\n" + userAuth.getCurrentUser().getDisplayName();
            email = "User Email: \n\n" + userAuth.getCurrentUser().getEmail();
            name_user.setText(name);
            email_user.setText(email);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setUserBitmap(Bitmap userBitmap) {
        this.userBitmap = userBitmap;
    }
}
