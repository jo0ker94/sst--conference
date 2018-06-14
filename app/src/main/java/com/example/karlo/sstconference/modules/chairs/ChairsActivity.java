package com.example.karlo.sstconference.modules.chairs;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karlo.sstconference.App;
import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.adapters.ConferenceChairsAdapter;
import com.example.karlo.sstconference.models.ConferenceChair;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ChairsActivity extends AppCompatActivity
        implements ChairsView,
        ConferenceChairsAdapter.OnItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.conference_chairs_recycler)
    RecyclerView mChairsRecycler;

    @Inject
    ChairsViewModel mViewModel;

    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chairs);
        mUnbinder = ButterKnife.bind(this);
        ((App) getApplication()).getComponent().inject(this);
        setUpToolbar();
        setUpObservers();
    }

    private void setUpObservers() {
        mViewModel.getChairs().observe(this, this::showConferenceChairs);
        mViewModel.getStatus().observe(this, status -> {
            switch(status.getResponse()) {
                case LOADING:
                    loadingData(status.getState());
                    break;
                case ERROR:
                    showError(new Throwable(status.getMessage()));
                    break;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.conference_chairs);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    @Override
    public void showConferenceChairs(List<ConferenceChair> conferenceChairs) {
        ConferenceChairsAdapter adapter = new ConferenceChairsAdapter(conferenceChairs, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mChairsRecycler.setLayoutManager(layoutManager);
        mChairsRecycler.setAdapter(adapter);

    }

    @Override
    public void onItemClick(ConferenceChair conferenceChair) {
        showChairDetails(conferenceChair);
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
        View view = View.inflate(this, R.layout.conference_chair_details, null);
        ImageView chairImage = view.findViewById(R.id.chairImageView);
        TextView chairTitle = view.findViewById(R.id.tvTitle);
        TextView chairName = view.findViewById(R.id.tvName);
        TextView chairEmail = view.findViewById(R.id.tvEmail);
        TextView chairPhone = view.findViewById(R.id.tvPhone);
        TextView chairFacility = view.findViewById(R.id.tvFacility);
        Picasso.get()
                .load(chair.getImageUrl())
                .fit()
                .centerInside()
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

    @Override
    public void loadingData(boolean loading) {
        mProgressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError(Throwable error) {
        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
    }
}