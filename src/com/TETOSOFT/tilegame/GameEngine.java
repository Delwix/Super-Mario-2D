package com.TETOSOFT.tilegame;


import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Iterator;

import com.TETOSOFT.graphics.*;
import com.TETOSOFT.input.*;
import com.TETOSOFT.test.GameCore;
import com.TETOSOFT.tilegame.sprites.*;

import static java.lang.Math.abs;

/**
 * GameManager manages all parts of the game.
 */
public class GameEngine extends GameCore 
{
    
    public static void main(String[] args) 
    {
        new GameEngine().run();
    }

    public static final float GRAVITY = 0.002f;

    private Point currentPoint = new Point();
    private Point pointCache = new Point();
    private TileMap map;
    private MapLoader mapLoader;
    private InputManager inputManager;
    private TileMapDrawer drawer;
    
    private GameAction moveLeft;
    private GameAction moveRight;
    private GameAction moveUp;
    private GameAction moveDown;
    private GameAction jump;
    private GameAction exit;
    private GameAction enter;
    private GameAction pause;

    private int scoreCoin =0;
    private int score =0;
    private int topScore =0;
    private int gameScore =0;
    private int collectedStars=0;
    private int numLives=6;

    private final Image menuImage = loadImage("images/SuperMarioMenu3.png");
    private int selectedOption = 15000;

    public void init()
    {
        super.init();
        
        // set up input manager
        initInput();
        
        // start resource manager
        mapLoader = new MapLoader(screen.getFullScreenWindow().getGraphicsConfiguration());
        
        // load resources
        drawer = new TileMapDrawer();
        drawer.setBackground(mapLoader.loadImage("background.jpg"));
        
        // load first map
        map = mapLoader.loadNextMap();
    }
    
    
    /**
     * Closes any resurces used by the GameManager.
     */
    public void stop() {
        super.stop();
        
    }

    public void startGame(){
        super.startGame();
    }

    public void resumeGame() throws InterruptedException {
        super.resumeGame();
    }

    public void restartGame() throws InterruptedException {
        map = mapLoader.reloadMap();
        super.restartGame();
    }

    public void exitGame(){
        super.exitGame();
    }

    public void pauseGame(){
        super.pauseGame();
    }

    private void initInput() {
        moveLeft = new GameAction("moveLeft");
        moveRight = new GameAction("moveRight");
        moveUp = new GameAction("moveUp",GameAction.DETECT_INITAL_PRESS_ONLY);
        moveDown = new GameAction("moveDown",GameAction.DETECT_INITAL_PRESS_ONLY);
        jump = new GameAction("jump", GameAction.DETECT_INITAL_PRESS_ONLY);
        exit = new GameAction("exit",GameAction.DETECT_INITAL_PRESS_ONLY);
        enter = new GameAction("enter", GameAction.DETECT_INITAL_PRESS_ONLY);
        pause = new GameAction("pause",GameAction.DETECT_INITAL_PRESS_ONLY);

        inputManager = new InputManager(screen.getFullScreenWindow());
        inputManager.setCursor(InputManager.INVISIBLE_CURSOR);
        inputManager.mapToKey(pause, KeyEvent.VK_P);
        inputManager.mapToKey(enter, KeyEvent.VK_ENTER);
        inputManager.mapToKey(moveLeft, KeyEvent.VK_LEFT);
        inputManager.mapToKey(moveRight, KeyEvent.VK_RIGHT);
        inputManager.mapToKey(jump, KeyEvent.VK_SPACE);
        inputManager.mapToKey(exit, KeyEvent.VK_ESCAPE);
        inputManager.mapToKey(moveUp, KeyEvent.VK_UP);
        inputManager.mapToKey(moveDown, KeyEvent.VK_DOWN);
    }

    public void checkInputMenu(){
        if(moveUp.isPressed()){
            selectedOption -= 1;
        }
        if(moveDown.isPressed()){
            selectedOption += 1;
        }
        if(exit.isPressed()){
            exitGame();
        }
        if(enter.isPressed()){
            switch(selectedOption%3){
                case 0:
                    startGame();
                    break;
                case 1:
                    System.out.println("OPTIONS");
                    break;
                case 2:
                    exitGame();
                    break;
            }
        }
    }



