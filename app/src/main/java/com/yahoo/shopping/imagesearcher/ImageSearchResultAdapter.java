package com.yahoo.shopping.imagesearcher;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by jamesyan on 8/4/15.
 */
public class ImageSearchResultAdapter extends ArrayAdapter<Image> {
    private Context mContext;
    private List<Image> mImages;

    public ImageSearchResultAdapter(Context context, int resource, List<Image> objects) {
        super(context, resource, objects);

        this.mContext = context;
        this.mImages = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_view_image_item, null);
        }

        Image image = mImages.get(position);

        ImageView ivImage = (ImageView) convertView.findViewById(R.id.image_list_item_iv_image);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.image_list_item_tv_title);

        tvTitle.setText(image.getTitle());
        Picasso.with(mContext).load(image.getUrl()).into(ivImage);

        return convertView;
    }
}
