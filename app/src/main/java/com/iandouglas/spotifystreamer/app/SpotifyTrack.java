package com.iandouglas.spotifystreamer.app;

import android.os.Parcel;
import android.os.Parcelable;

import kaaes.spotify.webapi.android.models.Track;

public class SpotifyTrack implements Parcelable {
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

    private SpotifyTrack(Parcel in) {
        id = in.readString();
        name = in.readString();
        album = in.readString();
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
        parcel.writeString(album);
        parcel.writeString(imgUrl);
    }

    public final Parcelable.Creator<SpotifyTrack> CREATOR = new Parcelable.Creator<SpotifyTrack>() {
        @Override
        public SpotifyTrack createFromParcel(Parcel parcel) {
            return new SpotifyTrack(parcel);
        }

        @Override
        public SpotifyTrack[] newArray(int i) {
            return new SpotifyTrack[i];
        }
    };
}
