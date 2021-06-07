package com.TETOSOFT.tilegame;

import com.TETOSOFT.input.InputManager;
import junit.framework.TestCase;
import org.junit.Test;

public class GameEngineTest extends TestCase {
    private InputManager inputManager;
    GameEngine ge;

    @Test
    public void testInitInput(){
        new GameEngine().init();

    }


}