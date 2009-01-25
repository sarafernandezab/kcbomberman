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

import bomberman.net.Event;
import bomberman.net.EventDispatcherBase;
import bomberman.server.api.ServerInterface;
import java.io.OutputStream;

/**
 * This class is the glue between Server and Client. It takes calls from the
 * Client and makes Events from it. These Events are sent to the Server.
 * @author Christian Lins
 */
class ClientOutput extends EventDispatcherBase implements ServerInterface
{

  public ClientOutput(OutputStream out)
  {
    super(out);
  }
  
  public void createGame(Event event)
  {
    event.setMethodName("createGame");
    sendEvent(event);
  }

  public void joinGame(Event event)
  {
    event.setMethodName("joinGame");
    sendEvent(event);
  }

  public void joinViewGame(Event event)
  {
    event.setMethodName("joinViewGame");
    sendEvent(event);
  }

  public void leaveGame(Event event)
  {
    event.setMethodName("leaveGame");
    sendEvent(event);
  }

  public void login1(Event event) 
  {
    event.setMethodName("login1");
    sendEvent(event);
  }

  public void login2(Event event)
  {
    event.setMethodName("login2");
    sendEvent(event);
  }

  public void logout(Event event)
  {
    event.setMethodName("logout");
    sendEvent(event);
  }

  public void logoutAll(Event event)
  {
    event.setMethodName("logoutAll");
    sendEvent(event);
  }

  public void move(Event event)
  {
    event.setMethodName("move");
    sendEvent(event);
  }

  public void placeBomb(Event event)
  {
    event.setMethodName("placeBomb");
    sendEvent(event);
  }

  public void sendChatMessage(Event event)
  {
    event.setMethodName("sendChatMessage");
    sendEvent(event);
  }

  public void startGame(Event event)
  {
    event.setMethodName("startGame");
    sendEvent(event);
  }

}
