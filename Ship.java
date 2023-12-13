import java.io.File;
import java.net.URL;
import java.util.List;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.imageio.ImageIO;

public class Ship extends MovingThing {
  private int speed;
  private Image image;

  public Ship() {
    this(10, 10, 50, 50, 10);
  }

  public Ship(int x, int y) {
    super(x, y);
  }

  public Ship(int x, int y, int s) {
    super(x, y);
    speed = s;
  }

  // all ctors call this ctor
  public Ship(int x, int y, int w, int h, int s) {
    super(x, y, w, h);
    speed = s;
    try {
      URL url = getClass().getResource("ship.jpg");
      image = ImageIO.read(url);
    } catch (Exception e) {
      // feel free to do something here
    }
  }

  public void setSpeed(int s) {
    speed = s;
  }

  public int getSpeed() {
    return speed;
  }

  public int calcHits(List <Ammo> shots) {
    int hits = 0;
    for (int j = 0; j < shots.size(); j++) {
      if (this.didCollide(shots.get(j))) {
        shots.remove(j);
        hits++;
      }
    }
    return hits;
  }

  public void move(String direction) {
    if (direction.equalsIgnoreCase("LEFT")) {
      setX(getX() - speed);
    } else if (direction.equalsIgnoreCase("RIGHT")) {
      setX(getX() + speed);
    } else if (direction.equalsIgnoreCase("UP")) {
      setY(getY() - speed);
    } else if (direction.equalsIgnoreCase("DOWN")) {
      setY(getY() + speed);
    }
  }

  public void draw(Graphics window) {
    window.drawImage(image, getX(), getY(), getWidth(), getHeight(), null);
  }

  public String toString() {
    return super.toString() + getSpeed();
  }
}
