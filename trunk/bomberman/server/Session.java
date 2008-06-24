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
import java.util.Random;

/**
 * Creates randomized Session.
 * @author Kai Ritterbusch (kai.ritterbusch@fh-osnabrueck.de)
 * @author Christian Lins (christian.lins@web.de)
 */
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
