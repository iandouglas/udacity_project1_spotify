package com.iandouglas.spotifystreamer.app;

import kaaes.spotify.webapi.android.models.Artist;

public class SpotifyArtist {
    String id;
    String name;
    String imgUrl;

    public SpotifyArtist(Artist artist) {
        this.id = artist.id;
        this.name = artist.name;
        this.imgUrl = "";

        if (artist.images.size() > 0) {
            this.imgUrl = artist.images.get(0).url;
        }
    }
}
