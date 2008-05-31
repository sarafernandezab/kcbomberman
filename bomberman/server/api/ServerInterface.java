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

import bomberman.server.Game;
import bomberman.client.api.ServerListenerInterface;
import java.rmi.Remote;
import java.rmi.RemoteException;

import bomberman.server.Session;

/**
 * Main interface for the game server. 
 * @author Christian Lins
 * @author Kai Ritterbusch
 */
public interface ServerInterface extends Remote
{
  void login(String nickname, ServerListenerInterface sli)
    throws RemoteException;
  
  void logout(Session session)
    throws RemoteException;
  
  void sendChatMessage(Session session, String message)
    throws RemoteException, InvalidSessionException;
  
  boolean createGame(Session session, String gameName)
    throws RemoteException, InvalidSessionException;

  boolean joinGame(Session session, String gameName)
    throws RemoteException, InvalidSessionException;
  
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
