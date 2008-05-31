package bomberman.server;

/**
 * Represents a wall on the playground.
 * @author chris
 */
public class Wall 
{
  private boolean exploadable = false;
  
  public Wall(boolean exploadable)
  {
    this.exploadable = exploadable;
  }
}
