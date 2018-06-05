package com.example.karlo.sstconference.modules.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.ui.FunctionalButton;
import com.example.karlo.sstconference.utility.RadiusPickerUtility;

import net.globulus.easyprefs.EasyPrefs;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.show_notifications)
    FunctionalButton mShowNotifications;
    @BindView(R.id.change_map_radius)
    FunctionalButton mMapRadius;

    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mUnbinder = ButterKnife.bind(this);
        setUpListeners();
        setUpToolbar();
    }

    private void setUpListeners() {
        mShowNotifications.setOnCheckedChangeListener((buttonView, isChecked) ->
                EasyPrefs.putShowNotifications(getApplicationContext(), isChecked));
        mShowNotifications.setChecked(EasyPrefs.getShowNotifications(this));
        mMapRadius.setOnClickListener(view -> RadiusPickerUtility.changeRadiusDialog(SettingsActivity.this, null));
    }

    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.search);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

}
