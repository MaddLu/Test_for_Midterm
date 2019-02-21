package com.example.musiclibrary;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ArtistList extends ArrayAdapter<Artist> {
    private Activity context;
    List<Artist> artists;

    // constructor bind to layout artist list layout resourse file
    public ArtistList(Activity context, List<Artist> artists){
        super(context, R.layout.layout_artist_list, artists);
        this.context = context;
        this.artists = artists;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        // reference the layout we want to bind too
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_artist_list, null, true);

        //Access the 2 textview we need to populate for each list item
        TextView textViewArtistName = listViewItem.findViewById(R.id.textViewArtistName);
        TextView textViewArtistGenre = listViewItem.findViewById(R.id.textViewArtistGenre);

        //bind the data
        Artist artist = artists.get(position);
        textViewArtistName.setText(artist.getArtistName());
        textViewArtistGenre.setText(artist.getArtistGenre());

        return listViewItem;
    }
}
