package com.example.coursework_uni;

public class Music {
    public String artist;
    public String title;
    public String track;
    public double duration;

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public Music(String artist, String title, String track, double duration) {
        this.artist = artist;
        this.title = title;
        this.track = track;
        this.duration = duration;
    }
}
