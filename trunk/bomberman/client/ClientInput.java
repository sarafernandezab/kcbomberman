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

import bomberman.client.api.ServerListenerInterface;
import bomberman.client.gui.LobbyPanel;
import bomberman.client.gui.MainFrame;
import bomberman.client.gui.PlaygroundPanel;
import bomberman.client.gui.WaitingPanel;
import bomberman.client.io.Resource;
import bomberman.net.Event;
import bomberman.net.EventReceiverBase;
import bomberman.server.Playground;
import bomberman.server.Session;
import bomberman.server.api.GameInfo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Callback class for the Server to Client connection.
 * @author Kai Ritterbusch
 * @author Christian Lins
 */
public class ClientInput extends EventReceiverBase implements ServerListenerInterface
{
  private String      gameStoppedMessage;
  
  public ClientInput(InputStream in)
  {
    super(in);
  }
  
  public void continueLogin(Event event)
  {
    
  }
  
  /**
   * An explosion has occurred that the Client must show.
   * @param x
   * @param y
   * @param distance
   * @throws java.rmi.RemoteException
   */
  public void explosion(int x, int y, int distance)
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
  {
    LobbyPanel lp = MainFrame.getInstance().getLobbyPanel();

    ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();

    for (GameInfo ginfo : gameInfo)
    {
      ArrayList<Object> row = new ArrayList<Object>();
      row.add(ginfo.getName());
      row.add(ginfo.getCreator());
      row.add(ginfo.getPlayerCount());
      row.add(ginfo.getStatus());

      data.add(row);
    }
    lp.setGameInfo(data);
  }
  
  /**
   * Notifies the Client that a game he was waiting for has started.
   * @param spectStatus
   */
  public void gameStarted(boolean spectStatus)
  {
    // Create a new playground
    MainFrame.getInstance()
      .setContentPane(new PlaygroundPanel(Playground.DEFAULT_WIDTH, Playground.DEFAULT_HEIGHT, spectStatus));
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
  public void gameStopped(int condition)
  {
    this.gameStoppedMessage = null;
    
    switch(condition)
    {
      case 1:
      {
        this.gameStoppedMessage = "Spiel wurde vom Admin beendet!";
        break;
      }
      case 2:
      {
        this.gameStoppedMessage = "Hurra! Du hast gewonnen!";
        break;
      }
      default:
        this.gameStoppedMessage = null;
    }
    
    // Change Window 
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run() 
      {
        if(gameStoppedMessage != null)
          JOptionPane.showMessageDialog(MainFrame.getInstance(), gameStoppedMessage);
        MainFrame.getInstance().setContentPane(MainFrame.getInstance().getLobbyPanel());
        MainFrame.getInstance().resetSize(); 
      }
    });
  }
  
  /**
   * is called when client joined a game
   * @param gameName
   * @throws java.rmi.RemoteException
   */
  public void gameJoined(String gameName)
  {
    System.out.println("Game joined");
     
    MainFrame.getInstance().setContentPane(new WaitingPanel(gameName));
    MainFrame.getInstance().setVisible(true);
  }
  
  /**
   * Notifies the Client that he can receive a chat message
   * Shows the message in LobbyPanel
   * @param message
   * @throws java.rmi.RemoteException
   */
  public void receiveChatMessage(String message)
  {    
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
  
  /**
   * Set the session
   * Switch the user gui to the LobbyPanel
   * @param session
   */
  public void loggedIn(Session session)
  {
    bomberman.client.ClientThread.Session = session;
    
    MainFrame.getInstance().setContentPane(MainFrame.getInstance().getLobbyPanel());
    MainFrame.getInstance().setVisible(true);
  }
  
  /**
   * Notifies the Client that he was logged out. Shows the StartPanel
   */   
  public void loggedOut()
  {    
    MainFrame.getInstance().setVisible(false);
    
    bomberman.client.ClientThread.Session = null;
    bomberman.client.ClientThread.Server  = null;
    
    //new ClientThread(RMIClientSocketFactoryImpl.ServerHost).start();
  }
  
  /**
   * Clientside update of the playground
   * @param playground
   */
  public void playgroundUpdate(Playground playground)
  {
    ((PlaygroundPanel)MainFrame.getInstance().getContentPane()).updatePlaygroundView(playground);
  }
  
  /**
   * Updates the user list in the LobbyPanel.
   * @param users
   * @throws java.rmi.RemoteException
   */
  public void userListUpdate(List<String> users)
  {
    LobbyPanel lobby = MainFrame.getInstance().getLobbyPanel();
    lobby.setUserList(users);
  }
  
  /**
   * This method is called for every player that dies on the playground
   * this Client is playing on.
   * @param x
   * @param y
   * @param playerNumber
   */
  public void playerDied(int x, int y, int playerNumber)
  {
    new AudioThread(Resource.getAsStream("resource/sfx/scream.mp3")).start();

    PlaygroundPanel pp = (PlaygroundPanel)MainFrame.getInstance().getContentPane();
    pp.drawDieAnimation(x, y, playerNumber);
  }
  
   /**
   * This method is called when players leaves game.
   * @throws java.rmi.RemoteException
   */
  public void playerLeftGame()
  {
    MainFrame.getInstance().setContentPane(MainFrame.getInstance().getLobbyPanel());
    MainFrame.getInstance().resetSize();
  }
  
  /**
   * This Method is called when Player died and therefore lost the game
   */ 
  public void youDied()
  {
    SwingUtilities.invokeLater(new Runnable() 
    {
      public void run()
      {
        JOptionPane.showMessageDialog(MainFrame.getInstance(), "Leider verloren!", "Game over", JOptionPane.INFORMATION_MESSAGE);
        MainFrame.getInstance().setContentPane(MainFrame.getInstance().getLobbyPanel());
        MainFrame.getInstance().resetSize();
      }
    });
  }
}
