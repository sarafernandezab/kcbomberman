/*
 *  KC Bomberman
 *  Copyright (C) 2008,2009 Christian Lins <cli@openoffice.org>
 *  Copyright (C) 2008 Kai Ritterbusch <kai.ritterbusch@googlemail.com>
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
import java.awt.AWTEvent;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.Socket;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;

/**
 * ClientThread starts a Thread for each Client
 * @author Kai Ritterbusch (kai.ritterbusch@fh-osnabrueck.de)
 * @author Christian Lins (christian.lins@web.de)
 */
public class ClientThread extends Thread
{
  public static ServerInterface Server;
  public static ClientInput  ServerListener;
  public static Session         Session;
  
  private String hostname;
  
  public ClientThread(String hostname)
  {
    this.hostname = hostname;
  }
  
  @Override
  public void run()
  {
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
      
      
      
      boolean retry = true;
      
      do
      {
        try
        {
          // Connect to server
          Socket socket  = new Socket("localhost", 4242);
          Server         = new ClientOutput(socket.getOutputStream());
          ServerListener = new ClientInput(socket.getInputStream());
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
