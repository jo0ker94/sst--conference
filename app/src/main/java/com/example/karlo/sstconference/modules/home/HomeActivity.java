package com.example.karlo.sstconference.modules.home;

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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karlo.sstconference.App;
import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.models.User;
import com.example.karlo.sstconference.modules.committee.CommitteeActivity;
import com.example.karlo.sstconference.modules.gallery.GalleryActivity;
import com.example.karlo.sstconference.modules.keynotespeakers.KeynoteActivity;
import com.example.karlo.sstconference.modules.login.LoginActivity;
import com.example.karlo.sstconference.modules.program.ProgramActivity;
import com.example.karlo.sstconference.modules.search.SearchActivity;
import com.example.karlo.sstconference.modules.subscribed.SubscriptionActivity;
import com.example.karlo.sstconference.modules.venue.VenueActivity;
import com.example.karlo.sstconference.receivers.EventAlarmReceiver;
import com.example.karlo.sstconference.utility.AlarmUtility;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

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
    @BindView(R.id.subscribed_link)
    CardView mLinkToSubscribed;
    @BindView(R.id.venue_link)
    CardView mLinkToVenue;

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

    private Unbinder mUnbinder;
    private ActionBarDrawerToggle mToggle;

    private User mUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);
        ((App) getApplication()).getComponent().inject(this);
        setUpNavigation();
        setUpListeners();
        setUpObservers();
    }

    private void setUpObservers() {
        mViewModel.getUser().observe(this, user -> {
            if (user != null) {
                mUser = user;
                bindUser(user);
                //if (!user.getSubscribedEvents().isEmpty()) {
                //    setUpReminders(user.getSubscribedEvents());
                //}
            }
        });

        mViewModel.getStatus().observe(this, status -> {
            switch(status.getResponse()) {
                case LOGOUT:
                    logOut();
                    cancelReminders(mUser.getSubscribedEvents());
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

    private void cancelReminders(List<Integer> subscribedEvents) {
        for (Integer integer : subscribedEvents) {
            AlarmUtility.cancelAlarm(this, integer, EventAlarmReceiver.class, null);
        }
    }

    private void setUpReminders(List<Integer> subscribedEvents) {
        for (Integer integer : subscribedEvents) {
            AlarmUtility.scheduleAlarm(this, Calendar.getInstance(), integer, EventAlarmReceiver.class);
        }
    }

    private void setUpListeners() {
        mLinkToGallery.setOnClickListener(view -> goToGallery());
        mLinkToProgram.setOnClickListener(view -> goToProgram());
        mLinkToSubscribed.setOnClickListener(view -> goToSubscribed());
        mLinkToVenue.setOnClickListener(view -> goToVenue());
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
            case R.id.committee:
                startActivity(new Intent(HomeActivity.this, CommitteeActivity.class));
                return true;
            case R.id.images:
                goToGallery();
                return true;
            case R.id.events:
                goToProgram();
                return true;
            case R.id.venue:
                goToVenue();
                return true;
            case R.id.speakers:
                startActivity(new Intent(HomeActivity.this, KeynoteActivity.class));
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
    public void goToSubscribed() {
        Intent intent = new Intent(HomeActivity.this, SubscriptionActivity.class);
        startActivity(intent);
    }

    @Override
    public void goToVenue() {
        Intent intent = new Intent(HomeActivity.this, VenueActivity.class);
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
    public void onBackPressed() {
        showExitDialog();
    }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    private void showExitDialog() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.sure_you_want_to_quit)
                .setPositiveButton(R.string.yes, (dialogInterface, i) -> finish())
                .setNegativeButton(R.string.no, null)
                .show();
    }
}
