package com.iandouglas.spotifystreamer.app;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class TracksFragment extends Fragment {

    public final static String LOG_TAG = "TracksFragment";

    private Toast toast;
    private SpotifyTrackAdapter mTracksAdapter;
    private ArrayList<SpotifyTrack> mTracks;
    FetchTracksTask trackTask = new FetchTracksTask();

    public String artistId;
    public String artistName;

    public TracksFragment() {
        trackTask = new FetchTracksTask();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        toast = new Toast(getActivity().getApplicationContext());

        if (savedInstanceState == null || !savedInstanceState.containsKey(getString(R.string.cached_tracks))) {
            mTracks = new ArrayList<>();
            TracksActivity trackActivity = (TracksActivity) getActivity();
            artistId = trackActivity.getArtistId();
            artistName = trackActivity.getArtistName();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.tracksfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        SpotifyArtist artist = new SpotifyArtist(artistId, artistName, "");
        outState.putParcelable(getString(R.string.cached_artist), artist);

        outState.putParcelableArrayList(getString(R.string.cached_tracks), mTracks);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (savedInstanceState != null && savedInstanceState.containsKey(getString(R.string.cached_tracks))) {
            mTracks = savedInstanceState.getParcelableArrayList(getString(R.string.cached_tracks));

            final SpotifyArtist artist = savedInstanceState.getParcelable(getString(R.string.cached_artist));
            artistId = artist.id;
            artistName = artist.name;
        }

        mTracksAdapter = new SpotifyTrackAdapter(getActivity(), mTracks);

        View rootView = inflater.inflate(R.layout.fragment_tracks, container, false);

        TextView artistNameView = (TextView) rootView.findViewById(R.id.artist_name_text);
        artistNameView.setText(getString(R.string.top_tracks) + " " + artistName);

        ListView listView = (ListView) rootView.findViewById(R.id.artist_track_list);
        listView.setAdapter(mTracksAdapter);

        if (savedInstanceState == null) {
            if (trackTask.getStatus() != AsyncTask.Status.FINISHED) {
                Log.d("TracksFragment", "onCreateView, canceling old search");
                trackTask.cancel(true);
            }

            mTracksAdapter.clear();

            trackTask = new FetchTracksTask();
            trackTask.execute(this.artistId);
        }

        if (mTracks != null) {
//            mTracksAdapter.clear();
            mTracksAdapter.addAll(mTracks);
        }

        return rootView;
    }


    public class FetchTracksTask extends AsyncTask<String, Void, List<SpotifyTrack>> {

        private final String LOG_TAG = FetchTracksTask.class.getSimpleName();

        @Override
        protected List<SpotifyTrack> doInBackground(String... params) {
            Tracks tracks = null;

            if (params.length == 0) {
                return null;
            }

            SpotifyService spotify = new SpotifyApi().getService();
            Map<String, Object> queryMap = new HashMap<>();
            queryMap.put("country", "US");

            try {
                tracks = spotify.getArtistTopTrack(artistId, queryMap);
            } catch (RetrofitError ex) {
                Toast.makeText(getActivity(), getResources().getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            }

            return parseSpotifyTrackResults(tracks);
        }

        @Override
        protected void onPostExecute(List<SpotifyTrack> results) {
            if (results == null || results.size() == 0) {
                showToast(getString(R.string.no_tracks));
                return;
            }

            loadTrackList(results);
        }

        public void loadTrackList(List<SpotifyTrack> results) {
            mTracksAdapter.clear();

            if (results != null) {
                mTracksAdapter.addAll(results);
            }
        }

        public List<SpotifyTrack> parseSpotifyTrackResults(Tracks results) {
            List<SpotifyTrack> data = new ArrayList<SpotifyTrack>();

            for(Track track : results.tracks) {
                data.add(new SpotifyTrack(track));
            }

            return data;
        }

        public void showToast(String message) {
            toast.cancel();
            toast = Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
