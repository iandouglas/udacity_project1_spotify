package com.iandouglas.spotifystreamer.app;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.List;

public class SpotifyArtistAdapter extends ArrayAdapter<SpotifyArtist> {
    private final Activity context;

    public class ViewHolder {
        TextView artistName;
        ImageView artistImage;
    }

    public SpotifyArtistAdapter(Activity context, List<SpotifyArtist> artists) {
        super(context, 0, artists);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RequestCreator p;
        ViewHolder holder;

        SpotifyArtist artist = getItem(position);

        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_item_artists, parent, false);

            holder = new ViewHolder();
            holder.artistName = (TextView) convertView.findViewById(R.id.list_item_artists_name);
            holder.artistImage = (ImageView) convertView.findViewById(R.id.list_item_artist_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.artistName.setText(artist.name);

        p = Picasso.with(getContext()).load(R.drawable.no_image);
        if (artist.imgUrl != "") {
            p = Picasso.with(getContext()).load(artist.imgUrl);
        }
        p.resize(50, 50).into(holder.artistImage);

        return convertView;
    }
}
