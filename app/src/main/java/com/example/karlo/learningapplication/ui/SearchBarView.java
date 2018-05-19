package com.example.karlo.learningapplication.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.karlo.learningapplication.Animations;
import com.example.karlo.learningapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karlo on 21.12.2017..
 */

public class SearchBarView extends LinearLayout {

    private Context mContext;
    private EditText mSearchEditText;
    private ImageView mSearchIconImage;
    private ImageView mSearchClearIcon;
    private ImageView mSearchBackIcon;
    private boolean mIsButton;
    private boolean mInSearchMode = false;

    private TextChangedListener mTextChangeListener;
    private FocusChangedListener mFocusChangedListener;
    private SearchBarListener mSearchBarListener;

    public interface TextChangedListener {
        void textChanged(String text);
        void afterTextChanged(String text);
    }

    public interface FocusChangedListener {
        void focusChanged(boolean isFocused);
    }

    public interface SearchBarListener {
        void onBackPressed();
        void onSearchButtonPressed();
    }

    public SearchBarView(Context context) {
        super(context);
        mContext = context;
        initView(null);
    }

    public SearchBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView(attrs);
    }

    public SearchBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView(attrs);
    }

    public SearchBarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        initView(attrs);
    }

    private void initView(AttributeSet attr) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_search_bar, this, true);

        mSearchEditText = (EditText) findViewById(R.id.search_edit_text);
        mSearchBackIcon = (ImageView) findViewById(R.id.search_back_icon);
        mSearchClearIcon = (ImageView) findViewById(R.id.search_clear_image);
        mSearchIconImage = (ImageView) findViewById(R.id.search_icon_image);

        setUpListeners();

        String hintText = null;
        if (attr != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(attr, R.styleable.SearchBarView, 0, 0);
            try {
                hintText = a.getString(R.styleable.SearchBarView_hintText);
            } finally {
                a.recycle();
            }
        }
        mSearchEditText.setHint(hintText != null ? hintText : mContext.getString(R.string.search));
    }

    private void setUpListeners() {
        mSearchClearIcon.setOnClickListener(view -> clearSearchBox());
        mSearchBackIcon.setOnClickListener(view -> closeSearchBox());

        mSearchEditText.addTextChangedListener(new TextWatcher());
        mSearchEditText.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(mSearchEditText);
            }
            if (!hasFocus && mSearchEditText.getText().length() == 0 && !mInSearchMode) {
                mSearchIconImage.setVisibility(View.VISIBLE);
                mSearchBackIcon.setVisibility(View.INVISIBLE);

            } else {
                mSearchIconImage.setVisibility(View.INVISIBLE);
                mSearchBackIcon.setVisibility(View.VISIBLE);
            }
            if (mFocusChangedListener != null) {
                mFocusChangedListener.focusChanged(hasFocus);
            }
        });
        mSearchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (mSearchBarListener != null) {
                    mSearchBarListener.onSearchButtonPressed();
                }
                hideKeyboard(mSearchEditText);
                return true;
            }
            return false;
        });
    }

    public void setText(String text) {
        mSearchEditText.setText(text);
    }

    public String getText() {
        return mSearchEditText.getText().toString();
    }

    private void clearSearchBox() {
        mSearchEditText.setText("");
    }

    private void closeSearchBox() {
        resetSearchBar();
        if (mSearchBarListener != null) {
            mSearchBarListener.onBackPressed();
        }
    }

    public void showSearchBar(Toolbar toolbar) {
        setEnabled(true);
        toolbar.setAnimation(Animations.outToLeftAnimation());
        toolbar.setVisibility(View.GONE);
        setVisibility(View.VISIBLE);
        setAnimation(Animations.inFromRightAnimation());
        if(requestFocus()) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        }
    }

    public void hideSearchBar(Toolbar toolbar) {
        setEnabled(false);
        resetSearchBar();
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setAnimation(Animations.inFromLeftAnimation());
        setAnimation(Animations.outToRightAnimation());
        setVisibility(View.GONE);
        hideKeyboard(getFocusedChild());
    }

    public void setOnEditorActionListener(TextView.OnEditorActionListener listener) {
        mSearchEditText.setOnEditorActionListener(listener);
    }

    public List<String>  filterArrayByText(List<String> items, String text) {
        String[] filterWords = text.toUpperCase().split(" ");
        List<String> filteredResults = new ArrayList<>();

        //for(String item : items) {
        //    if (!item.isDivider()) {
        //        String[] itemWords = item.getMedicationFromItem().getName().toUpperCase().split(" ");
        //        for(String filterWord : filterWords) {
        //            if (Stream.of(itemWords).anyMatch(word -> word.startsWith(filterWord))) {
        //                filteredMedications.add(item.getMedicationFromItem());
        //                break;
        //            }
        //        }
        //    }
        //}
        return filteredResults;
    }

    private void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void setOnTextChangedListener(TextChangedListener listener) {
        mTextChangeListener = listener;
    }

    public void setSearchBarListener(SearchBarListener listener) {
        mSearchBarListener = listener;
    }

    private class TextWatcher implements android.text.TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (mTextChangeListener != null) {
                mTextChangeListener.textChanged(charSequence.toString());
            }
            int textLength = charSequence.length();
            mSearchClearIcon.setVisibility(textLength > 0 ? View.VISIBLE : View.INVISIBLE);
            if (textLength > 0) {
                mSearchIconImage.setVisibility(View.INVISIBLE);
                mSearchBackIcon.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (mTextChangeListener != null) {
                mTextChangeListener.afterTextChanged(editable.toString());
            }
        }
    }

    public void setFocusChangedListener(FocusChangedListener focusChangedListener) {
        mFocusChangedListener = focusChangedListener;
    }

    /**
     * If true, SearchBarView acts as a button. When the view is clicked, it will fire the
     * OnClickListener. Touch events will not be propagated to its children (ie, EditText).
     * @param isButton
     */
    public void setToButtonMode(boolean isButton) {
        mIsButton = isButton;
    }

    GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return super.onSingleTapConfirmed(e);
        }
    };

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mIsButton) {
            mInSearchMode = true;
            gestureListener.onSingleTapConfirmed(ev);
            return true;

        } else {
            return false;
        }
    }

    public void resetSearchBar() {
        mInSearchMode = false;
        clearSearchBox();
        mSearchIconImage.setVisibility(View.VISIBLE);
        mSearchBackIcon.setVisibility(View.INVISIBLE);
        hideKeyboard(mSearchEditText);
    }

    //@Override
    //public boolean requestFocus() {
    //    return mSearchEditText.requestFocus();
    //}

    public void setEnabled(boolean enabled) {
        mSearchEditText.setEnabled(enabled);
    }

    public void setSearchMode(boolean searchMode) {
        mInSearchMode = searchMode;
    }

    public boolean getSearchMode() {
        return mInSearchMode;
    }
}
