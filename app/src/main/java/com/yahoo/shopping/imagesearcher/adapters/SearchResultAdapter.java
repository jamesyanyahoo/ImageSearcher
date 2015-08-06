package com.yahoo.shopping.imagesearcher.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yahoo.shopping.imagesearcher.R;
import com.yahoo.shopping.imagesearcher.activities.ImageViewActivity;
import com.yahoo.shopping.imagesearcher.models.Image;

import java.util.List;

/**
 * Created by jamesyan on 8/4/15.
 */
public class SearchResultAdapter extends ArrayAdapter<Image> {
    private Context mContext;
    private List<Image> mImages;

    public SearchResultAdapter(Context context, int resource, List<Image> objects) {
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

        final Image image = mImages.get(position);

        ImageView ivImage = (ImageView) convertView.findViewById(R.id.image_list_item_iv_image);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.image_list_item_tv_title);

        tvTitle.setText(Html.fromHtml(image.getTitle()));

        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        float imageWidth = image.getWidth();
        float imageHeight = image.getHeight();
        float imageViewWidth = ((metrics.widthPixels / metrics.density - 3 * 7) / 2 - 2) * metrics.density;
        float imageViewHeight = imageHeight * ( imageViewWidth  / imageWidth );

        ivImage.setMinimumWidth((int) imageViewWidth);
        ivImage.setMinimumHeight((int) imageViewHeight);
        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ImageViewActivity.class);
                intent.putExtra("imageUrl", image.getUrl());
                mContext.startActivity(intent);
            }
        });
        Log.i("xxxxxxxxx", image.getUrl());
        Picasso.with(mContext).load(image.getUrl()).into(ivImage);

        return convertView;
    }
}
