package com.example.karlo.sstconference.pager;

import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.View;

import com.example.karlo.sstconference.R;

public abstract class CardFragment extends Fragment {

    protected CardView mCardView;

    protected int mPosition = 0;
    protected OnArrowClick mListener;

    public interface OnArrowClick {
        void onArrowClick(int position);
    }

    public void setListener(OnArrowClick mListener) {
        this.mListener = mListener;
    }

    protected CardView getCardView() {
        return mCardView;
    };

    public class ArrowClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.left_arrow:
                    mListener.onArrowClick(mPosition - 1);
                    break;
                case R.id.right_arrow:
                    mListener.onArrowClick(mPosition + 1);
                    break;
            }
        }
    }
}
