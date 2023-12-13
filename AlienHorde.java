import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.List;

public class AlienHorde {
  private List < Alien > aliens;
  private int size;

  //Scores

  public AlienHorde(int size) {
    aliens = new ArrayList <Alien> (size);
    this.size = size;

    int y = 20;
    for (int rows = 0; rows < 2; rows++) {
      int x = 10;
      for(int alien = 0; alien < size; alien++) {
        aliens.add(new Alien(x, y, 10));
        x += 60;
      }
      y += 60;
    }

    System.out.println(aliens);
  }

  public int getSize() {
    return size;
  }

  public List < Alien > getAliens() {
    return aliens;
  }

  public void add(Alien al) {
    aliens.add(al);
  }

  public void draw(Graphics window) {
    for (Alien alien: aliens) {
      alien.draw(window);
    }
  }

  public void move(String direction) {
    for (Alien a: aliens) {
      if (direction.equalsIgnoreCase("LEFT")) {
        a.move("LEFT");
      } else if (direction.equalsIgnoreCase("RIGHT")) {
        a.move("RIGHT");
      } else if (direction.equalsIgnoreCase("DOWN")) {
        a.move("DOWN");
      }
    }
  }

  public boolean hordeAtBounds() {
    for (Alien a: aliens) {
      if (a.getY() < StarFighter.WIDTH-25) {
        return false;
      }
    }
    return true;
  }

  // calculate if Aliens are hit by shots, if so remove the shot and alien and return the number of hits
  public int calcHits(List < Ammo > shots) {
    int hits = 0;
    for (int i = 0; i < aliens.size(); i++) {
      for (int j = 0; j < shots.size(); j++) {
        if (aliens.get(i).didCollide(shots.get(j))) {
          aliens.remove(i);
          shots.remove(j);
          hits++;
        }
      }
    }
    return hits;
  }

  public String toString() {
    return "";
  }
}