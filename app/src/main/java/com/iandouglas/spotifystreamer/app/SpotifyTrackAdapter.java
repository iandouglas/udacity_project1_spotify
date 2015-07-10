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

public class SpotifyTrackAdapter extends ArrayAdapter<SpotifyTrack> {

    public SpotifyTrackAdapter(Activity context, List<SpotifyTrack> tracks) {
        super(context, 0, tracks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RequestCreator p;

        SpotifyTrack track = getItem(position);
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_tracks, parent, false);

        ImageView imgView = (ImageView) rootView.findViewById(R.id.list_item_track_image);
        p = Picasso.with(getContext()).load(R.drawable.no_image);
        if (track.imgUrl != "") {
            p = Picasso.with(getContext()).load(track.imgUrl);
        }
        p.resize(50, 50).into(imgView);

        TextView trackNameView = (TextView) rootView.findViewById(R.id.list_item_track_name);
        trackNameView.setText(track.name);

        TextView albumNameView = (TextView) rootView.findViewById(R.id.list_item_album_name);
        albumNameView.setText(track.album);

        return rootView;
    }
}
