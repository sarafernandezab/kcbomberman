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
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * An AI-controlled player. The AI uses a modified A* algorithm for
 * path finding.
 * @author Christian Lins (christian.lins@web.de)
 */
class AIPlayer extends Player
{
  private List<int[]> currentPath = new ArrayList<int[]>();
  private Playground  playground;
  
  public AIPlayer(Game g, Playground playground)
  {
    super(g, "KI-Knecht");
    
    this.nickname += hashCode();
    
    if(g == null || playground == null)
      throw new IllegalArgumentException();
    
    this.game       = g;
    this.playground = playground;
    
    Thread thread = new AIPlayerThread(this, g);
    thread.start();
  }
  
  private boolean containsExplodable(Element[] elements)
  {
    for(Element e : elements)
      if(e instanceof Explodable)
        return true;
    
    return false;
  }
  
  private boolean contains(Element[] elements, Element c)
  {
    for(Element e : elements)
      if(c.equals(e))
        return true;
    
    return false;
  }
  
  private boolean isTargetZone(Point pnt)
  {
    // Determine all possible neighbours...
    Element[] n1 = this.playground.getElement(pnt.x + 1, pnt.y);
    Element[] n2 = this.playground.getElement(pnt.x - 1, pnt.y);
    Element[] n3 = this.playground.getElement(pnt.x, pnt.y + 1);
    Element[] n4 = this.playground.getElement(pnt.x, pnt.y - 1);
			
    if(containsExplodable(n1) || 
       containsExplodable(n2) || 
       containsExplodable(n3) || 
       containsExplodable(n4))
    {
      if(contains(n1, this) || contains(n2, this) || contains(n3, this) || contains(n4, this))
        return false;
      else
        return true;
    }
    else
      return false;
  }
  
  /**
   * Calculates a path to a possible bombing spot
   */
  private List<int[]> calculateTargetPath()
  {			
    // The A* Algorithm			
    List<int[]> openNodes   = new ArrayList<int[]>(); // Noch nicht besuchte Nodes
    List<int[]> closedNodes = new ArrayList<int[]>(); // Schon abgearbeitete Punkte
			
    // Mit Startpunkt initialisieren (add = push = at end of list)
    openNodes.add(new int[] {gridX, gridY, 0, 0}); // Starting point (gridX, gridY) is the current player position
    while(openNodes.size() > 0)
    {
      int[] node = openNodes.remove(0); // pop()
      if(isTargetZone(new Point(node[0], node[1])) || closedNodes.size() > 15) // Ist der Nachbarpunkt sprengbar?
      {
        // Den Pfad zurückverfolgen
        List<int[]> path = new ArrayList<int[]>();
        path.add(0, node);
        while(closedNodes.size() > 0)
          path.add(0, closedNodes.remove(0));
					
        //trace("Target path length: " + path.length);
        return path;
      }
      else
      {
        int r1 = Math.random() > 0.5 ? 1 : -1;
        int r2 = r1 == 1 ? -1 : 1;
					
        // Alle möglichen Nachbarn von node herausfinden
        Element[] n1a = this.playground.getElement(node[0] + r1, node[1]);
        Element n1 = n1a == null ? null : n1a[0];
        Element[] n2a = this.playground.getElement(node[0] + r2, node[1]);
        Element n2 = n2a == null ? null : n2a[0];
        Element[] n3a = this.playground.getElement(node[0], node[1] + r1);
        Element n3 = n3a == null ? null : n3a[0];
        Element[] n4a = this.playground.getElement(node[0], node[1] + r2);
        Element n4 = n4a == null ? null : n4a[0];
					
        boolean saveNode = false;
					
        if(n1 == null && node[2] != node[0] + r1)
        {
          openNodes.add(0, new int[] {node[0] + r1, node[1], node[0], node[1]});
          saveNode = true;
        }
        if(n2 == null && node[2] != node[0] + r2)
        {
          openNodes.add(0, new int[] {node[0] + r2, node[1], node[0], node[1]});
          saveNode = true;
        }
        if(n3 == null && node[3] != node[1] + r1)
        {
          openNodes.add(0, new int[] {node[0], node[1] + r1, node[0], node[1]});
          saveNode = true;
        }
        if(n4 == null && node[3] != node[1] + r2)
        {
          openNodes.add(0, new int[] {node[0], node[1] + r2, node[0], node[1]});
          saveNode = true;
        }

        if(saveNode)
          closedNodes.add(0, node);
      }
    }		
    return null;
  }

