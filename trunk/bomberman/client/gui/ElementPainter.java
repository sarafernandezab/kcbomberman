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
import bomberman.server.api.Element;

import bomberman.server.api.Explodable;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import java.awt.Image;
import java.util.HashMap;

import javax.swing.JComponent;

/**
 * Paints an Element on a PlaygroundPanel.
 * @author Christian Lins (christian.lins@web.de)
 * @author Kai Ritterbusch (kai.ritterbusch@googlemail.com)
 */
public class ElementPainter extends JComponent
{
  public static final int    DEFAULT_SIZE    = 40;
  public static final String EXPLOSION_IMAGE = "resource/gfx/explosion/expl";
  
  private static HashMap<String, Image> ImageCache;
  
  static
  {
    ImageCache = new HashMap<String, Image>();
    for(int n = 1; n <= 5; n++)
    {
      Image img = Resource.getImage(EXPLOSION_IMAGE + n + ".png").getImage();
      ImageCache.put(EXPLOSION_IMAGE + n + ".png", img);
    }
  }
  
  private Image[] images    = new Image[5];
  private int     explStage = 0;
  private ExplosionTimer explTimer;
  private Element element;
  
  public ElementPainter()
  {
  }
  
  void newExplosion(int delay, int period)
  {
    this.explTimer = new ExplosionTimer(this, delay, period);
  }
  
  void nextExplosionImage()
  {
    explStage++;
    if(explStage > 5)
    {
      this.explTimer.cancel();
      explStage = 0;
    }
  }
  
  @Override
  public void paintComponent(Graphics g)
  {    
    g.setColor(Color.GREEN.darker().darker());
    g.fillRect(0, 0, getWidth(), getHeight());

    for(Image img : images)
    {
      if(img != null)
        g.drawImage(img, 0, 0, null);  
    }
    
    // Draw explosion if one has occurred
    if(explStage > 0 && (element == null || (element instanceof Explodable)))
    {
      Image img = ImageCache.get(EXPLOSION_IMAGE + (explStage + 1) + ".png");
      g.drawImage(img, 0, 0, null);
    }
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
  
  public void setElement(Element[] elements)
  {
    this.element = elements[0];
    
    for(int n = 0; n < elements.length; n++)
    {
      if(elements[n] == null)
      {
        this.images[n] = null;
      }
      else
      {
        String imageFilename = elements[n].getImageFilename();
        images[n] = ImageCache.get(imageFilename);
        if(images[n] == null)
        {
          images[n] = Resource.getImage(imageFilename).getImage();
          ImageCache.put(imageFilename, images[n]);
        }
      }
    }
  }
}
