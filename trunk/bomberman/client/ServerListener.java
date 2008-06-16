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

package bomberman.client;

import bomberman.client.api.ServerListenerInterface;
import bomberman.client.gui.LobbyPanel;
import bomberman.client.gui.MainFrame;
import bomberman.client.gui.PlaygroundPanel;
import bomberman.client.gui.WaitingPanel;
import bomberman.server.Playground;
import bomberman.server.Session;

import bomberman.server.api.GameInfo;
import java.awt.Container;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Callback class for the Server to Client connection.
 * @author Kai Ritterbusch
 */
public class ServerListener 
        extends UnicastRemoteObject 
        implements ServerListenerInterface
{
  public ServerListener() throws RemoteException
  {
    
  }
  
  public void gameListUpdate(ArrayList<GameInfo> gameInfo)
          throws RemoteException
  {
    Container cnt = MainFrame.getInstance().getContentPane();
    if(cnt instanceof LobbyPanel)
    {
      ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
      
      for(GameInfo ginfo : gameInfo)
      {
        ArrayList<Object> row = new ArrayList<Object>();
        row.add(ginfo.getName());
        row.add("");
        row.add("");
        row.add("");
        
        data.add(row);
      }
      ((LobbyPanel)cnt).addGameInfo(data);
    }
  }
  
  public void gameStarted() throws RemoteException
  {
    MainFrame.getInstance().setContentPane(new PlaygroundPanel(14, 16));
    System.out.println("Game start");
  }
  
  public void gameStopped() throws RemoteException
  {
    MainFrame.getInstance().setContentPane(new LobbyPanel());
    System.out.println("Game stopped");
  }
  
  public void gameJoined(String gameName) throws RemoteException
  {
    System.out.println("Game joined");
     
    MainFrame.getInstance().setContentPane(new WaitingPanel(gameName));
    //MainFrame.getInstance().setContentPane(new PlaygroundPanel(14, 16)); // TODO:
    MainFrame.getInstance().setVisible(true);
  }
  
  public void receiveChatMessage(String message) throws RemoteException
  {
    System.out.println(message);
    if(MainFrame.getInstance().getContentPane() instanceof LobbyPanel)
    {
      LobbyPanel lobby = (LobbyPanel)MainFrame.getInstance().getContentPane();
      lobby.addChatMessage(message);
    }
    else if(MainFrame.getInstance().getContentPane() instanceof WaitingPanel)
    {
      WaitingPanel waitingPanel = (WaitingPanel)MainFrame.getInstance().getContentPane();
      waitingPanel.addInfoText(message);
    }
  }
  
  public void loggedIn(Session session) throws RemoteException
  {
    bomberman.client.Main.Session = session;
    
    MainFrame.getInstance().setContentPane(new LobbyPanel());
    MainFrame.getInstance().setVisible(true);
  }
  
  public void playgroundUpdate(Playground playground) throws RemoteException
  {
    ((PlaygroundPanel)MainFrame.getInstance().getContentPane()).updatePlaygroundView(playground);
  }
  
  public void userListUpdate(ArrayList<String> users) throws RemoteException
  {
    if(MainFrame.getInstance().getContentPane() instanceof LobbyPanel)
    {
      LobbyPanel lobby = (LobbyPanel)MainFrame.getInstance().getContentPane();
      lobby.setUserList(users);
    }
  }
}
