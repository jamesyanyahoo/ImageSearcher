package com.yahoo.shopping.imagesearcher.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.yahoo.shopping.imagesearcher.R;
import com.yahoo.shopping.imagesearcher.interfaces.OnApplySettings;
import com.yahoo.shopping.imagesearcher.models.FilterConfig;

/**
 * Created by jamesyan on 8/4/15.
 */
public class SearchConfigDialogFragment extends DialogFragment {

    String mTitle;

    private static SearchConfigDialogFragment sDialogFragment;

    private Spinner mSpSize;
    private Spinner mSpColor;
    private Spinner mSpType;
    private TextView mTvSite;

    public static SearchConfigDialogFragment newInstance(String title) {
        if (sDialogFragment == null) {
            sDialogFragment = new SearchConfigDialogFragment();
        }

        // write code for title

        return sDialogFragment;
    }

    private ArrayAdapter<CharSequence> createAdapter(int resouceId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), resouceId, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        return adapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_search_config, container);

        mSpSize = (Spinner) view.findViewById(R.id.dialog_config_sp_size);
        mSpSize.setAdapter(createAdapter(R.array.config_image_size));

        mSpColor = (Spinner) view.findViewById(R.id.dialog_config_sp_color);
        mSpColor.setAdapter(createAdapter(R.array.config_image_color));

        mSpType = (Spinner) view.findViewById(R.id.dialog_config_sp_type);
        mSpType.setAdapter(createAdapter(R.array.config_image_type));

        mTvSite = (TextView) view.findViewById(R.id.dialog_config_txt_site);

        Button btnApply = (Button) view.findViewById(R.id.dialog_config_btn_apply);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchConfigDialogFragment.this.dismiss();
            }
        });

        return view;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        FilterConfig config = new FilterConfig();

        String size = mSpSize.getSelectedItem().toString();
        if (size.equals(getResources().getString(R.string.config_image_size_small))) {
            config.setSizeFilter(FilterConfig.ImageSizeFilter.Small);
        } else if (size.equals(getResources().getString(R.string.config_image_size_medium))) {
            config.setSizeFilter(FilterConfig.ImageSizeFilter.Medium);
        } else if (size.equals(getResources().getString(R.string.config_image_size_large))) {
            config.setSizeFilter(FilterConfig.ImageSizeFilter.Large);
        } else if (size.equals(getResources().getString(R.string.config_image_size_extra_large))) {
            config.setSizeFilter(FilterConfig.ImageSizeFilter.ExtraLarge);
        }

        String color = mSpColor.getSelectedItem().toString();
        if (color.equals(getResources().getString(R.string.config_image_color_black))) {
            config.setColorFilter(FilterConfig.ImageColorFilter.Black);
        } else if (color.equals(getResources().getString(R.string.config_image_color_blue))) {
            config.setColorFilter(FilterConfig.ImageColorFilter.Blue);
        } else if (color.equals(getResources().getString(R.string.config_image_color_brown))) {
            config.setColorFilter(FilterConfig.ImageColorFilter.Brown);
        } else if (color.equals(getResources().getString(R.string.config_image_color_gray))) {
            config.setColorFilter(FilterConfig.ImageColorFilter.Gray);
        } else if (color.equals(getResources().getString(R.string.config_image_color_green))) {
            config.setColorFilter(FilterConfig.ImageColorFilter.Green);
        }

        String type = mSpType.getSelectedItem().toString();
        if (type.equals(getResources().getString(R.string.config_image_type_face))) {
            config.setTypeFilter(FilterConfig.ImageTypeFiler.Face);
        } else if (type.equals(getResources().getString(R.string.config_image_type_photo))) {
            config.setTypeFilter(FilterConfig.ImageTypeFiler.Photo);
        } else if (type.equals(getResources().getString(R.string.config_image_type_clip_art))) {
            config.setTypeFilter(FilterConfig.ImageTypeFiler.ClipArt);
        } else if (type.equals(getResources().getString(R.string.config_image_type_line_art))) {
            config.setTypeFilter(FilterConfig.ImageTypeFiler.LineArt);
        }

        config.setSiteFilter(mTvSite.getText().toString());

        OnApplySettings handler = (OnApplySettings) getActivity();
        handler.onApplySettings(config);
    }
}
