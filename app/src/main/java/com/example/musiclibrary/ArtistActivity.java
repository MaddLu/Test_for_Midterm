package com.example.musiclibrary;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ArtistActivity extends AppCompatActivity {

    TextView textViewArtist;
    EditText editTextTrackName;
    RatingBar ratingBarRating;
    String artistId;
    ListView listViewTracks;

    DatabaseReference db;
    List<Track> tracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);

        //instantiate views
        editTextTrackName = findViewById(R.id.editTextTrackName);
        ratingBarRating = findViewById(R.id.ratingBarRating);
        listViewTracks = findViewById(R.id.listViewTracks);

        // obtain and displat artist name passed in as an extra
        Intent intent = getIntent();

        textViewArtist = findViewById(R.id.textViewArtist);
        textViewArtist.setText(intent.getStringExtra("artistName"));
        artistId = intent.getStringExtra("artistId");

        //db
        //make reference to a new or existing child node
        db = FirebaseDatabase.getInstance().getReference("artists/" + artistId + "/tracks");
        tracks = new ArrayList<>();
    }

    @Override
    protected void onStart(){
        super.onStart();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tracks.clear();

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    // create a new track for each document in the query and add to in memory array
                    Track track = postSnapshot.getValue(Track.class);
                    tracks.add(track);
                }
                // bind in memory array to the track listview
                TrackList trackListAdapter = new TrackList(ArtistActivity.this, tracks);
                listViewTracks.setAdapter(trackListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addTrack(View view) {
        //get input
        String trackName = editTextTrackName.getText().toString().trim();
        int rating = ((int) ratingBarRating.getRating());

        // validate and save
        if (TextUtils.isEmpty(trackName)){
            Toast.makeText(this, "Please enter a Track Name", Toast.LENGTH_LONG).show();
        }
        else{
            //generate a new id in the tracks collection beneth this artist
            String id = db.push().getKey();
            Track track = new Track(id, trackName, rating);
            db.child(id).setValue(track);
            Toast.makeText(this, "Track Saved", Toast.LENGTH_LONG).show();

            // clear form
            editTextTrackName.setText("");
            ratingBarRating.setRating(0);
        }
    }
}
