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

package bomberman.client.api;

import bomberman.server.Playground;
import bomberman.server.Session;
import bomberman.net.Event;
import bomberman.server.api.GameInfo;
import java.util.List;

/**
 * Callback interface for the clients.
 * @author Kai Ritterbusch
 * @author Christian Lins
 */
public interface ServerListenerInterface
{

  /**
   * On the playground the Client is currently playing,
   * an explosion has occurred.
   * This methods requests the Client gui to display this explosion.
   * @param x
   * @param y
   * @param distance
   */
  public void explosion(int x, int y, int distance);
  
  /**
   * The server notifies the client player that a player has died.
   * @throws java.rmi.RemoteException
   */
  public void playerDied(int x, int y, int playerNumber);
  
  /**
   * The Client receives a chat message.
   * @param message
   */
  public void receiveChatMessage(String message);
  
  void continueLogin(Event event);
  
  /**
   * The Client was successfully logged in.
   * @param session
   */
  public void loggedIn(Session session);
  
  /**
   * The Client was logged out.
   */
  public void loggedOut();
  
  /**
   * Transmittes an updated playground to the client.
   * The Playground is complete, so that no inconsistencies can occur.
   */
  public void playgroundUpdate(Playground playground);
  
  /**
   * The server sends an update of the game list.
   * @param gameList
   */
  public void gameListUpdate(List<GameInfo> gameList);
  
  /**
   * Notifies the client that is has joined the game.
   * @param gameName
   */
  public void gameJoined(String gameName);
  
  /**
   * Notifies the Client that the game it was playing in was
   * stopped by the Server due to the given condition.
   * The condition can be one of the following values:
   * <ul>
   *  <li>0: Unknown</li>
   *  <li>1: Game was stopped by admin</li>
   *  <li>2: You won the game</li>
   * </ul>
   */
  public void gameStopped(int condition);
  
  /**
   * Is called if the server starts the game this client has joined.
   */
  public void gameStarted(boolean specStatus);
 
  /**
   * Updates the user list in the LobbyPanel.
   * @param users
   * @throws java.rmi.RemoteException
   */
  public void userListUpdate(List<String> users);
  
  /**
   * This method is called when players leaves game.
   * @throws java.rmi.RemoteException
   */   
  public void playerLeftGame();
  
  /**
   * This Method is called when Player died and therefore lost the game.
   * @throws java.rmi.RemoteException
   */
  public void youDied();
}
