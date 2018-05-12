package com.example.karlo.learningapplication.modules.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karlo.learningapplication.App;
import com.example.karlo.learningapplication.R;
import com.example.karlo.learningapplication.models.User;
import com.example.karlo.learningapplication.modules.gallery.GalleryActivity;
import com.example.karlo.learningapplication.modules.login.LoginActivity;
import com.example.karlo.learningapplication.modules.program.ProgramActivity;
import com.example.karlo.learningapplication.modules.search.SearchActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Karlo on 26.3.2018..
 */

public class HomeActivity extends AppCompatActivity
        implements HomeView ,
        NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.program_link)
    CardView mLinkToProgram;
    @BindView(R.id.gallery_link)
    CardView mLinkToGallery;

    //
    // Navigation
    //
    @BindView(R.id.drawer)
    DrawerLayout mDrawerLayout;

    @Inject
    HomeViewModel mViewModel;

    private ImageView mUserImage;
    private TextView mUserName;
    private TextView mUserEmail;

    private ActionBarDrawerToggle mToggle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ((App) getApplication()).getComponent().inject(this);
        setUpNavigation();
        setUpListeners();
        setUpObservers();
    }

    private void setUpObservers() {
        mViewModel.getUser().observe(this, user -> {
            if (user != null) {
                bindUser(user);
            }
        });

        mViewModel.getStatus().observe(this, status -> {
            switch(status.getResponse()) {
                case LOGOUT:
                    logOut();
                    break;
                case LOADING:
                    loadingData(status.getState());
                    break;
                case ERROR:
                    showError(new Throwable(status.getMessage()));
                    break;
            }
        });
    }

    private void setUpListeners() {
        mLinkToGallery.setOnClickListener(view -> goToGallery());
        mLinkToProgram.setOnClickListener(view -> goToProgram());
    }

    private void setUpNavigation() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        mUserImage = headerView.findViewById(R.id.user_image);
        mUserName = headerView.findViewById(R.id.user_name);
        mUserEmail = headerView.findViewById(R.id.user_email);

    }

    @Override
    protected void onResume() {
        super.onResume();
        NavigationView navigationView = findViewById(R.id.navigation_view);
        int size = navigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                goToSearch();
                mDrawerLayout.closeDrawers();
                return true;
            case R.id.logout:
                mViewModel.signOut();
                return true;
            case R.id.images:
                startActivity(new Intent(HomeActivity.this, GalleryActivity.class));
                return true;
            default:
                return false;
        }
    }

    @Override
    public void goToGallery() {
        Intent intent = new Intent(HomeActivity.this, GalleryActivity.class);
        startActivity(intent);
    }

    @Override
    public void goToProgram() {
        Intent intent = new Intent(HomeActivity.this, ProgramActivity.class);
        startActivity(intent);
    }

    @Override
    public void goToSearch() {
        Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    @Override
    public void logOut() {
        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() { }

    @Override
    public void bindUser(User user) {
        mUserName.setText(user.getDisplayName());
        mUserEmail.setText(user.getMail());
        Picasso.get()
                .load(user.getImageUrl())
                .fit()
                .centerCrop()
                .placeholder(R.drawable.ic_account)
                .into(mUserImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap imageBitmap = ((BitmapDrawable) mUserImage.getDrawable()).getBitmap();
                        RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                        imageDrawable.setCircular(true);
                        imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                        mUserImage.setImageDrawable(imageDrawable);
                    }

                    @Override
                    public void onError(Exception e) {
                        mUserImage.setImageResource(R.drawable.ic_account);

                    }
                });
    }

    @Override
    public void showError(Throwable error) {
        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadingData(boolean loading) {
        mProgressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }
}
