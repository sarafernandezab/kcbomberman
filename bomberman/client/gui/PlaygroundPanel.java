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

package bomberman.client.gui;

import bomberman.client.Main;
import bomberman.server.Playground;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.rmi.RemoteException;

import javax.swing.JPanel;

/**
 * Panel that displays a game's playground. The client
 * receives changes from the server and displays this changes
 * on a PlaygroundPanel.
 * @author Kai Ritterbusch
 * @author Christian Lins
 */
public class PlaygroundPanel 
        extends JPanel
        implements KeyListener
{
  private ElementPainter[][] elementPainter;
  
  public PlaygroundPanel(int rows, int cols)
  {
    setBackground(Color.BLACK);
    
    GridBagConstraints gbc = new GridBagConstraints();
    GridBagLayout gbl = new GridBagLayout(); 
    setLayout(gbl);
    gbc.fill = GridBagConstraints.BOTH;    

    gbc.gridwidth   = 1;
    gbc.gridheight  = 1;
    //gbc.anchor = GridBagConstraints.NORTH;
    gbc.gridx = 0;
    gbc.gridy = 0;
    //gbc.weightx = 1;
    //gbc.weighty = 1;
    gbc.insets = new Insets(1,1,1,1);  // Abstaende
    
    this.elementPainter = new ElementPainter[rows][cols]; 
    gbl.setConstraints(this, gbc);
    
    for(int y = 0; y < rows; y++)
    {
      gbc.gridy = y;
      for(int x = 0; x < cols; x++)
      {
        gbc.gridx = x;
        this.elementPainter[y][x] = new ElementPainter(null);
        add(this.elementPainter[y][x], gbc);        
      } 
    }
    
    MainFrame.getInstance().setSize(
        (cols + 2) * ElementPainter.DEFAULT_SIZE,
        (rows + 2) * ElementPainter.DEFAULT_SIZE);
  }
  
  public void keyTyped(KeyEvent event) {}
  public void keyReleased(KeyEvent event) {}
  
  /**
   * Reacts on player actions.
   * @param event
   */
  public void keyPressed(KeyEvent event)
  {
    try
    {
      switch(event.getKeyCode())
      {
        case KeyEvent.VK_UP:
        {
          System.out.println("UP");
          Main.Server.move(Main.Session, 0, -1);
          break;
        }
        case KeyEvent.VK_DOWN:
        {
          System.out.println("DOWN");
          Main.Server.move(Main.Session, 0, +1);
          break;
        }
        case KeyEvent.VK_LEFT:
        {
          System.out.println("<=");
          Main.Server.move(Main.Session, -1, 0);
          break;
        }
        case KeyEvent.VK_RIGHT:
        {
          System.out.println("=>");
          Main.Server.move(Main.Session, +1, 0);
          break;
        }
        case KeyEvent.VK_SPACE:
        {
          Main.Server.placeBomb(Main.Session);
          break;
        }
      }
    }
    catch(RemoteException ex)
    {
      ex.printStackTrace();
    }
  }
  
  
  public void updatePlaygroundView(Playground playground) // TODO: 
  {
    for(int x=0; x< elementPainter.length; x++)    
      for(int y=0; y < elementPainter[x].length; y++)        
       elementPainter[x][y] = new ElementPainter(playground.getElement(x, y));
    
  }
}
