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

package bomberman.server;

import bomberman.client.api.ServerListenerInterface;
import bomberman.server.api.Event;
import bomberman.server.api.GameInfo;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.OutputStream;
import java.util.List;

/**
 *
 * @author Christian Lins (cli@openoffice.org)
 */
class ServerOutput implements ServerListenerInterface
{

  private OutputStream  out     = null;
  private XStream       xstream = new XStream(new DomDriver());
  
  public ServerOutput(OutputStream out)
  {
    this.out = out;
  }
  
  private void sendEvent(Event event)
  {
    this.xstream.toXML(event, out);
  }
  
  public void explosion(int x, int y, int distance) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void gameJoined(String gameName) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void gameListUpdate(List<GameInfo> gameList) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void gameStarted(boolean specStatus) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void gameStopped(int condition) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void loggedIn(Session session) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void loggedOut() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void playerDied(int x, int y, int playerNumber) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void playerLeftGame() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void playgroundUpdate(Playground playground) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void receiveChatMessage(String message) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void userListUpdate(List<String> users) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void youDied() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

}
