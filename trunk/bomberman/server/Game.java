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
  private Playground         playground;

  public Game(String name, Session creator)
  {
    this.gameName = name;
    this.creator  = creator;
    this.playground = new Playground(14, 16);
  }
  
  public Session getCreator()
  {
    return this.creator;
  }
  
  public ArrayList<Session> getPlayers()
  {
    return this.players;
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
}
