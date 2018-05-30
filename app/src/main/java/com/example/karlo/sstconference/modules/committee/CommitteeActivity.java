package com.example.karlo.sstconference.modules.committee;

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
import com.example.karlo.sstconference.adapters.CommitteeAdapter;
import com.example.karlo.sstconference.adapters.ConferenceChairsAdapter;
import com.example.karlo.sstconference.models.ConferenceChair;
import com.example.karlo.sstconference.models.committee.CommitteeMember;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CommitteeActivity extends AppCompatActivity
        implements CommitteeView,
        CommitteeAdapter.OnItemClickListener, ConferenceChairsAdapter.OnItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.steering_committee_recycler)
    RecyclerView mSteeringRecycler;
    @BindView(R.id.organizing_committee_recycler)
    RecyclerView mOrganizingRecycler;
    @BindView(R.id.program_committee_recycler)
    RecyclerView mProgramRecycler;
    @BindView(R.id.conference_chairs_recycler)
    RecyclerView mChairsRecycler;

    @Inject
    CommitteeViewModel mViewModel;

    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_committee);
        mUnbinder = ButterKnife.bind(this);
        ((App) getApplication()).getComponent().inject(this);
        setUpToolbar();
        setUpObservers();
    }

    private void setUpObservers() {
        mViewModel.getOrganizing().observe(this, this::showOrganizingCommittee);
        mViewModel.getProgram().observe(this, this::showProgramCommittee);
        mViewModel.getSteering().observe(this, this::showSteeringCommittee);
        mViewModel.getChairs().observe(this, this::showConferenceChairs);
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
        toolbar.setTitle(R.string.committee);
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
    public void onItemClick(View view, int position) {

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
        View view = getLayoutInflater().inflate(R.layout.conference_chair_details, null);
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
    public void onItemLongClick(View view, int position) {

    }

    @Override
    public void loadingData(boolean loading) {
        mProgressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError(Throwable error) {
        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void showConferenceChairs(List<ConferenceChair> conferenceChairs) {
        ConferenceChairsAdapter adapter = new ConferenceChairsAdapter(conferenceChairs, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mChairsRecycler.setLayoutManager(layoutManager);
        mChairsRecycler.setAdapter(adapter);

    }

    @Override
    public void showProgramCommittee(List<CommitteeMember> committeeMembers) {
        CommitteeAdapter adapter = new CommitteeAdapter(committeeMembers, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mProgramRecycler.setLayoutManager(layoutManager);
        mProgramRecycler.setAdapter(adapter);

    }

    @Override
    public void showSteeringCommittee(List<CommitteeMember> committeeMembers) {
        CommitteeAdapter adapter = new CommitteeAdapter(committeeMembers, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mSteeringRecycler.setLayoutManager(layoutManager);
        mSteeringRecycler.setAdapter(adapter);

    }

    @Override
    public void showOrganizingCommittee(List<CommitteeMember> committeeMembers) {
        CommitteeAdapter adapter = new CommitteeAdapter(committeeMembers, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mOrganizingRecycler.setLayoutManager(layoutManager);
        mOrganizingRecycler.setAdapter(adapter);

    }
}
