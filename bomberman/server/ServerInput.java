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

import bomberman.net.Event;
import bomberman.server.api.ServerInterface;
import bomberman.net.EventReceiverBase;
import java.io.InputStream;

/**
 * Handles the InputStream of a Client's socket. In most aspects this class
 * is an adapter to the bloat Server class.
 * @author Christian Lins
 */
class ServerInput extends EventReceiverBase implements ServerInterface 
{

  public ServerInput(InputStream in)
  {
    super(in);
  }
  
  public void createGame(Event event) 
  {
    
  }

  public void joinGame(Event event) 
  {

  }

  public void joinViewGame(Event event) 
  {

  }

  public void leaveGame(Event event) 
  {

  }

  public void login1(Event event)
  {
    System.out.println("ServerInput::login1(" + event.getArguments()[0] + ")");
  }

  public void login2(Event event) 
  {
    
  }

  public void logout(Event event) 
  {
  
  }

  public void logoutAll(Event event) 
  {

  }

  public void move(Event event) 
  {

  }

  public void placeBomb(Event event) 
  {

  }

  public void sendChatMessage(Event event) 
  {

  }

  public void startGame(Event event) 
  {

  }

}
