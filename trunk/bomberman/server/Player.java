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

import bomberman.server.api.Explodable;
import bomberman.server.api.Element;
import java.util.ArrayList;
import java.util.List;

public class Player extends Element implements Explodable
{
  protected List<Bomb> bombs = new ArrayList<Bomb>();
  protected Game   game;
  protected String nickname;
  protected int    id;
  
  // For extras
  protected int bombDistance = 3;
  protected int bombCount    = 1; 

  public Player(Game game, String nickname)
  {
    super(0, 0);
    
    this.game     = game;
    this.nickname = nickname;
  }
  
  // raise if collect BombDistance-Extra
  public void raiseBombDistance()
  {
    this.bombDistance++;
  }
  
  // raise if collect BombCount
  public void raiseBombCount()
  {
    this.bombCount++;
  }

  public String getImageFilename()
  {
    if(getID() == 1)                         
      return "resource/gfx/player1/6.png";
    else if(getID() == 2)
      return "resource/gfx/player2/6.png";
    else if(getID() == 3)
      return "resource/gfx/player3/6.png";
    else if(getID() == 4)
      return "resource/gfx/player4/6.png";
    else
      return null;
  }
  
  public String getNickname()
  {
    return this.nickname;
  }
  
  @Override
  public String toString()
  {
    return this.nickname;
  }

  public int getID() 
  {
    return id;
  }

  void placeBomb()
  {
    if(bombs.size() >= this.bombCount)
      return;
    System.out.println("Spieler " + nickname + " legt Bombe bei " + gridX + "/" + gridY);
    
    Bomb bomb = new Bomb(gridX, gridY, this);
    this.bombs.add(bomb);
    
    this.game.getPlayground().setElement(gridX, gridY, 0, bomb);
  }
  
  public void setGame(Game game)
  {
    this.game = game;
  }
  
  public void setID(int id) 
  {
    this.id = id;
  }
}
