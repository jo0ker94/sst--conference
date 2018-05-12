package com.example.karlo.learningapplication.modules.program;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.karlo.learningapplication.R;

import butterknife.BindView;

public class ProgramActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program);
        setUpToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.conference_program);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /*@Override
    public void onItemClick(View view, int position) {
        //showChairDetails(mAdapter.getItem(position));
        presenter.fetchTopics(position);
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
    public void showData(List<ConferenceChair> chairs) {
        //mAdapter = new ConferenceChairsAdapter(chairs, this);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //mRecyclerView.setLayoutManager(layoutManager);
        //mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void showTracks(List<Track> tracks) {
        //TrackAdapter adapter = new TrackAdapter(tracks, this);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //mRecyclerView.setLayoutManager(layoutManager);
        //mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showTopics(List<Topic> topics) {
        //TopicAdapter adapter = new TopicAdapter(topics, null);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //mRecyclerView.setLayoutManager(layoutManager);
        //mRecyclerView.setAdapter(adapter);
    }*/
}
