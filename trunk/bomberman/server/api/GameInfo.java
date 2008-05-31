/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bomberman.server.api;

import java.io.Serializable;

/**
 * Contains the information for one Game. This class is used to update
 * clients with new game information.
 * @author Christian Lins
 */
public class GameInfo implements Serializable
{
  private String gameName = null;
  
  public GameInfo(String gameName)
  {
    this.gameName = gameName;
  }
  
  public String getName()
  {
    return this.gameName;
  }
}
