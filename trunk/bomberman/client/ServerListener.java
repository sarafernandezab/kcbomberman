/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
  
  public void gameStarted()
          throws RemoteException
  {
    System.out.println("Game start");
  }
  
  public void gameJoined(String name) throws RemoteException
  {
    System.out.println("Game joined");
     
    MainFrame.getInstance().setContentPane(new WaitingPanel());
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
  }
  
  public void loggedIn(Session session) throws RemoteException
  {
    bomberman.client.Main.Session = session;
    
    MainFrame.getInstance().setContentPane(new LobbyPanel());
    MainFrame.getInstance().setVisible(true);
  }
  
  public void playgroundUpdate(Playground playground) throws RemoteException
  {
    
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
