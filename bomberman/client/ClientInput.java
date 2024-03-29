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
import bomberman.client.gui.LoginPanel;
import bomberman.client.gui.MainFrame;
import bomberman.client.gui.PlaygroundPanel;
import bomberman.client.gui.WaitingPanel;
import bomberman.client.io.Resource;
import bomberman.net.Event;
import bomberman.net.EventReceiverBase;
import bomberman.server.Playground;
import bomberman.server.api.Session;
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
  
  /**
   * Receives a challenge from the Server.
   * @param event
   */
  public void continueLogin(Event event)
  {
    System.out.println("ClientInput.continueLogin()");
    long challenge = (Long)event.getArguments()[0];
    LoginPanel.getInstance().continueLogin(challenge);
  }
  
  /**
   * An explosion has occurred that the Client must show.
   */
  public void explosion(Event event)
  {
    int x        = (Integer)event.getArguments()[0];
    int y        = (Integer)event.getArguments()[1];
    int distance = (Integer)event.getArguments()[2];

    AudioThread.playSound(Resource.getAsURL("resource/sfx/explosion.wav"));
    
    PlaygroundPanel pp = (PlaygroundPanel)MainFrame.getInstance().getContentPane();
    pp.drawExplosion(x, y, distance);
  }
 
  /**
   * Notifies the Client about an update of the game list, e.g.
   * someone else has created a new game or an existing game was
   * started elsewhere.
   */
  public void gameListUpdate(Event event)
  {
    List<GameInfo> gameInfo = (List<GameInfo>)event.getArguments()[0];
    LobbyPanel lp = MainFrame.getInstance().getLobbyPanel();

    List<List<Object>> data = new ArrayList<List<Object>>();

    for (GameInfo ginfo : gameInfo)
    {
      List<Object> row = new ArrayList<Object>();
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
   */
  public void gameStarted(Event event)
  {
    boolean spectStatus = (Boolean)event.getArguments()[0];
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
   */
  public void gameStopped(Event event)
  {
    int condition = (Integer)event.getArguments()[0];
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
   * Is called when client joined a game.
   */
  public void gameJoined(Event event)
  {
    String gameName = (String)event.getArguments()[0];
    System.out.println("Game joined");
     
    MainFrame.getInstance().setContentPane(new WaitingPanel(gameName));
    MainFrame.getInstance().setVisible(true);
  }
  
  /**
   * Notifies the Client that he can receive a chat message
   * Shows the message in LobbyPanel
   */
  public void receiveChatMessage(Event event)
  {
    String message = (String)event.getArguments()[0];
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
   */
  public void loggedIn(Event event)
  {
    Session session = (Session)event.getArguments()[0];
    ClientThread.getInstance().Session = session;
    
    MainFrame.getInstance().setContentPane(MainFrame.getInstance().getLobbyPanel());
    MainFrame.getInstance().setVisible(true);
  }
  
  /**
   * Notifies the Client that he was logged out. Shows the StartPanel
   * @param event Ignored
   */   
  public void loggedOut(Event event)
  {    
    MainFrame.getInstance().setVisible(false);
    
    ClientThread.getInstance().Session = null;
    ClientThread.getInstance().Server  = null;
    
    //new ClientThread(RMIClientSocketFactoryImpl.ServerHost).start();
  }
  
  /**
   * Clientside update of the playground
   */
  public void playgroundUpdate(Event event)
  {
    Playground playground = (Playground)event.getArguments()[0];
    if(MainFrame.getInstance().getContentPane() instanceof PlaygroundPanel)
    {
      ((PlaygroundPanel)MainFrame.getInstance().getContentPane())
          .updatePlaygroundView(playground);
    }
  }
  
  /**
   * Updates the user list in the LobbyPanel.
   */
  public void userListUpdate(Event event)
  {
    List<String> users = (List<String>)event.getArguments()[0];
    LobbyPanel lobby = MainFrame.getInstance().getLobbyPanel();
    lobby.setUserList(users);
  }
  
  /**
   * This method is called for every player that dies on the playground
   * this Client is playing on.
   */
  public void playerDied(Event event)
  {
    int x = (Integer)event.getArguments()[0];
    int y = (Integer)event.getArguments()[1];
    int playerNumber = (Integer)event.getArguments()[2];
    AudioThread.playSound(Resource.getAsURL("resource/sfx/scream.wav"));
    
    PlaygroundPanel pp = (PlaygroundPanel)MainFrame.getInstance().getContentPane();
    pp.drawDieAnimation(x, y, playerNumber);
  }
  
   /**
   * This method is called when players leaves game.
   */
  public void playerLeftGame(Event event)
  {
    MainFrame.getInstance().setContentPane(MainFrame.getInstance().getLobbyPanel());
    MainFrame.getInstance().resetSize();
  }
  
  /**
   * This Method is called when Player died and therefore lost the game
   */ 
  public void youDied(Event event)
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
