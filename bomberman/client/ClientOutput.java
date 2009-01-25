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

import bomberman.server.Session;
import bomberman.server.api.Event;
import bomberman.server.api.ServerInterface;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.OutputStream;
import java.util.List;

/**
 * This class is the glue between Server and Client. It takes calls from the
 * Client and makes Events from it. These Events are sent to the Server.
 * @author Christian Lins (cli@openoffice.org)
 */
class ClientOutput implements ServerInterface
{

  private OutputStream  out     = null;
  private XStream       xstream = new XStream(new DomDriver());

  /**
   * 
   * @param out Must be a BufferedOutputStream with an underlying Socket.
   */
  public ClientOutput(OutputStream out)
  {
    this.out = out;
  }
  
  private void sendEvent(Event event)
  {
    this.xstream.toXML(event, out);
  }
  
  public boolean createGame(Session session, String gameName)
  {
    Event event = new Event("createGame", new Object[] {session, gameName});
    sendEvent(event);
    return true;
  }

  public void joinGame(Session session, String gameName)
  {
    Event event = new Event("joinGame", new Object[]{session, gameName});
    sendEvent(event);
  }

  public void joinViewGame(Session session, String gameName)
  {
    Event event = new Event("joinViewGame", new Object[]{session, gameName});
    sendEvent(event);
  }

  public void leaveGame(Session session)
  {
    Event event = new Event("leaveGame", new Object[]{session});
    sendEvent(event);
  }

  public long login1(String username) 
  {
    Event event = new Event("login1", new Object[]{username});
    sendEvent(event);
    return 0;
  }

  public boolean login2(String username, long hash)
  {
    Event event = new Event("login2", new Object[]{username, hash});
    sendEvent(event);
    return true;
  }

  public void logout(Session session)
  {
  }

  public void logoutAll()
  {

  }

  public boolean move(Session session, int x, int y)
  {
    return false;
  }

  public boolean placeBomb(Session session)
  {
    return false;
  }

  public void sendChatMessage(Session session, String message)
  {

  }

  public boolean startGame(Session session, String gameName)
  {
    return false;
  }

}
