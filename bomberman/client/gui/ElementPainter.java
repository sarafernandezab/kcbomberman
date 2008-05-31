/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bomberman.client.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;

/**
 * Paints an Element on a PlaygroundPanel.
 * @author Christian Lins
 */
public class ElementPainter extends JComponent
{
  public static final int DEFAULT_SIZE = 40;
  
  @Override
  public void paintComponent(Graphics g)
  {
    g.setColor(Color.GREEN.darker().darker());
    g.fillRect(0, 0, getWidth(), getHeight());
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
}
