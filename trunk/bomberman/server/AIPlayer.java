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
import java.util.List;

/**
 * An AI-controlled player. The AI uses a modified A* algorithm for
 * path finding.
 * @author Christian Lins (christian.lins@web.de)
 */
public class AIPlayer extends Player
{
  private Playground playground;
  
  public AIPlayer(Playground playground)
  {
    super("KI-Knecht");
    
    this.playground = playground;
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
	/*	
  // Funktion zum Berechnen eines Weges zu einem Bombenpunkt
  private List calculateTargetPath()
  {
    int x = super.gridX;
    int y = super.gridY;
			
    // The A* Algorithm			
    var openNodes:Array   = new Array(); // Noch nicht besuchte Nodes
    var closedNodes:Array = new Array(); // Schon abgearbeitete Punkte
			
    // Mit Startpunkt initialisieren
    openNodes.push(new Array(x, y, 0, 0)); // Startpunkt (x, y) ist die Player-Position
    while(openNodes.length > 0)
    {
      var node:Array = openNodes.pop();
      if(isTargetZone(node) || closedNodes.lenght > 15) // Ist der Nachbarpunkt sprengbar?
      {
        // Den Pfad zurückverfolgen
        var path:Array = new Array();
        path.push(node);
        while(closedNodes.length > 0)
          path.push(closedNodes.pop());
					
        //trace("Target path length: " + path.length);
        return path;
      }
      else
      {
        int r1 = Math.random() > 0.5 ? 1 : -1;
        int r2 = r1 == 1 ? -1 : 1;
					
        // Alle möglichen Nachbarn von node herausfinden
        var n1 = Playground.Instance.getElementAt(node[0] + r1, node[1]);
        var n2 = Playground.Instance.getElementAt(node[0] + r2, node[1]);
        var n3 = Playground.Instance.getElementAt(node[0], node[1] + r1);
        var n4 = Playground.Instance.getElementAt(node[0], node[1] + r2);
					
        var saveNode:Boolean = false;
					
        if(n1 == null && node[2] != node[0] + r1)
        {
          openNodes.push(new Array(node[0] + r1, node[1], node[0], node[1]));
          saveNode = true;
        }
        if(n2 == null && node[2] != node[0] + r2)
        {
          openNodes.push(new Array(node[0] + r2, node[1], node[0], node[1]));
          saveNode = true;
        }
        if(n3 == null && node[3] != node[1] + r1)
        {
          openNodes.push(new Array(node[0], node[1] + r1, node[0], node[1]));
          saveNode = true;
        }
        if(n4 == null && node[3] != node[1] + r2)
        {
          openNodes.push(new Array(node[0], node[1] + r2, node[0], node[1]));
          saveNode = true;
        }

        if(saveNode)
          closedNodes.push(node);
      }
    }		
    return null;
  }
		
  // Funktion zum Berechnen eines Fluchtweges
  private List calculateHidePath(bomb:Sprite)
  {			
    int x = bomb.getMatrixX();
    int y = bomb.getMatrixY();
			
    // Der A* Algorithmus			
    var openNodes:Array = new Array();   // Noch nicht besuchte Nodes
    var closedNodes:Array = new Array(); // Schon  abgearbeitete Punkte
    
    // Mit Startpunkt initialisieren
    openNodes.push(new Array(x, y, 0, 0)); // Startpunkt (x, y) ist die Bombe
    while(openNodes.length > 0)
    {
      var node:Array = openNodes.pop();
      if(node[0] != x && node[1] != y) // Ist der Punkt sicher?
      {
        // Den Pfad zurückverfolgen
        var path:Array = new Array();
        path.push(node);
        while(closedNodes.length > 0)
          path.push(closedNodes.pop());

        //trace("Hide path length: " + path.length);
        return path;
      }
      else
      {
        // Alle möglichen Nachbarn von node herausfinden
        var n1 = Playground.Instance.getElementAt(node[0]+1, node[1]);
        var n2 = Playground.Instance.getElementAt(node[0]-1, node[1]);
        var n3 = Playground.Instance.getElementAt(node[0], node[1]+1);
        var n4 = Playground.Instance.getElementAt(node[0], node[1]-1);

        boolean saveNode = false;
					
        if(n1 == null && node[2] != node[0]+1)
        {
          openNodes.push(new Array(node[0]+1, node[1], node[0], node[1]));
          saveNode = true;
        }
        if(n2 == null && node[2] != node[0]-1)
        {
          openNodes.push(new Array(node[0]-1, node[1], node[0], node[1]));
						saveNode = true;
					}
					if(n3 == null && node[3] != node[1]+1)
					{
						openNodes.push(new Array(node[0], node[1]+1, node[0], node[1]));
						saveNode = true;
					}
					if(n4 == null && node[3] != node[1]-1)
					{
						openNodes.push(new Array(node[0], node[1]-1, node[0], node[1]));
						saveNode = true;
					}
					
					if(saveNode)
						closedNodes.push(node);
				}
			}
			
			return null;
		}
		
		// Prüft ob eine Bombe in meiner Nähe liegt.
		// Gibt false zurück, wenn keine Bombe in der Nähe ist.
		private function checkForBomb(bomb:Array):Sprite
		{
			var matrixX:int = this.matrixX;
			var matrixY:int = this.matrixY;
			
			if(bomb != null)
			{
				matrixX = bomb[0];
				matrixY = bomb[1];
			}
			
		   // Spieler steht auf der Bombe
		   if(Playground.Instance.getBombElementAt(matrixX, matrixY) is Bomb)
		      return Playground.Instance.getBombElementAt(matrixX, matrixY);
			  
		   // Spieler steht in der Naehe der Bombe, i < bombdistance
		   for(var i:int = 0; i < 4; i++)
		   {
			   if(Playground.Instance.getBombElementAt(matrixX+i, matrixY) is Bomb)
				  return Playground.Instance.getBombElementAt(matrixX+i, matrixY);
			   else if(Playground.Instance.getBombElementAt(matrixX-i, matrixY) is Bomb)		   
				  return Playground.Instance.getBombElementAt(matrixX-i, matrixY);
			   else if(Playground.Instance.getBombElementAt(matrixX, matrixY+i) is Bomb)
				  return Playground.Instance.getBombElementAt(matrixX, matrixY+i);
			   else if(Playground.Instance.getBombElementAt(matrixX, matrixY-i) is Bomb)
				  return Playground.Instance.getBombElementAt(matrixX, matrixY-i);
			   else if(Playground.Instance.getBombElementAt(matrixX, matrixY)   is Bomb)
				  return Playground.Instance.getBombElementAt(matrixX, matrixY); 
		   }
		   return null;
		}
		
		override public function explode():void
	  	{
			super.explode();
			timer.removeEventListener(TimerEvent.TIMER, tick);
			timer.stop();
	  	}
		
		private function randomDirection():int
		{
			return Math.round(Math.random() * 2) - 1;
		}
		
		public function tick(timerEvent:TimerEvent):void
		{			
		    // In dieser Methode werden die Aktionen des KI-Spielers ausgeführt.
			// Dabei ist zwischen verschiedenen Aktionen zu unterscheiden:
			// - Stelle für Bombe suchen
			// - Bombe plazieren
			// - Deckung suchen und auf Bombe warten

			if(currentPath.length > 0) // Solange noch ein Pfad existiert, laufen wir
			{
				var node:Array = currentPath.pop();
				//trace((node[0] - matrixX) + " " + (node[1] - matrixY));
				//trace(node[0] + " " + node[1] + " " + matrixX + " " + matrixY);
				if(!move((node[0] - matrixX) * 40, (node[1] - matrixY) * 40, true))
					currentPath = new Array(); // Lösche Pfad
			}
			else if(bombs.length < 1) // Es kann noch ne Bombe gelegt werden
			{
				if(isTargetZone(new Array(matrixX, matrixY)))
				{
					placeBomb();
					currentPath = calculateHidePath(checkForBomb(null));
					if(currentPath == null)
					{
						currentPath = new Array();
						trace("Ohoh...");
						placeBomb();
					}
				}
				else
				{
					currentPath = calculateTargetPath();
					if(currentPath == null)
						currentPath = new Array();
				}
			}
			else
			{
				var bomb:Sprite = checkForBomb(null);
				if(bomb == null)
					return;
				currentPath = calculateHidePath(bomb);
				if(currentPath == null)
				{
					currentPath = new Array();
				}
			}
		}*/
}
