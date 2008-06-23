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

package bomberman.server.api;

import bomberman.client.api.ServerListenerInterface;
import bomberman.server.Session;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Main interface for the game server. 
 * @author Christian Lins (christian.lins@web.de)
 * @author Kai Ritterbusch
 */
public interface ServerInterface extends Remote
{
  /**
   * Client wants to login with the given username.
   * The server may throw an RemoteException if the username is 
   * already used.
   * @param nickname
   * @param sli
   * @throws java.rmi.RemoteException
   */
  void login(String nickname, String password, ServerListenerInterface sli)
    throws RemoteException;
  
  /**
   * The client notfies the server that it was logged out (mostly
   * implicit through closing the client's frame).
   * @param session
   * @throws java.rmi.RemoteException
   */
  void logout(Session session)
    throws RemoteException;
  
  /**
   * Sends a chat message to the public channel.
   * @param session
   * @param message
   * @throws java.rmi.RemoteException
   * @throws bomberman.server.api.InvalidSessionException
   */
  void sendChatMessage(Session session, String message)
    throws RemoteException, InvalidSessionException;
  
  /**
   * Creates a new game on the server.
   * @param session
   * @param gameName
   * @return
   * @throws java.rmi.RemoteException
   * @throws bomberman.server.api.InvalidSessionException
   */
  boolean createGame(Session session, String gameName)
    throws RemoteException, InvalidSessionException;

  /**
   * A player wants to join the game.
   * @param session
   * @param gameName
   * @return
   * @throws java.rmi.RemoteException
   * @throws bomberman.server.api.InvalidSessionException
   */
  boolean joinGame(Session session, String gameName)
    throws RemoteException, InvalidSessionException;
  
  /**
   * A spectator wants to join the game.
   * @param session
   * @param gameName
   * @return
   * @throws java.rmi.RemoteException
   * @throws bomberman.server.api.InvalidSessionException
   */
  boolean joinViewGame(Session session, String gameName)
    throws RemoteException, InvalidSessionException;
  
  /**
   * The creator of a game wants to start the game through his 
   * WaitingPanel. The Server checks the session of validity and
   * if the caller is indeed the creator of the game.
   * @param session
   * @param gameName
   * @return true if the game was startet, false if the caller is not
   * the creator of the game. The return value is only for debugging reasons,
   * because every client that waits for the game is notfied through the
   * @see{ServerListenerInterface} that the game was started.
   * @throws java.rmi.RemoteException
   * @throws bomberman.server.api.InvalidSessionException
   */
  boolean startGame(Session session, String gameName)
    throws RemoteException, InvalidSessionException;
  
    /**
   * Is called when a Client has pressed its moving keys.
   * It is not possible to move in both directions at once,
   * so either x or y must be zero. If they are both != zero
   * the method will always return false.
   * @param session Session that identifies the Client.
   * @param x Direction on x-axis.
   * @param y Direction on y-axis
   * @return 
   * @throws java.rmi.RemoteException
   */
  boolean move(Session session, int x, int y) 
    throws RemoteException, InvalidSessionException;
  
  /**
   * Is called when a client has pressed 'Space' which is
   * usually the key for placing a bomb.
   * @param session
   * @return
   * @throws java.rmi.RemoteException
   */
  boolean placeBomb(Session session)
    throws RemoteException, InvalidSessionException;

  
}
