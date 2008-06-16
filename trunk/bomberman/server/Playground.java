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

/**
 * Logical Playground where the Server manages a game.
 * @author Kai Ritterbusch
 * @author Christian Lins
 */
public class Playground implements Serializable
{

  private Element[][] matrix = null;

  public Playground(int cols, int rows)
  {
    this.matrix = new Element[cols][rows];

    // Initialize the playground
    for (int x = 0; x < cols; x++)
    {
      for (int y = 0; y < rows; y++)
      {
        matrix[x][y] = null;

        // Solid borders
        if ((x == 0) || (x == cols - 1) || (y == 0) || (y == rows - 1))
        {
          this.matrix[x][y] = new Wall(false); // A solid wall
        }
        // Player starting points
        else if ((x == 1 && (y == 1 || y == 2)) || (x == 2 && y == 1) || // Links oben
                (x == cols - 2 && (y == 1 || y == 2)) || (x == cols - 3 && y == 1) || // Rechts oben
                (x == 1 && (y == rows - 2 || y == rows - 3)) || (x == 2 && y == rows - 2) || // Links unten
                (x == cols - 2 && (y == rows - 2 || y == rows - 3)) || (x == cols - 3 && y == rows - 3) // Rechts unten
                )
        {
          continue;
        }
        // Solid walls within
        else if ((y % 2 == 0) && (x % 2 == 0))
        {
          this.matrix[x][y] = new Wall(false); // Solid wall
        }
        else
        {
          if (Math.random() < 0.2) // 20% of the area should be empty
          {
            continue;
          }
          else
          {
            matrix[x][y] = new Wall(true); // Exploadable wall
 
            // Extras are placed later when a Wall explodes.
          }
        }
      }
    }
  }

  public Element getElement(int x, int y)
  {
    return this.matrix[x][y];
  }

  public void setElement(int x, int y, Element e)
  {
    this.matrix[x][y] = e;
  }
}
