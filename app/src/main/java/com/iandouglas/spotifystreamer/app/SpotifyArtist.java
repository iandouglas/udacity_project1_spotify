package com.iandouglas.spotifystreamer.app;

import android.os.Parcel;
import android.os.Parcelable;

import kaaes.spotify.webapi.android.models.Artist;

public class SpotifyArtist implements Parcelable {
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

    public SpotifyArtist(String id, String name, String imgUrl) {
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
    }

    private SpotifyArtist(Parcel in) {
        id = in.readString();
        name = in.readString();
        imgUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(imgUrl);
    }

    public final Parcelable.Creator<SpotifyArtist> CREATOR = new Parcelable.Creator<SpotifyArtist>() {
        @Override
        public SpotifyArtist createFromParcel(Parcel parcel) {
            return new SpotifyArtist(parcel);
        }

        @Override
        public SpotifyArtist[] newArray(int i) {
            return new SpotifyArtist[i];
        }
    };
}
