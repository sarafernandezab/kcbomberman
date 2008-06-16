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

package bomberman.client;

import bomberman.client.gui.MainFrame;
import bomberman.client.gui.PlaygroundPanel;
import bomberman.server.Session;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import bomberman.server.api.ServerInterface;
import java.awt.AWTEvent;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.rmi.ConnectException;
import javax.swing.JOptionPane;

/**/
public class ClientThread extends Thread
{
  public static ServerInterface Server;
  public static ServerListener  ServerListener;
  public static Session         Session;
  
  private String[] args;
  
  // Konstruktor mit uebergabe von Argumenten
  public ClientThread(String[] args)
  {
    this.args = args;
  }
  
  @Override
  public void run()
  {
    Registry registry;
    try
    {
      Toolkit.getDefaultToolkit().getSystemEventQueue().push(
        new EventQueue()
        {
          @Override
          protected void dispatchEvent(AWTEvent event) 
          {
            if (event instanceof KeyEvent) 
            {
              keyEvent((KeyEvent)event);     
            }
            super.dispatchEvent(event); 
          }
        });
      
      // Create main frame
      new MainFrame().setVisible(true);
      
      ServerListener = new ServerListener();
      
      if(this.args.length <= 1)
        registry = LocateRegistry.getRegistry(Registry.REGISTRY_PORT);
      else
        registry = LocateRegistry.getRegistry(args[1], Registry.REGISTRY_PORT);
      
      Server = (ServerInterface)registry.lookup("Server");
    }
    catch (ConnectException ex)
    {
      JOptionPane.showMessageDialog(MainFrame.getInstance(), "Server läuft nicht!");
    }
    catch (Exception ex) 
    {
      ex.printStackTrace();
    }
  }
  
  /**
   * Processes ALL KeyEvents the MainFrame receives.
   * @param event
   */
  private void keyEvent(KeyEvent event)
  {
    if(event.getID() == KeyEvent.KEY_PRESSED)
    {
      Container cnt = MainFrame.getInstance().getContentPane();
      if(cnt instanceof KeyListener)
      {
        ((KeyListener)cnt).keyPressed(event);
      }
    }
  }
}
