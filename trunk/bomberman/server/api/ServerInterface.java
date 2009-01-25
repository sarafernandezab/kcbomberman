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

package bomberman.server.api;

import bomberman.server.Session;

/**
 * Main interface for the game server. 
 * @author Christian Lins (cli@openoffice.org)
 * @author Kai Ritterbusch
 */
public interface ServerInterface
{  
  /**
   * Client wants to login with the given username. This method is
   * part one of the Challenge Handshake Authentification Protocol (CHAP)
   * and returns a challenge that is valid for a few seconds (default: 30s).
   * @param username
   * @param sli
   * @return A challenge > 0 if the username is valid, otherwise 0 is
   * returned.
   */
  long login1(String username);
  
  /**
   * Second part of the Challenge Handshake Authentification Protocol (CHAP).
   * If the login is successful the Server will transmit a Session object
   * through the given @see{ServerListenerInterface}.
   * @param username
   * @param hash
   * @return
   */
  boolean login2(String username, long hash);
  
  /**
   * The client notfies the server that it was logged out (mostly
   * implicit through closing the client's frame).
   * @param session
   */
  void logout(Session session);
  
  /**
   * Sends a chat message to the public channel.
   * @param session
   * @param message
   */
  void sendChatMessage(Session session, String message);
  
  /**
   * Creates a new game on the server.
   * @param session
   * @param gameName
   * @return
   */
  boolean createGame(Session session, String gameName);

  /**
   * A player wants to join the game.
   * @param session
   * @param gameName
   * @return
   */
  void joinGame(Session session, String gameName);
  
  /**
   * A spectator wants to join the game.
   * @param session
   * @param gameName
   * @return
   */
  void joinViewGame(Session session, String gameName);
  
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
   */
  boolean startGame(Session session, String gameName);
  
    /**
   * Is called when a Client has pressed its moving keys.
   * It is not possible to move in both directions at once,
   * so either x or y must be zero. If they are both != zero
   * the method will always return false.
   * @param session Session that identifies the Client.
   * @param x Direction on x-axis.
   * @param y Direction on y-axis
   * @return 
   */
  boolean move(Session session, int x, int y);
  
  /**
   * Is called when a client has pressed 'Space' which is
   * usually the key for placing a bomb.
   * @param session
   * @return
   */
  boolean placeBomb(Session session);
  
    
  /**
   * Is called when a client has pressed 'ESC' which is
   * usually the key for leaving a game
   * @param session
   * @return
   */
  void leaveGame(Session session);
  
  /**
   * Is called when server stops
   */
  void logoutAll();
          
}
