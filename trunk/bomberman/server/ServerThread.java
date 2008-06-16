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

package bomberman.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerThread extends Thread
{
  private static Registry Registry = null;
  
  private Server    server;
  
  public ServerThread(boolean daemon)
  {
    setDaemon(daemon);
  }
  
  @Override
  public void run()
  {
    try
    {
      this.server = new Server();
      
      // Erzeuge neue lokale Registry
      if(Registry == null)
        Registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
      
      // Binde Server an Namen
      Registry.rebind("KCBombermanServer", server);
      
      System.out.println("Bombermanserver bereit ...");
      
      synchronized(this)
      {
        wait();
      }
      
      System.out.println("Server gestoppt!");
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
      System.exit(1);
    }
  }
  
  public void stopThread()
  {
    try
    {
      Registry.unbind("KCBombermanServer");
      synchronized(this)
      {
        notify();
      }
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }
}
