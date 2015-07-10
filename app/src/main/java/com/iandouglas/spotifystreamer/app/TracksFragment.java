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
        mTracks = new ArrayList<>();

        TracksActivity trackActivity = (TracksActivity) getActivity();
        artistId = trackActivity.getArtistId();
        artistName = trackActivity.getArtistName();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mTracksAdapter = new SpotifyTrackAdapter(getActivity(), mTracks);

        View rootView = inflater.inflate(R.layout.fragment_tracks, container, false);

        TextView artistNameView = (TextView) rootView.findViewById(R.id.artist_name_text);
        artistNameView.setText(getString(R.string.top_tracks) + " " + artistName);

        ListView listView = (ListView) rootView.findViewById(R.id.artist_track_list);
        listView.setAdapter(mTracksAdapter);

        if (trackTask.getStatus() != AsyncTask.Status.FINISHED) {
            Log.d("TracksFragment", "onCreateView, canceling old search");
            trackTask.cancel(true);
        }

        mTracksAdapter.clear();

        trackTask = new FetchTracksTask();
        // param should be the artist id

        trackTask.execute(this.artistId);

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                SpotifyTrack track = mTracksAdapter.getItem(position);
//
//                Intent intent = new Intent(getActivity(), TracksFragment.class);
//                intent.putExtra(getString(R.string.track_name), track.name);
//                intent.putExtra(getString(R.string.track_id), track.id);
//
//                startActivity(intent);
//            }
//        });

        return rootView;
    }


    public class FetchTracksTask extends AsyncTask<String, Void, List<SpotifyTrack>> {

        private final String LOG_TAG = FetchTracksTask.class.getSimpleName();

        @Override
        protected List<SpotifyTrack> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            SpotifyService spotify = new SpotifyApi().getService();
            Map<String, Object> queryMap = new HashMap<>();
            queryMap.put("country", "US");

            Tracks tracks = spotify.getArtistTopTrack(artistId, queryMap);
            if (tracks == null || tracks.tracks.size() == 0) {
                showToast(getString(R.string.no_tracks));
            }

            return parseSpotifyTrackResults(tracks);

        }

        @Override
        protected void onPostExecute(List<SpotifyTrack> result) {
            if (result != null) {
                mTracksAdapter.clear();
                mTracksAdapter.addAll(result);
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
