package bomberman.server;

import bomberman.server.api.Element;

/**
 * Playground.
 * @author Kai Ritterbusch
 * @author Christian Lins
 */
public class Playground 
{
  private Element[][] matrix = null;
  
  public Playground(int cols, int rows)
  {
    this.matrix = new Element[cols][rows];
  }
}
