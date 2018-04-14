package com.example.karlo.learningapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.karlo.learningapplication.R;
import com.example.karlo.learningapplication.models.wiki.Item;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Karlo on 31.3.2018..
 */

public class WikiResultAdapter extends BaseAdapter {

    List<Item> mItems;

    public WikiResultAdapter(List<Item> items) {
        mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        {
            ViewHolder itemViewHolder;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                convertView = inflater.inflate(R.layout.item_search_result, parent, false);
                itemViewHolder = new ViewHolder(convertView);
                convertView.setTag(itemViewHolder);
            } else {
                itemViewHolder = (ViewHolder) convertView.getTag();
            }

            Item item = this.mItems.get(position);
            itemViewHolder.tvTitle.setText(item.getTitle());
            itemViewHolder.tvSnippet.setText(item.getSnippet());
            String image = item.getPagemap().getCseImage() != null ? item.getPagemap().getCseImage().get(0).getSrc() : null;
            Picasso.get()
                    .load(image)
                    .resize(100, 100)
                    .centerCrop()
                    .placeholder(R.drawable.no_img)
                    .into(itemViewHolder.imageView);

            return convertView;
        }
    }

    public static class ViewHolder {
        public TextView tvTitle, tvSnippet;
        public ImageView imageView;

        public ViewHolder(View view) {
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvSnippet = (TextView) view.findViewById(R.id.tvSnippet);
            imageView = (ImageView) view.findViewById(R.id.imageView);
        }
    }
}
