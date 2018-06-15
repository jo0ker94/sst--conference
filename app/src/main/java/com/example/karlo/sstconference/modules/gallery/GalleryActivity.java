package com.example.karlo.sstconference.modules.gallery;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karlo.sstconference.App;
import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.base.BaseView;
import com.example.karlo.sstconference.commons.Constants;
import com.example.karlo.sstconference.models.Image;
import com.example.karlo.sstconference.modules.login.LoginActivity;
import com.example.karlo.sstconference.pager.CardFragmentPagerAdapter;
import com.example.karlo.sstconference.pager.ShadowTransformer;
import com.example.karlo.sstconference.utility.AppConfig;
import com.example.karlo.sstconference.utility.NetworkUtility;

import net.globulus.easyprefs.EasyPrefs;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;

public class GalleryActivity extends AppCompatActivity
        implements BaseView,
        GalleryFeedAdapter.OnItemClickListener {

    @BindView(R.id.imageListView)
    RecyclerView mRecyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.emptyData)
    TextView mEmptyData;

    @Inject
    GalleryViewModel mViewModel;

    private GalleryFeedAdapter mAdapter;
    private ProgressDialog mProgressDialog;

    private Uri filePath;
    private List<Image> mItems = new ArrayList<>();

    Unbinder mUnbinder;
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        mUnbinder = ButterKnife.bind(this);
        ((App) getApplication()).getComponent().inject(this);
        setUpToolbar();
        mProgressDialog = new ProgressDialog(this);
        mProgressBar.setVisibility(View.VISIBLE);
        mEmptyData.setVisibility(View.GONE);
        setUpObservers();
    }

    private void setUpObservers() {
        mViewModel.getImages().observe(this, images -> {
            mProgressBar.setVisibility(View.GONE);
            if (images != null && !images.isEmpty()) {
                boolean hasData = !mItems.isEmpty();
                mItems.clear();
                mItems.addAll(images);
                showImages(hasData);
                mEmptyData.setVisibility(View.GONE);
            } else {
                mEmptyData.setVisibility(View.VISIBLE);
                mEmptyData.setOnClickListener(view -> takePicture());
            }
        });

        mViewModel.getStatus().observe(this, status -> {
            switch (status.getResponse()) {
                case PROGRESS:
                    mProgressDialog.setMessage(String.format(getString(R.string.upload_process), status.getInteger()));
                    break;
                case MESSAGE:
                case ERROR:
                    showError(new Throwable(status.getMessage()));
                    mProgressDialog.dismiss();
                    break;
                case LOADING:
                    loadingData(status.getState());
                    break;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
        mUnbinder.unbind();
    }

    private void showImages(boolean hasData) {
        if (hasData) {
            mAdapter.notifyDataSetChanged();
        } else {
            mAdapter = new GalleryFeedAdapter(mItems, this);
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(getSpanCount(), StaggeredGridLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    private int getSpanCount() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        float minElements = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, metrics);
        return (int) (width/minElements);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.image_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.imageUpload:
                if (hasNetworkConnection() && userLoggedIn()) {
                    chooseImage();
                }
                return true;
            case R.id.takeImage:
                if (hasNetworkConnection() && userLoggedIn()) {
                    takePicture();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean userLoggedIn() {
        if (!EasyPrefs.getGuestMode(this)) {
            return true;
        } else {
            Snackbar.make(mRecyclerView, R.string.only_for_logged_in, Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.login).toUpperCase(Locale.getDefault()), view -> {
                        EasyPrefs.putGuestMode(this, false);
                        startActivity(new Intent(GalleryActivity.this, LoginActivity.class));
                    })
                    .show();
            return false;
        }
    }

    private boolean hasNetworkConnection() {
        if (NetworkUtility.hasNetworkConnection(this)) {
            return true;
        } else {
            NetworkUtility.showNoNetworkDialog(this);
            return false;
        }
    }

    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.gallery);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), Constants.PICK_IMAGE_REQUEST);
    }

    private void takePicture() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, Constants.TAKE_IMAGE_REQUEST);
    }

    private void showDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.image_upload)
                .setMessage(R.string.image_upload_message)
                .setPositiveButton(R.string.upload, (dialogInterface, i) -> uploadImage())
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null ) {
            filePath = data.getData();
            showDialog();
        } else if (requestCode == Constants.TAKE_IMAGE_REQUEST && resultCode == RESULT_OK) {
            filePath = data.getData();
            showDialog();
        }
    }

    private void uploadImage() {
        if (filePath != null) {
            mProgressDialog.setTitle(R.string.uploading);
            mProgressDialog.show();
            mViewModel.uploadImage(filePath);
        }
    }

    @Override
    public void onItemClick(View view, int position) {

        List<GalleryCardFragment> cards = new ArrayList<>();
        for (int i = 0; i < mItems.size(); i++) {
            GalleryCardFragment cardFragment = new GalleryCardFragment();
            Bundle args = new Bundle();
            args.putParcelable(Constants.DATA, mItems.get(i));
            cardFragment.setArguments(args);
            cards.add(cardFragment);
        }

        CardFragmentPagerAdapter pagerAdapter = new CardFragmentPagerAdapter<>(getSupportFragmentManager(), dpToPixels(2), cards);
        ShadowTransformer fragmentCardShadowTransformer = new ShadowTransformer(mViewPager, pagerAdapter);
        fragmentCardShadowTransformer.enableScaling(true);

        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(position);
        mViewPager.setPageTransformer(false, fragmentCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(3);

        mViewPager.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    public float dpToPixels(int dp) {
        return dp * getResources().getDisplayMetrics().density;
    }

    @Override
    public void onBackPressed() {
        if (mRecyclerView.getVisibility() == View.VISIBLE) {
            super.onBackPressed();

        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.INVISIBLE);
        }
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
