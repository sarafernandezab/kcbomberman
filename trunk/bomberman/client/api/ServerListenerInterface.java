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

package bomberman.client.api;

import bomberman.server.Playground;
import bomberman.server.Session;
import bomberman.server.api.GameInfo;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Callback interface for the clients.
 * @author Kai Ritterbusch
 * @author Christian Lins
 */
public interface ServerListenerInterface extends Remote 
{
  /**
   * On the playground the Client is currently playing,
   * an explosion has occurred.
   * This methods requests the Client gui to display this explosion.
   * @param x
   * @param y
   * @param distance
   * @throws java.rmi.RemoteException
   */
  public void explosion(int x, int y, int distance)
    throws RemoteException;
  
  /**
   * The server notifies the client player that it has died.
   * @throws java.rmi.RemoteException
   */
  public void youDied()
    throws RemoteException;
  
  public void receiveChatMessage(String message)
    throws RemoteException;
  
  public void loggedIn(Session session)
    throws RemoteException;
  
  public void loggedOut()
    throws RemoteException;
  
  /**
   * Transmittes an updated playground to the client.
   * The Playground is complete, so that no inconsistencies can occur.
   * @throws java.rmi.RemoteException
   */
  public void playgroundUpdate(Playground playground)
    throws RemoteException;
  
  public void gameListUpdate(ArrayList<GameInfo> gameList)
    throws RemoteException;
  
  /**
   * Notifies the client that is has joined the game.
   * @param gameName
   * @throws java.rmi.RemoteException
   */
  public void gameJoined(String gameName)
    throws RemoteException;
  
  public void gameStopped() 
    throws RemoteException;
  
  /**
   * Is called if the server starts the game this client has joined.
   * @throws java.rmi.RemoteException
   */
  public void gameStarted(boolean specStatus)
    throws RemoteException;
  
  public void userListUpdate(ArrayList<String> users)
    throws RemoteException;
}
