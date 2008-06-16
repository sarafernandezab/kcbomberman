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
import java.util.ArrayList;
import java.util.List;

public class Player extends Element
{
  protected List<Bomb> bombs = new ArrayList<Bomb>();
  protected String nickname;
  protected int    id;
     
  public Player(String nickname)
  {
    this.nickname = nickname;
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

  public void setID(int id) 
  {
    this.id = id;
  }
  
  public void setPosition(int x, int y)
  {
    this.gridX = x;
    this.gridY = y;
  }
}
