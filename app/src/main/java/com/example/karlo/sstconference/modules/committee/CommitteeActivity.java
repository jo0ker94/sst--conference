package com.example.karlo.sstconference.modules.committee;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.karlo.sstconference.App;
import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.adapters.CommitteeAdapter;
import com.example.karlo.sstconference.models.committee.CommitteeMember;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CommitteeActivity extends AppCompatActivity
        implements CommitteeView,
        CommitteeAdapter.OnItemClickListener {

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
