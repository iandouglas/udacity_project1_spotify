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
    private final Activity context;

    public class ViewHolder {
        TextView trackName;
        TextView albumName;
        ImageView trackImage;
    }

    public SpotifyTrackAdapter(Activity context, List<SpotifyTrack> tracks) {
        super(context, 0, tracks);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RequestCreator p;
        ViewHolder holder;

        SpotifyTrack track = getItem(position);

        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_item_tracks, parent, false);

            holder = new ViewHolder();
            holder.trackName = (TextView) convertView.findViewById(R.id.list_item_track_name);
            holder.albumName = (TextView) convertView.findViewById(R.id.list_item_album_name);
            holder.trackImage = (ImageView) convertView.findViewById(R.id.list_item_track_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.trackName.setText(track.name);
        holder.albumName.setText(track.album);

        p = Picasso.with(getContext()).load(R.drawable.no_image);
        if (track.imgUrl != "") {
            p = Picasso.with(getContext()).load(track.imgUrl);
        }
        p.resize(50, 50).into(holder.trackImage);

        return convertView;
    }
}
