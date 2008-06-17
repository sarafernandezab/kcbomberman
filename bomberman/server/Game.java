/*
 *  KC Bomberman
 *  Copyright 2008 Christian Lins <christian.lins@web.de>
 *  Copyright 2008 Kai Ritterbusch <kai.ritterbusch@googlemail.com>
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package bomberman.server;

import bomberman.server.api.Element;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kai Ritterbusch
 * @author Christian Lins
 */
public class Game implements Serializable
{
  public static final int DEFAULT_WIDTH  = 16;
  public static final int DEFAULT_HEIGHT = 14;
  
  private Session       creator        = null;
  private String        gameName       = null;
  private List<Session> playerSessions = new ArrayList<Session>();
  private List<Player>  players        = new ArrayList<Player>();
  private Playground    playground;
  private boolean       playgroundUpdateRequired = false;
  private boolean       running        = false;
  
  public Game(String name, Session creator)
  {
    this.gameName = name;
    this.creator  = creator;
    this.playground = new Playground(DEFAULT_WIDTH, DEFAULT_HEIGHT);
  }
  
  /**
   * Fills up the game with AI-controlled players.
   */
  public void addAI()
  {
    while(players.size() < 4)
      addPlayer(new AIPlayer(this, playground));
  }
  
  // Adds a player to the playground
  public void addPlayer(Player player)
  {
    player.setID(players.size() + 1);
    
    // Adds player to playground view, set starting position
    int x = 0;
    int y = 0;
    if(player.getID() == 1)
    {
      x = 1;
      y = 1;
    }
    else if(player.getID() == 2)
    {
      x = playground.getWidth() - 2;
      y = playground.getHeight() - 2;
    }
    else if(player.getID() == 3)
    {
      x = 1;
      y = playground.getHeight() - 2;      
    }
    else if(player.getID() == 4)
    {
      x = playground.getWidth() - 2;
      y = 1;
    }    
    this.playground.setElement(x, y, player);
    player.setPosition(x, y);
    
    players.add(player);
    
    System.out.println("Player"+ player.getID() +"added to Playground ("+player.getNickname() +")");
  }
  
  /**
   * Forces the server to update the Playground, e.g. when an AI player has moved.
   */
  public void forcePlaygroundUpdate()
  {
    this.playgroundUpdateRequired = true;
  }
  
  public boolean isPlaygroundUpdateRequired()
  {
    boolean update = this.playgroundUpdateRequired;
    this.playgroundUpdateRequired = false;
    
    return update;
  }
  
  // Removes a player to the playground
  public void removePlayer(int x, int y, Player player)
  {  
    if(this.playground.getElement(x, y).equals(player))   // Removes only the selected player 
      this.playground.setElement(x, y, null);    
  }
  
  /**
   * Moves a player in the game's playground if possible.
   * @param player
   * @param dx
   * @param dy
   * @return
   */
  public boolean movePlayer(Player player, int dx, int dy)
  {
    // Check if we can move in that direction
    int nx = player.getX() + dx;
    int ny = player.getY() + dy;
    
    if(nx < 0 || ny < 0 
            || this.playground.getWidth() <= nx 
            || this.playground.getHeight() <= ny)
      return false;
    
    Element el = this.playground.getElement(nx, ny)[0];
    if(el == null) // oder Extra
    {
      // Set old position in Playground to null...
      this.playground.setElement(player.getX(), player.getY(), null);
      // ...and set new position
      player.setPosition(nx, ny);
      this.playground.setElement(player.getX(), player.getY(), player);
      
      return true;
    }
    else
      return false;
  }
  
  public Session getCreator()
  {
    return this.creator;
  }
  
  public List<Session> getPlayerSessions()
  {
    return this.playerSessions;
  }
  
  @Override
  public String toString()
  {
    return this.gameName;
  }

  public Playground getPlayground() 
  {
    return playground;
  }

  public void setPlayground(Playground playground) 
  {
    this.playground = playground;
  }

  public boolean isRunning() 
  {
    return running;
  }

  public void setRunning(boolean running) 
  {
    this.running = running;
  }
}
