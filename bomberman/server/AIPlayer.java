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
import java.util.Timer;
import java.util.TimerTask;

/**
 * An AI-controlled player. The AI uses a modified A* algorithm for
 * path finding.
 * @author Christian Lins (christian.lins@web.de)
 */
class AIPlayer extends Player
{
  private List<int[]> currentPath = new ArrayList<int[]>();
  private Game        game;
  private Playground  playground;
  
  public AIPlayer(Game game, Playground playground)
  {
    super("KI-Knecht");
    
    this.game       = game;
    this.playground = playground;
    
    // Set working timer
    Timer timer = new Timer();
    timer.schedule(new TimerTask()
    {
      public void run()
      {
        tick();
      }
    }, 0, 500);
  }
  
  private boolean isTargetZone(Point pnt)
  {
    // Alle möglichen Nachbarn von node herausfinden
    Element n1 = this.playground.getElement(pnt.x + 1, pnt.y);
    Element n2 = this.playground.getElement(pnt.x - 1, pnt.y);
    Element n3 = this.playground.getElement(pnt.x, pnt.y + 1);
    Element n4 = this.playground.getElement(pnt.x, pnt.y - 1);
			
    if(n1 instanceof Explodable || 
       n2 instanceof Explodable || 
       n3 instanceof Explodable || 
       n4 instanceof Explodable)
    {
      if(n1 == this || n2 == this || n3 == this || n4 == this)
        return false;
      else
        return true;
    }
    else
      return false;
  }
  
  // Funktion zum Berechnen eines Weges zu einem Bombenpunkt
  private List<int[]> calculateTargetPath()
  {
    int x = super.gridX;
    int y = super.gridY;
			
    // The A* Algorithm			
    List<int[]> openNodes   = new ArrayList<int[]>(); // Noch nicht besuchte Nodes
    List<int[]> closedNodes = new ArrayList<int[]>(); // Schon abgearbeitete Punkte
			
    // Mit Startpunkt initialisieren (add = push = at end of list)
    openNodes.add(new int[] {x, y, 0, 0}); // Startpunkt (x, y) ist die Player-Position
    while(openNodes.size() > 0)
    {
      int[] node = openNodes.remove(0); // pop()
      if(isTargetZone(new Point(node[0], node[1])) || closedNodes.size() > 15) // Ist der Nachbarpunkt sprengbar?
      {
        // Den Pfad zurückverfolgen
        List<int[]> path = new ArrayList<int[]>();
        path.add(node);
        while(closedNodes.size() > 0)
          path.add(closedNodes.remove(0));
					
        //trace("Target path length: " + path.length);
        return path;
      }
      else
      {
        int r1 = Math.random() > 0.5 ? 1 : -1;
        int r2 = r1 == 1 ? -1 : 1;
					
        // Alle möglichen Nachbarn von node herausfinden
        Element n1 = this.playground.getElement(node[0] + r1, node[1]);
        Element n2 = this.playground.getElement(node[0] + r2, node[1]);
        Element n3 = this.playground.getElement(node[0], node[1] + r1);
        Element n4 = this.playground.getElement(node[0], node[1] + r2);
					
        boolean saveNode = false;
					
        if(n1 == null && node[2] != node[0] + r1)
        {
          openNodes.add(new int[] {node[0] + r1, node[1], node[0], node[1]});
          saveNode = true;
        }
        if(n2 == null && node[2] != node[0] + r2)
        {
          openNodes.add(new int[] {node[0] + r2, node[1], node[0], node[1]});
          saveNode = true;
        }
        if(n3 == null && node[3] != node[1] + r1)
        {
          openNodes.add(new int[] {node[0], node[1] + r1, node[0], node[1]});
          saveNode = true;
        }
        if(n4 == null && node[3] != node[1] + r2)
        {
          openNodes.add(new int[] {node[0], node[1] + r2, node[0], node[1]});
          saveNode = true;
        }

        if(saveNode)
          closedNodes.add(node);
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
    openNodes.add(new int[] {x, y, 0, 0}); // Startpunkt (x, y) ist die Bombe
    while(openNodes.size() > 0)
    {
      int[] node = openNodes.remove(0);
      if(node[0] != x && node[1] != y) // Ist der Punkt sicher?
      {
        // Den Pfad zurückverfolgen
        List<int[]> path = new ArrayList<int[]>();
        path.add(node);
        while(closedNodes.size() > 0)
          path.add(closedNodes.remove(0));

        //trace("Hide path length: " + path.length);
        return path;
      }
      else
      {
        // Alle möglichen Nachbarn von node herausfinden
        Element n1 = this.playground.getElement(node[0]+1, node[1]);
        Element n2 = this.playground.getElement(node[0]-1, node[1]);
        Element n3 = this.playground.getElement(node[0], node[1]+1);
        Element n4 = this.playground.getElement(node[0], node[1]-1);

        boolean saveNode = false;
					
        if(n1 == null && node[2] != node[0]+1)
        {
          openNodes.add(new int[] {node[0]+1, node[1], node[0], node[1]});
          saveNode = true;
        }
        if(n2 == null && node[2] != node[0]-1)
        {
          openNodes.add(new int[] {node[0]-1, node[1], node[0], node[1]});
          saveNode = true;
        }
        if(n3 == null && node[3] != node[1]+1)
        {
          openNodes.add(new int[] {node[0], node[1]+1, node[0], node[1]});
          saveNode = true;
        }
        if(n4 == null && node[3] != node[1]-1)
        {
          openNodes.add(new int[]{node[0], node[1]-1, node[0], node[1]});
          saveNode = true;
        }

        if(saveNode)
          closedNodes.add(node);
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
    if(this.playground.getElement(matrixX, matrixY) instanceof Bomb)
      return this.playground.getElement(matrixX, matrixY);
 
    // Spieler steht in der Naehe der Bombe, i < bombdistance
    for(int i = 0; i < 4; i++)
    {
      if(this.playground.getElement(matrixX+i, matrixY) instanceof Bomb)
        return this.playground.getElement(matrixX+i, matrixY);
      else if(this.playground.getElement(matrixX-i, matrixY) instanceof Bomb)		   
        return this.playground.getElement(matrixX-i, matrixY);
      else if(this.playground.getElement(matrixX, matrixY+i) instanceof Bomb)
        return this.playground.getElement(matrixX, matrixY+i);
      else if(this.playground.getElement(matrixX, matrixY-i) instanceof Bomb)
        return this.playground.getElement(matrixX, matrixY-i);
      else if(this.playground.getElement(matrixX, matrixY) instanceof Bomb)
        return this.playground.getElement(matrixX, matrixY); 
    }
    return null;
  }

  private int randomDirection()
  {
    return Math.round(new Random().nextFloat() * 2) - 1;
  }
  
  private boolean move(int dx, int dy, boolean weissnich)
  {
    //System.out.println(this.nickname + " laeuft in Richtung " + dx + "/" + dy);
    boolean moved = this.game.movePlayer(this, dy, dx);
    
    if(moved)
    {
      this.game.forcePlaygroundUpdate();
    }
    
    return moved;
  }
  
  private void placeBomb()
  {
    //System.out.println(this.nickname + " has placed bomb at " + gridX + "/" + gridY);
  }
  
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
      if(!move((node[0] - gridX), (node[1] - gridY), true))
        currentPath = new ArrayList<int[]>(); // Lösche Pfad
    }
    else if(bombs.size() < 1) // Es kann noch ne Bombe gelegt werden
    {
      if(isTargetZone(new Point(gridX, gridY)))
      {
        placeBomb();
        currentPath = calculateHidePath(checkForBomb(null));
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
