package com.bootcamp.amberved.googleimagesearch.adapaters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bootcamp.amberved.googleimagesearch.R;
import com.bootcamp.amberved.googleimagesearch.models.GoogleImageResults;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by amber_ved on 2/14/15.
 */
public class GoogleImageResultsAdapater extends ArrayAdapter<GoogleImageResults> {
    
    public GoogleImageResultsAdapater(Context context, /*int resource,*/ List<GoogleImageResults> images) {
        super(context, android.R.layout.simple_list_item_1, images);
        
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        GoogleImageResults image = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.google_image_results, parent, false);
        }
        
        ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        
        ivImage.setImageResource(0);

        Picasso.with(getContext()).load(image.getTbUrl())
                .fit()
                .centerInside()
                .into(ivImage);
                //.placeholder(R.drawable.bridge)
                //.error(R.drawable.bridge)

        tvTitle.setText(Html.fromHtml(image.getTitle()));

        return convertView;   
    }
}
