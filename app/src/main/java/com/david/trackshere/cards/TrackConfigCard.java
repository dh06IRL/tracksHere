package com.david.trackshere.cards;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.david.trackshere.R;
import com.david.trackshere.activity.GMapActivity;
import com.parse.ParseObject;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by davidhodge on 12/28/14.
 */
public class TrackConfigCard extends Card {

    Context mContext;
    CardView cardView;
    TextView name;
    ImageView image;
    ParseObject parseObject;
    String lat;
    String lon;
    Target target;

    public TrackConfigCard(Context context, ParseObject parseObject, String lat, String lon) {
        super(context, R.layout.card_track_config);
        mContext = context;
        this.parseObject = parseObject;
        this.lat = lat;
        this.lon = lon;
    }

    public TrackConfigCard(Context context, int innerLayout) {
        super(context, innerLayout);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        cardView = (CardView) parent.findViewById(R.id.card_view);
        name = (TextView) parent.findViewById(R.id.name);
        image = (ImageView) parent.findViewById(R.id.image);

        name.setText(parseObject.getString("name"));

        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                image.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(mContext)
                .load(parseObject.getString("image"))
                .into(target);

        this.setOnClickListener(new OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                Intent viewConfig = new Intent(mContext, GMapActivity.class);
                viewConfig.putExtra("name", parseObject.getString("name"));
                viewConfig.putExtra("data", parseObject.getString("data"));
                viewConfig.putExtra("lat", lat);
                viewConfig.putExtra("lon", lon);
                mContext.startActivity(viewConfig);
            }
        });
    }

}
