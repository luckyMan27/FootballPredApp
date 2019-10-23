package com.fortunato.footballpredictions.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.fortunato.footballpredictions.Fragments.FavoriteFragment;
import com.fortunato.footballpredictions.Fragments.HomeFragment;
import com.fortunato.footballpredictions.Fragments.LiveFragment;
import com.fortunato.footballpredictions.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer = null;

    private static final String FRAGMENT_USED = "selectedFragment";
    private Fragment selectedFragment = null;
    private HomeFragment home_frag = null;
    private FavoriteFragment fav_frag = null;
    private LiveFragment live_frag = null;

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

        if(savedInstanceState != null){
            selectedFragment = getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_USED);
            if(selectedFragment != null){
                if(selectedFragment instanceof HomeFragment){
                    home_frag = (HomeFragment)selectedFragment;
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, home_frag).commit();
                    navigationView.setCheckedItem(R.id.nav_home);
                }
                if(selectedFragment instanceof FavoriteFragment) {
                    fav_frag = (FavoriteFragment)selectedFragment;
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fav_frag).commit();
                    navigationView.setCheckedItem(R.id.nav_favorites);
                }
                if(selectedFragment instanceof LiveFragment) {
                    live_frag = (LiveFragment)selectedFragment;
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, live_frag).commit();
                    navigationView.setCheckedItem(R.id.nav_live);
                }
            }
        } else {
            fav_frag = new FavoriteFragment();
            selectedFragment = fav_frag;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fav_frag).commit();
            navigationView.setCheckedItem(R.id.nav_favorites);
        }
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
        int id = item.getItemId();

        if (id == R.id.bookBets) {
            return true;
        }

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
                Intent intent = new Intent(this, AccountActivity.class);
                startActivity(intent);
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