    public void checkInputPause() throws InterruptedException {
        if(pause.isPressed()){
            pauseGame();
        }
        if(exit.isPressed()){
            exitGame();
        }
        if(moveUp.isPressed()){
            selectedOption -= 1;
        }
        if(moveDown.isPressed()){
            selectedOption += 1;
        }
        if(exit.isPressed()){
            exitGame();
        }
        if(enter.isPressed()){
            switch(selectedOption%3){
                case 0:
                    resumeGame();
                    break;
                case 1:
                    restartGame();
                    break;
                case 2:
                    exitGame();
                    break;
            }
        }
    }
    private void checkInput(long elapsedTime) 
    {
        
        if (exit.isPressed()) {
            stop();
        }

        if (pause.isPressed()){
            pauseGame();
        }

        Player player = (Player)map.getPlayer();
        if (player.isAlive()) 
        {
            float velocityX = 0;
            currentPoint = getTileCollision(player, player.getX(), player.getY());
            if (moveLeft.isPressed()) //Calculating the score depending on the movements on the X axis
            {
                velocityX-=player.getMaxSpeed();

                /*if (currentPoint.getY() != 9.0) {
                    System.out.println("i moved left !");
                    currentScoreXaxis = currentScoreXaxis - 0.01;
                    if (currentScoreXaxis > topScoreXaxis)
                        topScoreXaxis = (int) currentScoreXaxis;
                }*/

            }
            if (moveRight.isPressed()) {
                velocityX+=player.getMaxSpeed();
                score = (int) (player.getX() / 500);
                score += scoreCoin;
                if (score > topScore) {
                    topScore = score;
                }
                System.out.println("the score is " + score);
                /*if (currentPoint.getY() != 9.0) {
                    System.out.println("i moved right !");
                    currentScoreXaxis = currentScoreXaxis + 0.01;
                    if (currentScoreXaxis > topScoreXaxis)
                        topScoreXaxis = (int) currentScoreXaxis;
                }*/
            }
            if (jump.isPressed()) {
                player.jump(false);
            }
            player.setVelocityX(velocityX);
        }
        
    }
    
    
    public void draw(Graphics2D g) {
        
        drawer.draw(g, map, screen.getWidth(), screen.getHeight());
        g.setColor(Color.WHITE);
        g.drawString("Press ESC for EXIT.",10.0f,20.0f);
        g.setColor(Color.GREEN);
        g.drawString("Coins: "+collectedStars,300.0f,20.0f);
        g.setColor(Color.YELLOW);
        g.drawString("Lives: "+(numLives),500.0f,20.0f );
        g.setColor(Color.WHITE);
        g.drawString("Home: "+mapLoader.currentMap,700.0f,20.0f);
        
    }
    
    
    /**
     * Gets the current map.
     */
    public TileMap getMap() {
        return map;
    }
    
