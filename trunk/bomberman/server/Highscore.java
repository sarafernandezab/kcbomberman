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

import bomberman.util.Pair;
import java.util.HashMap;

/**
 *
 * @author Christian Lins (christian.lins@web.de)
 */
public class Highscore 
{
  // Stores won and lost games per username
  private HashMap<String, Pair<Integer, Integer>> data = new HashMap<String, Pair<Integer, Integer>>();

  public int hasLostGame(String username)
  {
    int lostGames = this.data.get(username).getB();
    lostGames++;
    this.data.get(username).setB(lostGames);
    return lostGames;
  }
  
  public int hasWonGame(String username)
  {
    int wonGames = this.data.get(username).getA();
    wonGames++;
    this.data.get(username).setA(wonGames);
    return wonGames;
  }
}
