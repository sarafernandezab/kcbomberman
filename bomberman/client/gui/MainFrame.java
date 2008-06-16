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

import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

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
          bomberman.client.ClientThread.Server.logout(bomberman.client.ClientThread.Session);
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