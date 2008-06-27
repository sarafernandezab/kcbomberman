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
import bomberman.server.Session;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import bomberman.server.api.ServerInterface;
import bomberman.server.rmi.RMIClientSocketFactoryImpl;
import java.awt.AWTEvent;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.rmi.ConnectException;

/**
 * ClientThread starts a Thread for each Client
 * @author Kai Ritterbusch (kai.ritterbusch@fh-osnabrueck.de)
 * @author Christian Lins (christian.lins@web.de)
 */
public class ClientThread extends Thread
{
  public static ServerInterface Server;
  public static ServerListener  ServerListener;
  public static Session         Session;
  
  private String hostname;
  
  public ClientThread(String hostname)
  {
    this.hostname = hostname;
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
      
      boolean retry = true;
      
      do
      {
        try
        {
          if(this.hostname == null)
            registry = LocateRegistry.getRegistry(Registry.REGISTRY_PORT);
          else
          {
            RMIClientSocketFactoryImpl.ServerHost = this.hostname;
            System.out.println("Search registry on host " + hostname + "...");
            registry = LocateRegistry.getRegistry(hostname, Registry.REGISTRY_PORT);
          }

          Server = (ServerInterface)registry.lookup("KCBombermanServer");
          retry = false;
        }
        catch(ConnectException ex)
        {
          System.out.println("Server läuft nicht. Warte...");
          Thread.sleep(1000);
        }
      }
      while(retry);
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