    /**
     * Gets the tile that a Sprites collides with. Only the
     * Sprite's X or Y should be changed, not both. Returns null
     * if no collision is detected.
     */
    public Point getTileCollision(Sprite sprite, float newX, float newY) 
    {
        float fromX = Math.min(sprite.getX(), newX);
        float fromY = Math.min(sprite.getY(), newY);
        float toX = Math.max(sprite.getX(), newX);
        float toY = Math.max(sprite.getY(), newY);
        
        // get the tile locations
        int fromTileX = TileMapDrawer.pixelsToTiles(fromX);
        int fromTileY = TileMapDrawer.pixelsToTiles(fromY);
        int toTileX = TileMapDrawer.pixelsToTiles(
                toX + sprite.getWidth() - 1);
        int toTileY = TileMapDrawer.pixelsToTiles(
                toY + sprite.getHeight() - 1);
        
        // check each tile for a collision
        for (int x=fromTileX; x<=toTileX; x++) {
            for (int y=fromTileY; y<=toTileY; y++) {
                if (x < 0 || x >= map.getWidth() ||
                        map.getTile(x, y) != null) {
                    // collision found, return the tile
                    pointCache.setLocation(x, y);
//                    if (y == 9)
//                        System.out.println("Oh shit collision happened ! X is " + pointCache.getX() +" and Y is " + pointCache.getY());
                    return pointCache;
                }
            }
        }
        
        // no collision found
        return null;
    }
    
    
    /**
     * Checks if two Sprites collide with one another. Returns
     * false if the two Sprites are the same. Returns false if
     * one of the Sprites is a Creature that is not alive.
     */
    public boolean isCollision(Sprite s1, Sprite s2) {
        // if the Sprites are the same, return false
        if (s1 == s2) {
            return false;
        }
        
        // if one of the Sprites is a dead Creature, return false
        if (s1 instanceof Creature && !((Creature)s1).isAlive()) {
            return false;
        }
        if (s2 instanceof Creature && !((Creature)s2).isAlive()) {
            return false;
        }
        
        // get the pixel location of the Sprites
        int s1x = Math.round(s1.getX());
        int s1y = Math.round(s1.getY());
        int s2x = Math.round(s2.getX());
        int s2y = Math.round(s2.getY());
        
        // check if the two sprites' boundaries intersect
        return (s1x < s2x + s2.getWidth() &&
                s2x < s1x + s1.getWidth() &&
                s1y < s2y + s2.getHeight() &&
                s2y < s1y + s1.getHeight());
    }
    
    
    /**
     * Gets the Sprite that collides with the specified Sprite,
     * or null if no Sprite collides with the specified Sprite.
     */
    public Sprite getSpriteCollision(Sprite sprite) {
        
        // run through the list of Sprites
        Iterator i = map.getSprites();
        while (i.hasNext()) {
            Sprite otherSprite = (Sprite)i.next();
            if (isCollision(sprite, otherSprite)) {
                // collision found, return the Sprite
                return otherSprite;
            }
        }
        
        // no collision found
        return null;
    }
    
    
    /**
     * Updates Animation, position, and velocity of all Sprites
     * in the current map.
     */
    public void update(long elapsedTime) {
        Creature player = (Creature)map.getPlayer();
        
        
        // player is dead! start map over
        if (player.getState() == Creature.STATE_DEAD) {  	
            map = mapLoader.reloadMap();
            return;
        }
        
        // get keyboard/mouse input
        checkInput(elapsedTime);
        
        // update player
        updateCreature(player, elapsedTime);
        player.update(elapsedTime);
        
        // update other sprites
        Iterator i = map.getSprites();
        while (i.hasNext()) {
            Sprite sprite = (Sprite)i.next();
            if (sprite instanceof Creature) {
                Creature creature = (Creature)sprite;
                if (creature.getState() == Creature.STATE_DEAD) {
                    i.remove();
                } else {
                    updateCreature(creature, elapsedTime);
                }
            }
            // normal update
            sprite.update(elapsedTime);
        }
    }
    
    
    /**
     * Updates the creature, applying gravity for creatures that
     * aren't flying, and checks collisions.
     */
    private void updateCreature(Creature creature,
            long elapsedTime) {
        
        // apply gravity
        if (!creature.isFlying()) {
            creature.setVelocityY(creature.getVelocityY() +
                    GRAVITY * elapsedTime);
        }
        
        // change x
        float dx = creature.getVelocityX();
        float oldX = creature.getX();
        float newX = oldX + dx * elapsedTime;
        Point tile =
                getTileCollision(creature, newX, creature.getY());
        if (tile == null) {
            creature.setX(newX);
        } else {
            // line up with the tile boundary
            if (dx > 0) {
                creature.setX(
                        TileMapDrawer.tilesToPixels(tile.x) -
                        creature.getWidth());
            } else if (dx < 0) {
                creature.setX(
                        TileMapDrawer.tilesToPixels(tile.x + 1));
            }
            creature.collideHorizontal();
        }
        if (creature instanceof Player) {
            checkPlayerCollision((Player)creature, false);
        }
        
        // change y
        float dy = creature.getVelocityY();
        float oldY = creature.getY();
        float newY = oldY + dy * elapsedTime;
        tile = getTileCollision(creature, creature.getX(), newY);
        if (tile == null) {
            creature.setY(newY);
        } else {
            // line up with the tile boundary
            if (dy > 0) {
                creature.setY(
                        TileMapDrawer.tilesToPixels(tile.y) -
                        creature.getHeight());
            } else if (dy < 0) {
                creature.setY(
                        TileMapDrawer.tilesToPixels(tile.y + 1));
            }
            creature.collideVertical();
        }
        if (creature instanceof Player) {
            boolean canKill = (oldY < creature.getY());
            checkPlayerCollision((Player)creature, canKill);
        }
        
    }

    public void updateMenu(){
        checkInputMenu();
    }
    public void updatePause() throws InterruptedException {
        checkInputPause();
    }
    
    /**
     * Checks for Player collision with other Sprites. If
     * canKill is true, collisions with Creatures will kill
     * them.
     */
    public void checkPlayerCollision(Player player,
            boolean canKill) {
        if (!player.isAlive()) {
            return;
        }
        
        // check for player collision with other sprites
        Sprite collisionSprite = getSpriteCollision(player);
        if (collisionSprite instanceof PowerUp) {
            acquirePowerUp((PowerUp)collisionSprite);
        } else if (collisionSprite instanceof Creature) {
            Creature badguy = (Creature)collisionSprite;
            if (canKill) {
                // kill the badguy and make player bounce
                badguy.setState(Creature.STATE_DYING);
                player.setY(badguy.getY() - player.getHeight());
                player.jump(true);
            } else {
                // player dies!
                player.setState(Creature.STATE_DYING);
                numLives--;
                if (score > gameScore)
                    gameScore = score;
                scoreCoin = 0;
                score = 0;
                if(numLives==0) {
                    //Insert final score interface here, and stop it from crashing everytime
                    gameOver();
                    
                }
            }
        }
    }
    
    
    /**
     * Gives the player the speicifed power up and removes it
     * from the map.
     */
    public void acquirePowerUp(PowerUp powerUp) {
        // remove it from the map
        map.removeSprite(powerUp);
        
        if (powerUp instanceof PowerUp.Star) {
            // do something here, like give the player points
            collectedStars++;
            scoreCoin++;
//            System.out.println("The score is " + score);
            if(collectedStars==100) 
            {
                numLives++;
                collectedStars=0;
            }
            
        } else if (powerUp instanceof PowerUp.Music) {
            // change the music
            
        } else if (powerUp instanceof PowerUp.Goal) {
            // advance to next map      
      
            map = mapLoader.loadNextMap();
            
        }
    }

