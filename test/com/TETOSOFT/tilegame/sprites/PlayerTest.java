package com.TETOSOFT.tilegame.sprites;

import com.TETOSOFT.graphics.Animation;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;

public class PlayerTest  {
    private Animation anim;
    private Player p;

    @Before
    public void initPlayer(){
        anim = new Animation();
        p = new Player(anim, anim, anim, anim);
    }


    @Test
    public void testSetY() {

        float inputY = (float)123;
        p.setY(inputY);
        assertEquals(inputY,p.getY(),0);
    }

    @Test
    public void testSetX() {
        float inputX = (float)123;
        p.setX(inputX);
        assertEquals(inputX,p.getX());
    }

    @Test
    public void testCollisions(){
        float expectedVal = (float) 0;
        p.collideHorizontal();
        p.collideVertical();

        float velX = p.getVelocityX();
        float velY = p.getVelocityY();

        assertEquals(expectedVal,velX);
        assertEquals(expectedVal, velY);
    }

    @Test
    public void testStates(){
        p.setState(p.STATE_DEAD);
        int state = p.getState();
        assertEquals(p.STATE_DEAD, state);

        assertFalse(p.isAlive());
        assertEquals((float)0, p.getVelocityX());
        assertEquals((float)0, p.getVelocityY());
        assertEquals((float)0, p.getX());
        assertEquals((float)0, p.getY());
    }


}