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
  private String creator  = null;
  
  public GameInfo(String gameName, String creator)
  {
    this.gameName = gameName;
    this.creator  = creator;
  }
  
  public String getName()
  {
    return this.gameName;
  }
  
  public String getCreator()
  {
    return this.creator;
  }
}
