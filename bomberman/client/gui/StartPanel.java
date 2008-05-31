/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bomberman.client.gui;

import bomberman.client.io.Resource;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JPanel;

/**
 * Shows the splash screen and the login window.
 * @author Christian Lins
 */
public class StartPanel extends JPanel
{
  private Image       background = null;
  private LoginPanel  loginPanel = new LoginPanel();
  
  public StartPanel()
  {
    this.background = Resource.getImage("resource/gfx/splash.jpg").getImage();
    
    setLayout(null);
    add(loginPanel);
  
    addComponentListener(new ComponentAdapter() 
    {
      @Override
      public void componentResized(ComponentEvent event)
      {
        int x = getWidth() / 2 - 200;
        int y = getHeight() - 100;
        loginPanel.setBounds(x, y, 400, 75);
      }
    });
  }
  
  @Override
  public void paintComponent(Graphics g)
  {
    g.drawImage(background, 0, 0, null);
  }
}
