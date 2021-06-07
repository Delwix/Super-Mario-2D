package com.TETOSOFT.tilegame;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class ExistingFilesTest {

    @Test
    public void audioTest(){
        File audioFile = new File("sounds/bg-music.wav");
        File sound = new File("sounds/enemy-die.wav");
        assertTrue(audioFile.exists());
        assertTrue(sound.exists());
    }

}