    public void drawMenu(Graphics2D g){
        drawer.draw(g, map, screen.getWidth(), screen.getHeight());
        switch (selectedOption%3){
            case 0:
                g.setColor(Color.RED);
                g.drawString("PLAY",screen.getWidth()/2-60,screen.getHeight()/2+60);
                g.setColor(Color.WHITE);
                g.drawString("OPTIONS",screen.getWidth()/2-60,screen.getHeight()/2+90);
                g.setColor(Color.WHITE);
                g.drawString("EXIT GAME",screen.getWidth()/2-60,screen.getHeight()/2+120);
                break;
            case 1:
                g.setColor(Color.WHITE);
                g.drawString("PLAY",screen.getWidth()/2-60,screen.getHeight()/2+60);
                g.setColor(Color.RED);
                g.drawString("OPTIONS",screen.getWidth()/2-60,screen.getHeight()/2+90);
                g.setColor(Color.WHITE);
                g.drawString("EXIT GAME",screen.getWidth()/2-60,screen.getHeight()/2+120);
                break;
            case 2:
                g.setColor(Color.WHITE);
                g.drawString("PLAY",screen.getWidth()/2-60,screen.getHeight()/2+60);
                g.setColor(Color.WHITE);
                g.drawString("OPTIONS",screen.getWidth()/2-60,screen.getHeight()/2+90);
                g.setColor(Color.RED);
                g.drawString("EXIT GAME",screen.getWidth()/2-60,screen.getHeight()/2+120);
                break;
        }

        g.setColor(Color.WHITE);
        g.drawString("Press ESC for EXIT.",10.0f,20.0f);
        g.setColor(Color.GREEN);
        g.drawString("AAAAAAAA: "+collectedStars,300.0f,20.0f);
        g.setColor(Color.YELLOW);
        g.drawString("Bbbb: "+(numLives),500.0f,20.0f );
        g.setColor(Color.WHITE);
        g.drawString("Home: "+mapLoader.currentMap,700.0f,20.0f);

        g.drawImage(menuImage,screen.getWidth()/4,screen.getHeight()/5,null);
    }

    public void drawPause(Graphics2D g){
        drawer.draw(g, map, screen.getWidth(), screen.getHeight());
        switch (selectedOption%3){
            case 0:
                g.setColor(Color.RED);
                g.drawString("RESUME",screen.getWidth()/2-60,screen.getHeight()/2+60);
                g.setColor(Color.WHITE);
                g.drawString("RESTART",screen.getWidth()/2-60,screen.getHeight()/2+90);
                g.setColor(Color.WHITE);
                g.drawString("EXIT GAME",screen.getWidth()/2-60,screen.getHeight()/2+120);
                break;
            case 1:
                g.setColor(Color.WHITE);
                g.drawString("RESUME",screen.getWidth()/2-60,screen.getHeight()/2+60);
                g.setColor(Color.RED);
                g.drawString("RESTART",screen.getWidth()/2-60,screen.getHeight()/2+90);
                g.setColor(Color.WHITE);
                g.drawString("EXIT GAME",screen.getWidth()/2-60,screen.getHeight()/2+120);
                break;
            case 2:
                g.setColor(Color.WHITE);
                g.drawString("RESUME",screen.getWidth()/2-60,screen.getHeight()/2+60);
                g.setColor(Color.WHITE);
                g.drawString("RESTART",screen.getWidth()/2-60,screen.getHeight()/2+90);
                g.setColor(Color.RED);
                g.drawString("EXIT GAME",screen.getWidth()/2-60,screen.getHeight()/2+120);
                break;
        }
        g.setColor(Color.RED);
        g.drawString("PAUSED",screen.getWidth()/2-15,screen.getHeight()/2+10);
    }
 
    public void drawGameOver(Graphics2D g){
        drawer.draw(g, map, screen.getWidth(), screen.getHeight());
        g.setColor(Color.RED);
        g.drawString("Game Over",screen.getWidth()/2-15,screen.getHeight()/2+10);
    }
    

}