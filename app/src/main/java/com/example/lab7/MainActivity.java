package com.example.lab7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Character> mCharacters;
    private ListView mListView;
    private CharactersAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = findViewById(R.id.listView1);
        mCharacters = new ArrayList<>();
        mAdapter = new CharactersAdapter(mCharacters);
        mListView.setAdapter(mAdapter);

        new CharactersAsyncTask().execute();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Character character = (Character) mAdapter.getItem(position);

                Bundle bundle = new Bundle();
                bundle.putString("name", character.toString());
                bundle.putString("height", character.toString());
                bundle.putString("mass", character.toString());

                if (findViewById(R.id.details_container) == null) {
                    Intent intent = new Intent(MainActivity.this, activity_empty.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    DetailsFragment fragment = new DetailsFragment();
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.details_container, fragment)
                            .commit();
                }
            }
        });

    }
    protected class CharactersAdapter extends BaseAdapter {

        private List<Character> mCharacters;

        public CharactersAdapter(List<Character> characters) {
            mCharacters = characters;
        }

        @Override
        public int getCount() {
            return mCharacters.size();
        }

        @Override
        public Object getItem(int position) {
            return mCharacters.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) convertView;
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            if (view == null) {
                view = (TextView) inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            }

            view.setText(mCharacters.get(position).charValue());

            return view;
        }
    }




        private class CharactersAsyncTask extends AsyncTask<Void, Void, List<Character>> {
            public void CharactersAdapter(List<Character> characters) {
                mCharacters = characters;
            }
            @Override
            protected List<Character> doInBackground(Void... voids) {
                List<Character> characters = new ArrayList<>();

                try {
                    URL url = new URL("https://swapi.dev/api/people/?format=json");
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    try {
                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder result = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }

                        JSONObject response = new JSONObject(result.toString());
                        JSONArray results = response.getJSONArray("results");
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject character = results.getJSONObject(i);
                            String name = character.getString("name");
                            String height = character.getString("height");
                            String mass = character.getString("mass");


                        }
                    } finally {
                        urlConnection.disconnect();
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

                return characters;
            }

            @Override
            protected void onPostExecute(List<Character> characters) {
                mCharacters = characters;
                mAdapter = new CharactersAdapter(mCharacters);
                mListView.setAdapter(mAdapter);
            }

        }
    }
