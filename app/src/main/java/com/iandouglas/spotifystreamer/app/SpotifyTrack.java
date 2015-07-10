package com.iandouglas.spotifystreamer.app;

import kaaes.spotify.webapi.android.models.Track;

public class SpotifyTrack {
    String id;
    String name;
    String album;
    String imgUrl;

    public SpotifyTrack(Track track) {
        this.id = track.id;
        this.name = track.name;
        this.album = track.album.name;
        this.imgUrl = "";

        if (track.album.images.size() > 0) {
            this.imgUrl = track.album.images.get(0).url;
        }
    }
}
