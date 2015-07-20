package com.david.trackshere.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.david.trackshere.R;
import com.david.trackshere.activity.TracksInfoActivity;
import com.parse.ParseObject;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by davidhodge on 12/26/14.
 */
public class TracksAdapter extends BaseAdapter {

    private List<ParseObject> items;
    private LayoutInflater inflater;
    private Context mContext;

    public TracksAdapter(Context context, List<ParseObject> items) {
        inflater = LayoutInflater.from(context);
        this.items = items;
        mContext = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_tracks, parent, false);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.location = (TextView) convertView.findViewById(R.id.location);
            holder.image = (ImageView) convertView.findViewById(R.id.image);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(items.get(position).getString("name"));
        holder.location.setText(items.get(position).getString("loc"));

        Picasso.with(mContext)
                .load(items.get(position).getString("image"))
                .into(holder.image);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cat = new Intent(mContext, TracksInfoActivity.class);
                cat.putExtra("extras", items.get(position).toString());
                cat.putExtra("name", items.get(position).getString("name"));
                cat.putExtra("search", items.get(position).getString("dName"));
                cat.putExtra("lat", items.get(position).getString("lat"));
                cat.putExtra("lon", items.get(position).getString("lon"));
                cat.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.getApplicationContext().startActivity(cat);
            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView name;
        TextView location;
        ImageView image;
    }
}
