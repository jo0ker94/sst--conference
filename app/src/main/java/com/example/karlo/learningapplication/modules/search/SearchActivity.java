package com.example.karlo.learningapplication.modules.search;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.karlo.learningapplication.R;
import com.example.karlo.learningapplication.adapters.WikiResultRecyclerAdapter;
import com.example.karlo.learningapplication.commons.BaseActivity;
import com.example.karlo.learningapplication.models.wiki.WikiResult;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Karlo on 31.3.2018..
 */

public class SearchActivity extends BaseActivity<SearchView, SearchPresenter> implements SearchView {

    @BindView(R.id.searchListView)
    RecyclerView mRecyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setUpToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.searchMenu);
        android.widget.SearchView searchView = (android.widget.SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s.length() > 2) {
                    presenter.searchWiki(s);
                } else {
                    if (!searchView.isIconified()) {
                        searchView.setIconified(true);
                    }
                    item.collapseActionView();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.search);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void showNoResult() {

    }

    @Override
    public void showResult(WikiResult result) {
        WikiResultRecyclerAdapter adapter = new WikiResultRecyclerAdapter(result.getItems(), (view, position) ->
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(result.getItems().get(position).getLink())))
        );
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void loadingData(boolean loading) {
        mProgressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError(Throwable error) {
        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @NonNull
    @Override
    public SearchPresenter createPresenter() {
        return new SearchPresenter(this);
    }

}
