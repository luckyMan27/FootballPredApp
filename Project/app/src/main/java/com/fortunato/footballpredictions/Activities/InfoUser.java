package com.fortunato.footballpredictions.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

    private ProgressBar progBar = null;

    private Bitmap userBitmap;

    private LoadImage loadImage = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_user);

        userAuth = FirebaseAuth.getInstance();
        progBar = findViewById(R.id.progBarUser);
        name_user = findViewById(R.id.textInfoUserName);
        email_user = findViewById(R.id.textInfoUserEmail);
        userImg = findViewById(R.id.img_info_user);

        if(userAuth == null) return;


        if(userAuth.getCurrentUser().getPhotoUrl() != null){
            loadImage = new LoadImage(userAuth.getCurrentUser().getPhotoUrl().toString(), null, InfoUser.this);
            loadImage.start();
            Log.i("prova", "image");
        }

        if (loadImage != null) {
            progBar.setVisibility(View.VISIBLE);
            AsyncTaskRunner runner = new AsyncTaskRunner();
            runner.execute();
        }
        name = "User Name: \n\n" + userAuth.getCurrentUser().getDisplayName();
        email = "User Email: \n\n" + userAuth.getCurrentUser().getEmail();
        name_user.setText(name);
        email_user.setText(email);

    }

    public void setUserBitmap(Bitmap userBitmap) {
        this.userBitmap = userBitmap;
    }

    private class AsyncTaskRunner extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPostExecute(Void result) {
            // execution of result of Long time consuming operation
            progBar.setVisibility(View.GONE);
            userImg.setImageBitmap(userBitmap);
        }


        @Override
        protected Void doInBackground(Void... voids) {
            try {
                loadImage.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }
}
