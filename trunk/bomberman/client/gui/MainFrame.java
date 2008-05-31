/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bomberman.client.gui;

import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.rmi.RemoteException;
import javax.swing.JFrame;

/**
 * The Main application window.
 * @author Christian Lins
 * @author Kai Ritterbusch
 */
public class MainFrame extends JFrame
{
  private static MainFrame instance = null;
  
  public static MainFrame getInstance()
  {
    return instance;
  }
  
  public MainFrame()
  {
    instance = this;
    setTitle("Bomberman - von Kai Ritterbusch und Christian Lins");
    setSize(640, 500);
    setContentPane(new StartPanel());
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    
    WindowListener listener = new WindowAdapter() 
    {
      @Override
      public void windowClosing(WindowEvent w) 
      {
        try
        {
          System.out.println("ausgeloggt");
          bomberman.client.Main.Server.logout(bomberman.client.Main.Session);
        }
        catch(Exception re)
        {
          System.err.println(re.getLocalizedMessage());
        }
      }
    };
    this.addWindowListener(listener);
  }
  
  @Override
  public void setContentPane(Container cnt)
  {
    super.setContentPane(cnt);
    repaint();
  }
}
