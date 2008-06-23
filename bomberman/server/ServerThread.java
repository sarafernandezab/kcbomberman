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
  
  private Server server;
  
  public ServerThread(boolean daemon)
          throws RemoteException
  {
    super("ServerThread");
    setDaemon(daemon);
    
    this.server = new Server();
  }
  
  public Server getServer()
  {
    return this.server;
  }
  
  @Override
  public void run()
  {
    try
    {      
      // Create local registry
      if(Registry == null)
        Registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
      
      // Bind server to name
      Registry.rebind("KCBombermanServer", server);      
      
      // Set a shutdown hook that stores the changed highscore and database
      Runtime.getRuntime().addShutdownHook(
              new ShutdownThread(server.getDatabase(), server.getHighscore()));
      
      System.out.println("Bombermanserver bereit ...");

      synchronized(this)
      {
        notifyAll();
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
  
  public synchronized void stopThread()
  {
    try
    {
      Registry.unbind("KCBombermanServer");
      notify();
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }
}
