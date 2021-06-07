package com.TETOSOFT.input;

import junit.framework.TestCase;
import org.junit.Before;

import java.awt.event.KeyEvent;

public class InputManagerTest extends TestCase {
    InputManager im;
    GameAction gm1;
    GameAction gm2;
    @Before
    public void initInputManager(){
        im = new InputManager(null);
        gm1 = new GameAction("moveDown",GameAction.DETECT_INITAL_PRESS_ONLY);
        gm2 =  new GameAction("jump", GameAction.DETECT_INITAL_PRESS_ONLY);
    }

    public void testMapToKey() {
/*        im.mapToKey(new GameAction("moveDown",GameAction.DETECT_INITAL_PRESS_ONLY), KeyEvent.VK_DOWN);
        im.mapToKey(gm2, KeyEvent.VK_DOWN);
        assertEquals(1,im.getMaps(gm1).size());*/
    }
}