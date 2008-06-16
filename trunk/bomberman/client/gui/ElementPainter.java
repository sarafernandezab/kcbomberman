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

import bomberman.client.io.Resource;
import bomberman.server.ExplodableWall;
import bomberman.server.Player;
import bomberman.server.SolidWall;
import bomberman.server.Wall;
import bomberman.server.api.Element;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

/**
 * Paints an Element on a PlaygroundPanel.
 * @author Christian Lins
 */
public class ElementPainter extends JComponent
{
  public static final int DEFAULT_SIZE = 40;
  private ImageIcon image = null;
  
  public ElementPainter()
  {     
    
  }
  
  @Override
  public void paintComponent(Graphics g)
  {    
    if(image == null) // No Image, background
    {
      g.setColor(Color.GREEN.darker().darker());
      g.fillRect(0, 0, getWidth(), getHeight());
    }
    else // Image loaded
      g.drawImage(this.image.getImage(),0,0,null);    
  }
  
  @Override
  public Dimension getPreferredSize()
  {
    return new Dimension(40, 40);
  }
  
  @Override
  public Dimension getMinimumSize()
  {
    return getPreferredSize();
  }
  
  public void setElement(Element element)
  {
    if(element instanceof SolidWall)
    {
      image = Resource.getImage("resource/gfx/solid_wall.png");
    }
    else if(element instanceof ExplodableWall)
    {
      image = Resource.getImage("resource/gfx/explodable_wall.png");
    } 
    else if(element instanceof Player)
    {
      if( ((Player)element).getID() == 1)                         
        image = Resource.getImage("resource/gfx/player1/6.png");
      else if( ((Player)element).getID() == 2)
        image = Resource.getImage("resource/gfx/player2/6.png");
      else if( ((Player)element).getID() == 3)
        image = Resource.getImage("resource/gfx/player3/6.png");
      else if( ((Player)element).getID() == 4)
        image = Resource.getImage("resource/gfx/player4/6.png");
    }
    else
      image = null;
  }
}
