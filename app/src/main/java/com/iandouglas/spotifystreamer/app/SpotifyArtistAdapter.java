package com.iandouglas.spotifystreamer.app;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class SpotifyArtistAdapter extends ArrayAdapter<SpotifyArtist> {

    public SpotifyArtistAdapter(Activity context, List<SpotifyArtist> artists) {
        super(context, 0, artists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SpotifyArtist artist = getItem(position);
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_artists, parent, false);

//        ImageView imgView = (ImageView) rootView.findViewById(R.id.list_item_artist_image);
//        Picasso.with(getContext()).load(artist.imgUrl).resize(50, 50).into(imgView);

        TextView artistNameView = (TextView) rootView.findViewById(R.id.list_item_artists_name);
        artistNameView.setText(artist.artistName);

        return rootView;
    }
}
