import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Canvas;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import static java.lang.Character.*;
import java.awt.image.BufferedImage;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.Timer;

public class OuterSpace extends Canvas implements KeyListener, Runnable {
  private Ship ship;

  private AlienHorde horde;
  private Bullets shots;
  private Bullets alienShots;

  private boolean[] keys;
  private BufferedImage back;

  private int playerScore = 0;
  private int alienScore = 0;

  private Random rand = new Random();

  private int LIVES = 3;

  boolean isLifeLost = false;
  boolean isGameOver = false;
  boolean playerWins = false;

  private long alienMoveTimer = 0;
  private long alienShootTimer = 0;

  public OuterSpace() {
    setBackground(Color.black);

    keys = new boolean[6];

    // instantiate other instance variables
    // Ship, Alien
    ship = new Ship();
    ship.setX(80);
    ship.setY(300);
    ship.setSpeed(1);

    /* TODO: Change Alien Population, and base it off of window dimensions */
    horde = new AlienHorde(8);
    shots = new Bullets();
    alienShots = new Bullets();

    this.addKeyListener(this);
    new Thread(this).start();

    setVisible(true);
  }

  public void update(Graphics window) {
    paint(window);
  }

  public void paint(Graphics window) {
    // set up the double buffering to make the game animation nice and smooth
    Graphics2D twoDGraph = (Graphics2D) window;

    // take a snap shop of the current screen and same it as an image
    // that is the exact same width and height as the current screen
    if (back == null)
      back = (BufferedImage) (createImage(getWidth(), getHeight()));

    // create a graphics reference to the back ground image
    // we will draw all changes on the background image
    Graphics graphToBack = back.createGraphics();

    if (isGameOver) {
      graphToBack.setColor(Color.BLACK);
      graphToBack.fillRect(0, 0, getWidth(), getHeight());
      graphToBack.setColor(Color.WHITE);
      graphToBack.drawString("Game Over! Your score: " + playerScore, 25, 50);
      if (keys[5]) {
        playerScore = 0;
        alienScore = 0;
        isGameOver = false;
        isLifeLost = false;
        horde = new AlienHorde(8);
        shots = new Bullets();
        alienShots = new Bullets();
        keys[5] = false;
      }
      twoDGraph.drawImage(back, null, 0, 0);
    } else if (isLifeLost) {
      // System.out.println("Life Lost");
      graphToBack.setColor(Color.BLACK);
      graphToBack.fillRect(0, 0, getWidth(), getHeight());
      graphToBack.setColor(Color.WHITE);
      graphToBack.drawString("Life lost! Press R to resume.", 50, 200);
      if (keys[5]) {
        playerScore = 0;
        alienScore = 0;
        isGameOver = false;
        isLifeLost = false;
        horde = new AlienHorde(8);
        shots = new Bullets();
        alienShots = new Bullets();
        keys[5] = false;
      }
      twoDGraph.drawImage(back, null, 0, 0);
    } else if (playerWins) {
      graphToBack.setColor(Color.BLACK);
      graphToBack.fillRect(0, 0, getWidth(), getHeight());
      graphToBack.setColor(Color.WHITE);
      graphToBack.drawString("Congrats, You won! Your score is " + playerScore, 25, 50);
      twoDGraph.drawImage(back, null, 0, 0);
    } else {
      graphToBack.setColor(Color.BLUE);
      graphToBack.drawString("StarFighter ", 25, 50);
      graphToBack.setColor(Color.BLACK);
      graphToBack.fillRect(0, 0, 500, 400);

      if (keys[0]) {
        ship.move("LEFT");
      }
      if (keys[1]) {
        ship.move("RIGHT");
      }
      if (keys[2]) {
        ship.move("UP");
      }
      if (keys[3]) {
        ship.move("DOWN");
      }
      if (keys[4]) {
        shots.add(new Ammo(ship.getX() + 22, ship.getY() - 5));
        keys[4] = false;
      }
      if (keys[5]) {
        playerScore = 0;
        alienScore = 0;
        isGameOver = false;
        isLifeLost = false;
        horde = new AlienHorde(8);
        shots = new Bullets();
        alienShots = new Bullets();
        keys[5] = false;
      }

      // add code to move Ship, Alien, etc.
      ship.draw(graphToBack);
      horde.draw(graphToBack);

      if (alienMoveTimer % 500 == 0) {
        horde.move("DOWN");
      }

      shots.draw(graphToBack);
      shots.move("UP");
      playerScore += horde.calcHits(shots.getList());
      alienScore = ship.calcHits(alienShots.getList());

      if ((alienShootTimer%50 == 0) && alienShots.getList().size() < 5) {
        int randAlien = rand.nextInt(horde.getAliens().size());
        Alien alien = horde.getAliens().get(randAlien);
        alienShots.add(new Ammo(alien.getX() + 15, alien.getY() + 35, 2));
      }

      alienShots.draw(graphToBack, Color.PINK);
      alienShots.move("DOWN");

      shots.cleanUpEdges();
      alienShots.cleanUpEdges();

      // Draw Score
      LIVES -= alienScore;
      graphToBack.setColor(Color.WHITE);
      graphToBack.drawString("Player Score: " + playerScore, 5, getHeight() - 70);
      graphToBack.drawString("Lives Remaining: " + (LIVES), 5, getHeight() - 30);

      if (alienScore > 0) {
        isLifeLost = true;
      }
      if (LIVES <= 0) {
        isGameOver = true;
      }

      if (horde.hordeAtBounds()) {
        playerWins = true;
      }

      alienMoveTimer++;
      alienShootTimer++;
      twoDGraph.drawImage(back, null, 0, 0);
    }
  }

  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
      keys[0] = true;
    }
    if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
      keys[1] = true;
    }
    if (e.getKeyCode() == KeyEvent.VK_UP) {
      keys[2] = true;
    }
    if (e.getKeyCode() == KeyEvent.VK_DOWN) {
      keys[3] = true;
    }
    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
      keys[4] = true;
    }
    if (e.getKeyCode() == KeyEvent.VK_R) {
      keys[5] = true;
    }
    repaint();
  }

  public void keyReleased(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
      keys[0] = false;
    }
    if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
      keys[1] = false;
    }
    if (e.getKeyCode() == KeyEvent.VK_UP) {
      keys[2] = false;
    }
    if (e.getKeyCode() == KeyEvent.VK_DOWN) {
      keys[3] = false;
    }
    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
      keys[4] = false;
    }
    if (e.getKeyCode() == KeyEvent.VK_R) {
      keys[5] = false;
    }
    repaint();
  }

  public void keyTyped(KeyEvent e) {
    // no code needed here
  }

  public void run() {
    try {
      while (true) {
        Thread.currentThread().sleep(5);
        repaint();
      }
    } catch (Exception e) {
    }
  }
}