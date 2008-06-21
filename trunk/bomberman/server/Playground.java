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
 * @author Kai Ritterbusch (kai.ritterbusch@googlemail.com)
 * @author Christian Lins (christian.lins@web.de)
 */
public class Playground implements Serializable
{
  public static final int DEFAULT_WIDTH  = 17;
  public static final int DEFAULT_HEIGHT = 15;
  
  /**
   * 3D-matrix. The third level has size 5. 
   * [][][0] is for Bombs and Extras
   * [][][1-4] is for Players.
   */
  private Element[][][] matrix = null;
  private int cols, rows;
  
  public Playground(int cols, int rows)
  {
    this.cols = cols;
    this.rows = rows;
    this.matrix = new Element[cols][rows][5];

    // Initialize the playground
    for (int x = 0; x < cols; x++)
    {
      for (int y = 0; y < rows; y++)
      {
        // Solid borders
        if ((x == 0) || (x == cols - 1) || (y == 0) || (y == rows - 1))
        {
          this.matrix[x][y][0] = new SolidWall(x, y); // A solid wall
        }
        // Solid walls within
        else if ((y % 2 == 0) && (x % 2 == 0))
        {
          this.matrix[x][y][0] = new SolidWall(x, y); // Solid wall
        }
        // Player starting points
        else if ((x == 1 && (y == 1 || y == 2)) || (x == 2 && y == 1) || // top left
                (x == cols - 2 && (y == 1 || y == 2)) || (x == cols - 3 && y == 1) || // top right
                (x == 1 && (y == rows - 2 || y == rows - 3)) || (x == 2 && y == rows - 2) || // lower left
                (x == cols - 2 && (y == rows - 2 || y == rows - 3)) 
                  || (x == cols - 3 && y == rows - 2) // lower right
                )
        {
          // Make no walls
          continue;
        }
        else if (Math.random() >= 0.2) // 20% of the area should be empty
        {
          matrix[x][y][0] = new ExplodableWall(x, y); // Exploadable wall
 
          // Extras are placed later when a Wall explodes.
        }
      }
    }
  }

  public int getWidth()
  {
    return cols;
  }
  
  public int getHeight()
  {
    return rows;
  }
  
  public Element[] getElement(int x, int y)
  {
    try
    {
      return this.matrix[x][y];
    }
    catch(ArrayIndexOutOfBoundsException ex)
    {
      return null;
    }   
  }
  
  public void setElement(int x, int y, int layer, Element e)
  {
    this.matrix[x][y][layer] = e;
  }
}
