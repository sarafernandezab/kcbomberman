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
  public static final String EXPLOSION_IMAGE =  "resource/gfx/explosion/expl";
  public static final String PLAYER_DIE_IMAGE = "resource/gfx/player";
  
  private static HashMap<String, Image> ImageCache;
  
  static
  {
    ImageCache = new HashMap<String, Image>();
    // Explosion-Image
    for(int n = 1; n <= 5; n++)
    {
      Image img = Resource.getImage(EXPLOSION_IMAGE + n + ".png").getImage();
      ImageCache.put(EXPLOSION_IMAGE + n + ".png", img);
    }
    // Player-Die-Image
    for(int n = 1; n <= 4; n++)
    {
      for(int i = 21; i <= 25; i++)
      {
        Image img = Resource.getImage(PLAYER_DIE_IMAGE + n + "/"+ i +".png").getImage();
        ImageCache.put(PLAYER_DIE_IMAGE + n + "/"+ i +".png", img);
      }
    }
  }
  
  private Image[] images    = new Image[5];
  private int     explStage = 0;
  private int     dieStage = 0;
  private ExplosionTimer explTimer;
  private DieTimer dieTimer;
  private Element element;
  private int playerNumber;
  
  public ElementPainter()
  {
  }
  
  private void stopExplosionTimer()
  {
    this.explTimer.cancel();
    this.explTimer = null;
    explStage = 0;
  }
  
  synchronized void newExplosion(int delay, int period)
  {
    if(this.explTimer != null)
      stopExplosionTimer();

    this.explTimer = new ExplosionTimer(this, delay, period);
  }
  
  
  synchronized void newDieAnimation(int delay, int period, int playerNumber)
  {
    if(this.dieTimer != null)
      stopDieTimer();
    this.playerNumber = playerNumber;
    this.dieTimer = new DieTimer(this, delay, period);
  }
  
  private void stopDieTimer()
  {
    this.dieTimer.cancel();
    this.dieTimer = null;
    explStage = 0;
  }
  
  void nextDieImage()
  {
    dieStage++;
    if(dieStage > 5)
      stopDieTimer();
  }

  
  void nextExplosionImage()
  {
    explStage++;
    if(explStage > 5)
      stopExplosionTimer();
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
    if(explStage > 0 && (getElement() == null || (getElement() instanceof Explodable)))
    {
      Image img = ImageCache.get(EXPLOSION_IMAGE + (explStage + 1) + ".png");
      g.drawImage(img, 0, 0, null);      
    }
    // Draw die animation
    if(dieStage > 0)
    {
      Image img = ImageCache.get(PLAYER_DIE_IMAGE + this.playerNumber + "/" + (dieStage + 20) + ".png");
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

  public
  Element getElement()
  {
    return element;
  }
}
