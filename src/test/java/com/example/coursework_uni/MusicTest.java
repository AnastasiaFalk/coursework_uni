package com.example.coursework_uni;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MusicTest {

    @Test
    void testConstructor() {
        Music music = new Music("Artist", "Title", "Track", 3.45);
        assertEquals("Artist", music.getArtist());
        assertEquals("Title", music.getTitle());
        assertEquals("Track", music.getTrack());
        assertEquals(3.45, music.getDuration(), 0.01);
    }

    @Test
    void testSettersAndGetters() {
        Music music = new Music("", "", "", 0.0);

        music.setArtist("New Artist");
        assertEquals("New Artist", music.getArtist());

        music.setTitle("New Title");
        assertEquals("New Title", music.getTitle());

        music.setTrack("New Track");
        assertEquals("New Track", music.getTrack());

        music.setDuration(4.56);
        assertEquals(4.56, music.getDuration(), 0.01);
    }
}