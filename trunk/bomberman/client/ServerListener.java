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
import bomberman.client.gui.StartPanel;
import bomberman.client.gui.WaitingPanel;
import bomberman.client.io.Resource;
import bomberman.server.Playground;
import bomberman.server.Session;
import bomberman.server.api.GameInfo;

import java.awt.Container;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Callback class for the Server to Client connection.
 * @author Kai Ritterbusch (kai.ritterbusch@fh-osnabrueck.de)
 * @author Christian Lins (christian.lins@web.de)
 */
public class ServerListener 
        extends UnicastRemoteObject 
        implements ServerListenerInterface
{
  public ServerListener() throws RemoteException
  {
  }
  
  public void explosion(int x, int y, int distance)
          throws RemoteException
  {
    new AudioThread(Resource.getAsStream("resource/sfx/explosion.mp3")).start();

    PlaygroundPanel pp = (PlaygroundPanel)MainFrame.getInstance().getContentPane();
    pp.drawExplosion(x, y, distance);
  }
 
  /**
   * Notifies the Client about an update of the game list, e.g.
   * someone else has created a new game or an existing game was
   * started elsewhere.
   * @param gameInfo
   * @throws java.rmi.RemoteException
   */
  public void gameListUpdate(List<GameInfo> gameInfo)
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
        row.add(ginfo.getCreator());
        row.add(ginfo.getPlayerCount());
        row.add(ginfo.getStatus());
        
        data.add(row);
      }
      ((LobbyPanel)cnt).addGameInfo(data);
    }
  }
  
  public void gameStarted(boolean spectStatus) throws RemoteException
  {
    MainFrame.getInstance().setContentPane(new PlaygroundPanel(Playground.DEFAULT_WIDTH, Playground.DEFAULT_HEIGHT, spectStatus));
    MainFrame.getInstance().setVisible(true);
    MainFrame.getInstance().repaint();
    System.out.println("Game start");
  }
  
  /**
   * Notifies the Client that the game it was playing in was
   * stopped by the Server due to the given condition.
   * The condition can be one of the following values:
   * <ul>
   *  <li>0: Unknown</li>
   *  <li>1: Game was stopped by admin</li>
   *  <li>2: You won the game</li>
   * </ul>
   * @throws java.rmi.RemoteException
   */
  public void gameStopped(int condition) throws RemoteException
  {
    String msg = "Unknown gameStopped condition!";
    
    switch(condition)
    {
      case 0:
      {
        msg = "Spiel wurde aus unbekanntem Grund beendet!";
        break;
      }
      case 1:
      {
        msg = "Spiel wurde vom Admin beendet!";
        break;
      }
      case 2:
      {
        msg = "Hurra! Du hast gewonnen!";
        break;
      }
    }
    
    // Change Window 
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run() 
      {
        JOptionPane.showMessageDialog(null, "TODO: Endnachricht!");
        MainFrame.getInstance().setContentPane(MainFrame.getInstance().getLobbyPanel());
        MainFrame.getInstance().setVisible(true);        
      }
    });
    System.out.println("Game stopped: " + msg);
  }
  
  public void gameJoined(String gameName) throws RemoteException
  {
    System.out.println("Game joined");
     
    MainFrame.getInstance().setContentPane(new WaitingPanel(gameName));
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
    bomberman.client.ClientThread.Session = session;
    
    MainFrame.getInstance().setContentPane(MainFrame.getInstance().getLobbyPanel());
    MainFrame.getInstance().setVisible(true);
  }
  
  public void loggedOut() throws RemoteException
  {    
    MainFrame.getInstance().setContentPane(new StartPanel());
    MainFrame.getInstance().setVisible(true);
  }
  
  public void playgroundUpdate(Playground playground) throws RemoteException
  {
    ((PlaygroundPanel)MainFrame.getInstance().getContentPane()).updatePlaygroundView(playground);
  }
  
  public void userListUpdate(List<String> users) throws RemoteException
  {
    if(MainFrame.getInstance().getContentPane() instanceof LobbyPanel)
    {
      LobbyPanel lobby = (LobbyPanel)MainFrame.getInstance().getContentPane();
      lobby.setUserList(users);
    }
  }
  
  // TODO: playerDied für jeden Player aufrufen => Code nach youDied()
  public void playerDied(int x, int y, int playerNumber) throws RemoteException
  {
    new AudioThread(Resource.getAsStream("resource/sfx/scream.mp3")).start();

    PlaygroundPanel pp = (PlaygroundPanel)MainFrame.getInstance().getContentPane();
    pp.drawDieAnimation(x, y, playerNumber);

    // Change Window 
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run() 
      {
        JOptionPane.showMessageDialog( null, "Sie sind leider gestorben!" );
        MainFrame.getInstance().setContentPane(MainFrame.getInstance().getLobbyPanel());
        MainFrame.getInstance().setVisible(true);        
      }
    });
  }
}
