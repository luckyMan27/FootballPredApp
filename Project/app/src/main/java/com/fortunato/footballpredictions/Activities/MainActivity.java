package com.fortunato.footballpredictions.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.fortunato.footballpredictions.Fragments.FavoriteFragment;
import com.fortunato.footballpredictions.Fragments.HomeFragment;
import com.fortunato.footballpredictions.Fragments.LiveFragment;
import com.fortunato.footballpredictions.Networks.LoadImage;
import com.fortunato.footballpredictions.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer = null;

    private static final String FRAGMENT_USED = "selectedFragment";
    private Fragment selectedFragment = null;
    private HomeFragment home_frag = null;
    private FavoriteFragment fav_frag = null;
    private LiveFragment live_frag = null;

    private static final int MY_REQUEST_CODE = 7777;

    private List<AuthUI.IdpConfig> providers;
    private Button loginB;
    private Button logoutB;
    private FirebaseUser user;
    private Bitmap userBitmap;
    private TextView userEmail = null;
    private TextView userName = null;
    private ImageView userImg = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Debug", Thread.currentThread().getName());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        userEmail = navigationView.getHeaderView(0).findViewById(R.id.userEmail);
        userName = navigationView.getHeaderView(0).findViewById(R.id.userName);
        userImg = navigationView.getHeaderView(0).findViewById(R.id.userImg);


        logoutB = navigationView.getHeaderView(0).findViewById(R.id.logoutButton);
        //loginB = findViewById(R.id.loginButton);
        loginB = navigationView.getHeaderView(0).findViewById(R.id.loginButton);
        Log.d("Debug-main", loginB.toString());
        loginB.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showSigninOptions();
            }
        });

        logoutB.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                user = null;
                logoutUser();
            }
        });

        if(savedInstanceState != null){
            selectedFragment = getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_USED);
            if(selectedFragment != null){
                if(selectedFragment instanceof HomeFragment){
                    home_frag = (HomeFragment)selectedFragment;
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, home_frag)
                            .commit();
                    navigationView.setCheckedItem(R.id.nav_home);
                }
                if(selectedFragment instanceof FavoriteFragment) {
                    fav_frag = (FavoriteFragment)selectedFragment;
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, fav_frag)
                            .commit();
                    navigationView.setCheckedItem(R.id.nav_favorites);
                }
                if(selectedFragment instanceof LiveFragment) {
                    live_frag = (LiveFragment)selectedFragment;
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, live_frag)
                            .commit();
                    navigationView.setCheckedItem(R.id.nav_live);
                }
            }
        } else {
            live_frag = new LiveFragment();
            selectedFragment = live_frag;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment).commit();
            navigationView.setCheckedItem(R.id.nav_live);
        }
    }

    private void logoutUser() {
        logoutB.setEnabled(false);
        logoutB.setVisibility(View.INVISIBLE);
        loginB.setEnabled(true);
        loginB.setVisibility(View.VISIBLE);
        userName.setText("No name");
        userEmail.setText("No email");
        userImg.setImageResource(R.mipmap.ic_launcher);
    }

    private void showSigninOptions() {
        providers = Arrays.asList(
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        if(user == null)
            startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.MyTheme)
                        .build(), MY_REQUEST_CODE);
        else Toast.makeText(MainActivity.this, "Already Logged!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MY_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                user = FirebaseAuth.getInstance().getCurrentUser();
                showUserInfos();
                Toast.makeText(this, user.getDisplayName(), Toast.LENGTH_SHORT).show();
                loginB.setEnabled(false);
                loginB.setVisibility(View.INVISIBLE);
                logoutB.setEnabled(true);
                logoutB.setVisibility(View.VISIBLE);
            } else {
                IdpResponse response = IdpResponse.fromResultIntent(data);
                if(response!=null) Toast.makeText(this, response.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showUserInfos() {
        LoadImage loadImage = new LoadImage(user.getPhotoUrl().toString(), null, MainActivity.this);
        loadImage.start();
        try {
            loadImage.join();
            userEmail.setText(user.getEmail());
            userName.setText(user.getDisplayName());
            userImg.setImageBitmap(userBitmap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setUserBitmap(Bitmap userBitmap) {
        this.userBitmap = userBitmap;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, FRAGMENT_USED, selectedFragment);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()){
                        case R.id.nav_home:
                            if(home_frag == null) home_frag = new HomeFragment();
                            selectedFragment = home_frag;
                            break;
                        case R.id.nav_favorites:
                            if(fav_frag == null) fav_frag = new FavoriteFragment();
                            selectedFragment = fav_frag;
                            break;
                        case R.id.nav_live:
                            if(live_frag == null) live_frag = new LiveFragment();
                            selectedFragment = live_frag;
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            default:
                break;
            case R.id.draw_menu_account:
                showSigninOptions();
                break;
            case R.id.draw_menu_share:
                Toast.makeText(this, "Share", Toast.LENGTH_LONG).show();
                break;
            case R.id.draw_menu_bets:
                Toast.makeText(this, "Bets", Toast.LENGTH_LONG).show();
                break;
            case R.id.draw_menu_info:
                Toast.makeText(this, "Informations", Toast.LENGTH_LONG).show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
