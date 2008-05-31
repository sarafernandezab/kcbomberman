package bomberman.server;

import java.io.Serializable;
import java.util.Random;

public class Session implements Serializable
{
  static long serialVersionUID = 103984384;
  
  private int sessionID;
  private int hashCode;
    
  public Session()
  {
    Random rn = new Random();
    this.sessionID = rn.nextInt();
       
    this.hashCode = rn.nextInt();
  }

  @Override
  public boolean equals(Object obj)
  {
    return hashCode() == obj.hashCode();
  }
  
  public int getID()
  {
    return this.sessionID;
  }      

  /**
   * It is necessary to override this method that instance recognition
   * works over VM borders.
   * @return
   */
  @Override
  public int hashCode() 
  {
    return this.hashCode; 
  }

}
