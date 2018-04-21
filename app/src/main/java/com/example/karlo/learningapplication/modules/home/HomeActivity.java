package com.example.karlo.learningapplication.modules.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karlo.learningapplication.R;
import com.example.karlo.learningapplication.adapters.ConferenceChairsAdapter;
import com.example.karlo.learningapplication.commons.BaseActivity;
import com.example.karlo.learningapplication.models.ConferenceChair;
import com.example.karlo.learningapplication.models.User;
import com.example.karlo.learningapplication.modules.login.LoginActivity;
import com.example.karlo.learningapplication.modules.search.SearchActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Karlo on 26.3.2018..
 */

public class HomeActivity extends BaseActivity<HomeView, HomePresenter>
        implements HomeView,
        NavigationView.OnNavigationItemSelectedListener,
        ConferenceChairsAdapter.OnItemClickListener {

    @BindView(R.id.committeeRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    //
    // Navigation
    //
    @BindView(R.id.drawer)
    DrawerLayout mDrawerLayout;

    private ImageView mUserImage;
    private TextView mUserName;
    private TextView mUserEmail;

    private ActionBarDrawerToggle mToggle;

    private ConferenceChairsAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setUpNavigation();
        presenter.fetchData();
        presenter.fetchUser();
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
                presenter.signOut();
                return true;
            default:
                return false;
        }
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
    public void showData(List<ConferenceChair> chairs) {
        mAdapter = new ConferenceChairsAdapter(chairs, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void bindUser(User user) {
        mUserName.setText(user.getDisplayName());
        mUserEmail.setText(user.getMail());
        Picasso.get()
                .load(user.getImageUrl())
                .resize(100, 100)
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

    @NonNull
    @Override
    public HomePresenter createPresenter() {
        return new HomePresenter();
    }

    @Override
    public void attachView() {
        presenter.attachView(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        showChairDetails(mAdapter.getItem(position));
    }

    @Override
    public void openMailDialog(String mail) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "SST 2017");
        intent.setData(Uri.parse("mailto:" + mail));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void openDialDialog(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    private void showChairDetails(ConferenceChair chair) {
        View view = getLayoutInflater().inflate(R.layout.conference_chair_details, null);
        ImageView chairImage = view.findViewById(R.id.chairImageView);
        TextView chairTitle = view.findViewById(R.id.tvTitle);
        TextView chairName = view.findViewById(R.id.tvName);
        TextView chairEmail = view.findViewById(R.id.tvEmail);
        TextView chairPhone = view.findViewById(R.id.tvPhone);
        TextView chairFacility = view.findViewById(R.id.tvFacility);
        Picasso.get()
                .load(chair.getImageUrl())
                .resize(250, 350)
                .centerCrop()
                .placeholder(R.drawable.no_img)
                .into(chairImage);
        chairTitle.setText(chair.getChairTitle());
        chairName.setText(chair.getName());
        chairEmail.setText(chair.getEmail());
        chairPhone.setText(chair.getPhoneNumber());
        chairFacility.setText(chair.getFacility());

        chairEmail.setPaintFlags(chairEmail.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        chairPhone.setPaintFlags(chairPhone.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        chairEmail.setOnClickListener(emailView -> openMailDialog(chair.getEmail()));
        chairPhone.setOnClickListener(phoneView -> openDialDialog(chair.getPhoneNumber()));

        new AlertDialog.Builder(this)
                .setView(view)
                .show();
    }
}
