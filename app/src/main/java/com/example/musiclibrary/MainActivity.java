package com.example.musiclibrary;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editTextName;
    Spinner spinnerGenres;
    DatabaseReference db;
    TextView textViewArtistName;
    TextView textViewArtistGenre;
    ListView listViewArtists;
    List<Artist> artists;
    FirebaseAuth authDb;
    Toolbar appToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.editTextName);
        spinnerGenres = findViewById(R.id.spinnerGenres);
        listViewArtists = findViewById(R.id.listViewArtists);
        appToolBar = findViewById(R.id.appToolBar);
        setSupportActionBar(appToolBar);

        //db connection
        db = FirebaseDatabase.getInstance().getReference("artists");
        artists = new ArrayList<>();
        authDb = FirebaseAuth.getInstance();
        if (authDb.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
        // Write a message to the database
        /*FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");*/
        listViewArtists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //get selected artist
                Artist artist = artists.get(position);

                // create an intent
                Intent intent = new Intent(getApplicationContext(), ArtistActivity.class);

                // pass the artist name and id
                intent.putExtra("artistName", artist.getArtistName());
                intent.putExtra("artistId", artist.getArtistId());

                // load the artist activity
                startActivity(intent);


            }
        });
    }

    //inflate the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_main:
                return true;
            case R.id.action_profile:
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart(){
        super.onStart();

        //create listener for data change
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // clear the data
                artists.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Artist artist = postSnapshot.getValue(Artist.class);
                    artists.add(artist);
                }

                //create data adapter and bind to list
                ArtistList artistAdapter = new ArtistList(MainActivity.this, artists);
                listViewArtists.setAdapter(artistAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void addArtist(View view) {
        // read input values
        String artistName = editTextName.getText().toString().trim();
        String artistGenre = spinnerGenres.getSelectedItem().toString();

        //Validate
        if (!TextUtils.isEmpty(artistName)) {
            //creat and populate objects, generating an id first and then including it
            String artistId = db.push().getKey();
            Artist artist = new Artist(artistId, artistName, artistGenre);

            //save db
            db.child(artistId).setValue(artist);

            // clear the edittext
            editTextName.setText("");
            Toast.makeText(this,"Artist Added", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this,"Please enter a name", Toast.LENGTH_LONG).show();
        }
    }

    public void logout(View view) {
        authDb.signOut();
        finish();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}