  // Funktion zum Berechnen eines Fluchtweges
  private List<int[]> calculateHidePath(Element bomb)
  {
    if(bomb == null)
      return null;
    
    int x = bomb.getX();
    int y = bomb.getY();
			
    // Der A* Algorithmus			
    List<int[]> openNodes   = new ArrayList<int[]>();   // Noch nicht besuchte Nodes
    List<int[]> closedNodes = new ArrayList<int[]>(); // Schon  abgearbeitete Punkte
    
    // Mit Startpunkt initialisieren
    openNodes.add(0, new int[] {x, y, 0, 0}); // Startpunkt (x, y) ist die Bombe
    while(openNodes.size() > 0)
    {
      int[] node = openNodes.remove(0);
      if(node[0] != x && node[1] != y || closedNodes.size() > 15) // Ist der Punkt sicher?
      {
        // Den Pfad zurückverfolgen
        List<int[]> path = new ArrayList<int[]>();
        path.add(0, node);
        while(closedNodes.size() > 0)
          path.add(0, closedNodes.remove(0));

        //trace("Hide path length: " + path.length);
        return path;
      }
      else
      {
        // Alle möglichen Nachbarn von node herausfinden
        Element[] n1a = this.playground.getElement(node[0]+1, node[1]);
        Element n1 = n1a == null ? null : n1a[0];
        Element[] n2a = this.playground.getElement(node[0]-1, node[1]);
        Element n2 = n2a == null ? null : n2a[0];
        Element[] n3a = this.playground.getElement(node[0], node[1]+1);
        Element n3 = n3a == null ? null : n3a[0];
        Element[] n4a = this.playground.getElement(node[0], node[1]-1);
        Element n4 = n4a == null ? null : n4a[0];

        boolean saveNode = false;
					
        if(n1 == null && node[2] != node[0]+1)
        {
          openNodes.add(0, new int[] {node[0]+1, node[1], node[0], node[1]});
          saveNode = true;
        }
        if(n2 == null && node[2] != node[0]-1)
        {
          openNodes.add(0, new int[] {node[0]-1, node[1], node[0], node[1]});
          saveNode = true;
        }
        if(n3 == null && node[3] != node[1]+1)
        {
          openNodes.add(0, new int[] {node[0], node[1]+1, node[0], node[1]});
          saveNode = true;
        }
        if(n4 == null && node[3] != node[1]-1)
        {
          openNodes.add(0, new int[]{node[0], node[1]-1, node[0], node[1]});
          saveNode = true;
        }

        if(saveNode)
          closedNodes.add(0, node);
      }
    }		

    return null;
  }
		
  // Prüft ob eine Bombe in meiner Nähe liegt.
  // Gibt false zurück, wenn keine Bombe in der Nähe ist.
  private Element checkForBomb(Point bomb)
  {
    int matrixX = this.gridX;
    int matrixY = this.gridY;
			
    if(bomb != null)
    {
      matrixX = bomb.x;
      matrixY = bomb.y;
    }

    // Spieler steht auf der Bombe
    if(this.playground.getElement(matrixX, matrixY)[0] instanceof Bomb)
      return this.playground.getElement(matrixX, matrixY)[0];
 
    // Spieler steht in der Naehe der Bombe, i < bombdistance
    for(int i = 0; i < 4; i++)
    {
      if(this.playground.getElement(matrixX+i, matrixY) != null &&
         this.playground.getElement(matrixX+i, matrixY)[0] instanceof Bomb)
        return this.playground.getElement(matrixX+i, matrixY)[0];
      else if(this.playground.getElement(matrixX-i, matrixY) != null &&
              this.playground.getElement(matrixX-i, matrixY)[0] instanceof Bomb)		   
        return this.playground.getElement(matrixX-i, matrixY)[0];
      else if(this.playground.getElement(matrixX, matrixY+i) != null &&
              this.playground.getElement(matrixX, matrixY+i)[0] instanceof Bomb)
        return this.playground.getElement(matrixX, matrixY+i)[0];
      else if(this.playground.getElement(matrixX, matrixY-i) != null &&
              this.playground.getElement(matrixX, matrixY-i)[0] instanceof Bomb)
        return this.playground.getElement(matrixX, matrixY-i)[0];
      else if(this.playground.getElement(matrixX, matrixY) != null &&
              this.playground.getElement(matrixX, matrixY)[0] instanceof Bomb)
        return this.playground.getElement(matrixX, matrixY)[0]; 
    }
    return null;
  }

  private int randomDirection()
  {
    return Math.round(new Random().nextFloat() * 2) - 1;
  }
  
  private boolean move(int dx, int dy)
  {
    System.out.println(this.nickname + " laeuft in Richtung " + dx + "/" + dy);
    boolean moved = this.game.movePlayer(this, dx, dy);
    
    if(moved)
    {
      this.game.forcePlaygroundUpdate();
    }
    
    return moved;
  }
  
  /**
   * A small step for AI...
   */
  public void tick()
  {
    if(!this.game.isRunning())
      return;
    
    // In dieser Methode werden die Aktionen des KI-Spielers ausgeführt.
    // Dabei ist zwischen verschiedenen Aktionen zu unterscheiden:
    // - Stelle für Bombe suchen
    // - Bombe plazieren
    // - Deckung suchen und auf Bombe warten

    if(currentPath.size() > 0) // Solange noch ein Pfad existiert, laufen wir
    {
      int[] node = currentPath.remove(0);
      if(!move(node[0] - gridX, node[1] - gridY)) // Move expects relative direction
        currentPath = new ArrayList<int[]>();     // Delete path because it must be invalid
    }
    else if(bombs.size() < 1) // Es kann noch ne Bombe gelegt werden
    {
      if(isTargetZone(new Point(gridX, gridY)))
      {
        placeBomb();
        currentPath = calculateHidePath(checkForBomb(new Point(gridX, gridY)));
        if(currentPath == null)
        {
          currentPath = new ArrayList<int[]>();
		placeBomb(); // Suicide
        }
      }
      else
      {
        currentPath = calculateTargetPath();
        if(currentPath == null)
          currentPath = new ArrayList<int[]>();
      }
    }
    else
    {
      Element bomb = checkForBomb(null);
      if(bomb == null)
        return;
      currentPath = calculateHidePath(bomb);
      if(currentPath == null)
      {
        currentPath = new ArrayList<int[]>();
      }
    }
  }
}