package com.example.musiclibrary;

public class Track {
    private String trackId;
    private String trackName;
    private int rating;

    public Track(){

    }

    public Track(String trackId, String trackName, int rating){
        this.trackId = trackId;
        this.trackName = trackName;
        this.rating = rating;
    }

    public String getTrackId(){
        return trackId;
    }

    public String getTrackName(){

        return trackName;
    }

    public int getRating(){
        return rating;
    }
}
