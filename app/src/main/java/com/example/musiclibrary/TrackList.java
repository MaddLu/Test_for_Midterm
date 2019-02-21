package com.example.musiclibrary;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TrackList extends ArrayAdapter<Track> {
    private Activity context;
    List<Track> tracks;

    // constructor bind to layout track list layout resourse file
    public TrackList(Activity context, List<Track> tracks){
        super(context, R.layout.layout_track_list, tracks);
        this.context = context;
        this.tracks = tracks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        // reference the layout we want to bind too
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_track_list, null, true);

        //Access the 2 textview we need to populate for each list item
        TextView textViewTrackName = listViewItem.findViewById(R.id.textViewTrackName);
        TextView textViewRating = listViewItem.findViewById(R.id.textViewRating);

        //bind the data
        Track track = tracks.get(position);
        textViewTrackName.setText(track.getTrackName());
        textViewRating.setText(String.valueOf(track.getRating()));

        return listViewItem;
    }
}
