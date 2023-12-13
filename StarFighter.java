import javax.swing.JFrame;
import java.awt.Component;

public class StarFighter extends JFrame
{
  public static final int WIDTH = 500;
  public static final int HEIGHT = 400;

  public StarFighter()
  {
    super("STARFIGHTER");
    setSize(WIDTH,HEIGHT);

    OuterSpace theGame = new OuterSpace();
    ((Component)theGame).setFocusable(true);

    getContentPane().add(theGame);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }

  public static void main( String args[] )
  {
    StarFighter run = new StarFighter();
  }
}
