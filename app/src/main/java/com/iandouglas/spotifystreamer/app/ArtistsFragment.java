package com.iandouglas.spotifystreamer.app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;

public class ArtistsFragment extends Fragment {
    private Toast toast;
    private SpotifyArtistAdapter mArtistsAdapter;


    public ArtistsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        toast = new Toast(getActivity().getApplicationContext());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.artistsfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        SpotifyArtist[] artistList = {};
        mArtistsAdapter = new SpotifyArtistAdapter(getActivity(), Arrays.asList(artistList));

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_artists);
        listView.setAdapter(mArtistsAdapter);

        final EditText artistSearch = (EditText)rootView.findViewById(R.id.edittext_artist_search);
        artistSearch.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                if (artistSearch.length() >= 2) {
                    FetchArtistsTask artistTask = new FetchArtistsTask();
                    artistTask.execute(artistSearch.getText().toString());
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        return rootView;
    }

    public class FetchArtistsTask extends AsyncTask<String, Void, List<SpotifyArtist>> {

        private final String LOG_TAG = FetchArtistsTask.class.getSimpleName();

        @Override
        protected List<SpotifyArtist> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            ArtistsPager results = spotify.searchArtists(params[0]);

            if (results == null || results.artists.items.size() == 0)
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                        toast = Toast.makeText(getActivity().getApplicationContext(), "no results found", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });

            return parseSpotifyArtistResults(results);
        }

//        @Override
        protected void onPostExecute(List<SpotifyArtist> result) {
            if (result != null) {
                mArtistsAdapter.clear();
                mArtistsAdapter.addAll(result);
//                for(SpotifyArtist artist : result) {
//                    Log.d(LOG_TAG, artist.artistName);
//                    Log.d(LOG_TAG, artist.imgUrl);
//                    mArtistsAdapter.add(artist);
//                }
            }
        }

        public List<SpotifyArtist> parseSpotifyArtistResults(ArtistsPager results) {
            List<SpotifyArtist> data = new ArrayList<SpotifyArtist>();

            for(Artist artist : results.artists.items) {

//            for (int i = 0; i < results.artists.items.size(); i++) {
//                Artist artist = results.artists.items.get(i);
                String artistName = artist.name;
                String imgUrl = "http://fc01.deviantart.net/fs71/f/2014/279/4/5/doge__by_honeybunny135-d81wk54.png";
                if (artist.images.size() > 0) {
                    imgUrl = artist.images.get(0).url;
                }
                data.add(new SpotifyArtist(artistName, imgUrl));
            }

            return data;
        }
    }
}
