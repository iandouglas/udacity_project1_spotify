package com.iandouglas.spotifystreamer.app;

import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.RetrofitError;


public class ArtistsFragment extends Fragment {
    private Toast toast;
    private SpotifyArtistAdapter mArtistsAdapter;
    private ArrayList<SpotifyArtist> mArtists;
    FetchArtistsTask artistTask = new FetchArtistsTask();

    public ArtistsFragment() {
        artistTask = new FetchArtistsTask();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        toast = new Toast(getActivity().getApplicationContext());

        if (savedInstanceState == null || !savedInstanceState.containsKey(getString(R.string.cached_artists))) {
            mArtists = new ArrayList<>();
        } else {
            mArtists = savedInstanceState.getParcelableArrayList(getString(R.string.cached_artists));
        }
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
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(getString(R.string.cached_artists), mArtists);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mArtistsAdapter = new SpotifyArtistAdapter(getActivity(), mArtists);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_artists);
        listView.setAdapter(mArtistsAdapter);

        final EditText artistSearch = (EditText)rootView.findViewById(R.id.edittext_artist_search);
        artistSearch.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                if (s.equals(artistSearch.getText())) {
                    return;
                }

                // if the user is still typing, cancel any old search going on
                artistTask.cancel(true);

                if (artistSearch.length() == 0) {
                    mArtistsAdapter.clear();
                }

                if (artistSearch.length() >= 2) {
                    artistTask = new FetchArtistsTask();
                    artistTask.execute(artistSearch.getText().toString());
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                SpotifyArtist artist = mArtistsAdapter.getItem(position);

                Intent intent = new Intent(getActivity(), TracksActivity.class);
                intent.putExtra(getString(R.string.artist_name), artist.name);
                intent.putExtra(getString(R.string.artist_id), artist.id);

                startActivity(intent);
            }
        });

        if (savedInstanceState != null) {
            List<SpotifyArtist> result = savedInstanceState.getParcelableArrayList(getString(R.string.cached_artists));
            if (result != null) {
                mArtistsAdapter.addAll(result);
            }
        }

        return rootView;
    }


    public class FetchArtistsTask extends AsyncTask<String, Void, List<SpotifyArtist>> {

        private final String LOG_TAG = FetchArtistsTask.class.getSimpleName();

        @Override
        protected List<SpotifyArtist> doInBackground(String... params) {
            ArtistsPager results = null;

            if (params.length == 0) {
                return null;
            }

            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            try {
                results = spotify.searchArtists(params[0]);
            } catch (RetrofitError ex) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                        toast = Toast.makeText(
                                getActivity().getApplicationContext(),
                                getString(R.string.connection_error),
                                Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            }

            return parseSpotifyArtistResults(results);
        }

        @Override
        protected void onPostExecute(List<SpotifyArtist> results) {
            mArtistsAdapter.clear();

            if (results == null || results.size() == 0) {
                showToast(getString(R.string.no_artists_found));
                return;
            }

            mArtistsAdapter.addAll(results);
        }

        public List<SpotifyArtist> parseSpotifyArtistResults(ArtistsPager results) {
            List<SpotifyArtist> data = new ArrayList<SpotifyArtist>();

            if (results == null || results.artists.items.size() == 0) {
                return data;
            }

            for(Artist artist : results.artists.items) {
                data.add(new SpotifyArtist(artist));
            }

            return data;
        }

        public void showToast(String message) {
            toast.cancel();
            toast = Toast.makeText(
                    getActivity().getApplicationContext(),
                    message,
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
