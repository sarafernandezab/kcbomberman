/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bomberman.server;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Kai Ritterbusch
 * @author Christian Lins
 */
public class Game implements Serializable
{
  private Session            creator   = null;
  private String             gameName  = null;
  private ArrayList<Session> players   = new ArrayList<Session>();

  public Game(String name, Session creator)
  {
    this.gameName = name;
    this.creator  = creator;
  }
  
  public Session getCreator()
  {
    return this.creator;
  }
  
  public ArrayList<Session> getPlayers()
  {
    return this.players;
  }
}
