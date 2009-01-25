/*
 *  KC Bomberman
 *  Copyright (C) 2008-2009 Christian Lins <cli@openoffice.org>
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

package bomberman.server;

import bomberman.server.api.ServerInterface;
import java.io.InputStream;

/**
 * Handles the InputStream of a Client's socket. In most aspects this class
 * is an adapter to the bloat Server class.
 * @author Christian Lins
 */
class ServerInput extends Thread implements ServerInterface 
{

  private InputStream in;
  
  public ServerInput(InputStream in)
  {
    this.in = in;
  }
  
  public boolean createGame(Session session, String gameName) 
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void joinGame(Session session, String gameName) 
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void joinViewGame(Session session, String gameName) 
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void leaveGame(Session session) 
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public long login1(String username) 
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public boolean login2(String username, long hash) 
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void logout(Session session) 
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void logoutAll() 
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public boolean move(Session session, int x, int y) 
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public boolean placeBomb(Session session) 
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void sendChatMessage(Session session, String message) 
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public boolean startGame(Session session, String gameName) 
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

}
