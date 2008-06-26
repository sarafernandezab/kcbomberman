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
import java.util.List;

/**
 * Callback interface for the clients.
 * @author Kai Ritterbusch (kai.ritterbusch@fh-osnabrueck.de)
 * @author Christian Lins (christian.lins@web.de)
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
   * The server notifies the client player that a player has died.
   * @throws java.rmi.RemoteException
   */
  public void playerDied(int x, int y, int playerNumber)
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
  
  public void gameListUpdate(List<GameInfo> gameList)
    throws RemoteException;
  
  /**
   * Notifies the client that is has joined the game.
   * @param gameName
   * @throws java.rmi.RemoteException
   */
  public void gameJoined(String gameName)
    throws RemoteException;
  
  /**
   * Notifies the Client that the game it was playing in was
   * stopped by the Server due to the given condition.
   * The condition can be one of the following values:
   * <ul>
   *  <li>0: Unknown</li>
   *  <li>1: Game was stopped by admin</li>
   *  <li>2: You won the game</li>
   * </ul>
   * @throws java.rmi.RemoteException
   */
  public void gameStopped(int condition) 
    throws RemoteException;
  
  /**
   * Is called if the server starts the game this client has joined.
   * @throws java.rmi.RemoteException
   */
  public void gameStarted(boolean specStatus)
    throws RemoteException;
 
  /**
   * Updates the userlist(JList) in the LobbyPanel
   * @param users
   * @throws java.rmi.RemoteException
   */
  public void userListUpdate(List<String> users)
    throws RemoteException;
  
  /**
   * This method is called when players leaves game
   * @throws java.rmi.RemoteException
   */   
  public void playerLeftGame() 
    throws RemoteException;
  
  /**
   * This Method is called when Player died and therefore lost the game
   * @throws java.rmi.RemoteException
   */
  public void youDied()
    throws RemoteException;
}
