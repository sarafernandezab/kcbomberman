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

package bomberman.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import bomberman.client.api.ServerListenerInterface;
import bomberman.server.api.GameInfo;
import bomberman.server.api.InvalidSessionException;
import bomberman.server.api.ServerInterface;
import bomberman.server.gui.ServerControlPanel;
import java.util.List;
import java.util.Map.Entry;

/**
 * TODO: Shared resources of this class have to be synchronized
 * @author chris
 */
public class Server extends UnicastRemoteObject implements ServerInterface  
{
  public static final long serialVersionUID = 23482528;
  
  private HashMap<Session,Player>                   players = new HashMap<Session,Player>(); 
  private HashMap<Session,ServerListenerInterface>  clients = new HashMap<Session,ServerListenerInterface>(); 
  private HashMap<String, Game>                     games  = new HashMap<String, Game>();
  private HashMap<Session, Game>                    playerToGame  = new HashMap<Session, Game>();
  
  public Server() throws RemoteException
  {
    Runnable run = new Runnable()
    {
      @Override
      public void run()
      {
        for(;;)
        {
          for(Entry<String, Game> e : games.entrySet())
          {
            try
            {
              if(e.getValue().isPlaygroundUpdateRequired())
              {
                Game game = e.getValue();
                // Updates Playground when moved
                for(Session sess : game.getPlayerSessions())
                  clients.get(sess).playgroundUpdate(game.getPlayground());
              }
              Thread.sleep(100);
            }
            catch(Exception ex)
            {
              ex.printStackTrace();
            }
          }
        }
      }
    };
    Thread updater = new Thread(run, "Playground updater");
    updater.setPriority(Thread.MIN_PRIORITY);
    updater.setDaemon(true);
    updater.start();
    
    // Log-Message
    if(ServerControlPanel.getInstance() != null)
      ServerControlPanel.getInstance().addLogMessages("ServerInstanz erstellt");
    System.out.println("ServerInstanz erstellt");
  }
  
  /**
   * Checks if the Session is still valid. If it is not
   * valid an InvalidSessionException is thrown to the remote
   * client.
   * @param session
   * @return
   */
  private void checkSession(Session session)
          throws InvalidSessionException
  {
    if(!clients.containsKey(session))
      throw new InvalidSessionException();
  }
  
  /**
   * Sends a game list update to all client that are not playing.
   */
  private void gameListUpdate()
  {
    ArrayList<GameInfo> gameList = new ArrayList<GameInfo>();
    Set<String> gameNames = this.games.keySet();
    for(String str : gameNames)
    {
      Game game = this.games.get(str);
      gameList.add(new GameInfo(str, players.get(game.getCreator()).getNickname(), game.isRunning(), game.getPlayerCount() ));
    }
    
    Set<Session> sessions = this.clients.keySet();
    for(Session session : sessions)
    {
      try
      {
        ServerListenerInterface client = this.clients.get(session);
        client.gameListUpdate(gameList);
      }
      catch(Exception ex)
      {
        System.err.println("Exception while notifying client: " + ex.getLocalizedMessage());
      }
    }
  }
  
  // Logout with username
  public void logout(String userName) throws RemoteException
  {
    for(Entry<Session, Player> ent : players.entrySet() )
      if(ent.getValue().getNickname().equals(userName))
      {
        Game game = playerToGame.get(ent.getKey());
        if(game != null)
        {
          // send logoutmessage to other players in the game
          if(game.getCreator().equals(ent.getKey()))
            logoutMessage(game);
        }
        this.clients.get(ent.getKey()).loggedOut();
        players.remove(ent.getKey());        
        clients.remove(ent.getKey());
        
        // Build list of usernames
        ArrayList<String> nicknames = new ArrayList<String>();
        for(Session sess : players.keySet())    
          nicknames.add(players.get(sess).getNickname());       
        
        // Notify all users of the new user 
        for(Session sess : clients.keySet())    
          clients.get(sess).userListUpdate(nicknames);
      }      
  }
  
  // Removes the player from game
  public void logout(Session session)  throws RemoteException          
  {
    // Log-Message
    if(ServerControlPanel.getInstance() != null)
      ServerControlPanel.getInstance().addLogMessages(players.get(session).getNickname() + " logout");
    System.out.println(players.get(session).getNickname() + " logout");    
       
    Game game = playerToGame.get(session);     
    // send logoutmessage to other players in the game
    if(game.getCreator().equals(session))
      logoutMessage(game);
    
    players.remove(session);
    clients.remove(session);
    
    // Build list of usernames
    ArrayList<String> nicknames = new ArrayList<String>();
    for(Session sess : players.keySet())    
      nicknames.add(players.get(sess).getNickname());
    
    // Notify all users of the new user 
    for(Session sess : clients.keySet())    
      clients.get(sess).userListUpdate(nicknames);
    
    gameListUpdate();
  }
  
  // Sends logout-Message if Creator stopped Game
  public void logoutMessage(Game game) throws RemoteException
  {   
    for(Session sess : game.getPlayerSessions())
      clients.get(sess).gameStopped();
    games.remove(game.toString()); 
  }
     
  /**
   * Notifies the Server of a Client movement.
   * @param session
   * @param x
   * @param y
   * @return
   * @throws java.rmi.RemoteException
   */
  public boolean move(Session session, int x, int y) throws RemoteException
  {
    if(x == y)
      return false;
        
    checkSession(session);
    
    // Get Game
    Game game = playerToGame.get(session);
    
    // Get Player
    Player player = players.get(session);    
    
    if(game.movePlayer(player, x, y))
    {
      // Updates Playground when moved
      for(Session sess : game.getPlayerSessions())
        clients.get(sess).playgroundUpdate(game.getPlayground());
      return true;
    }
    else
      return false;
  }
  
  public boolean placeBomb(Session session) 
          throws RemoteException
  {
    checkSession(session);
    
    Player player = this.players.get(session);
    if(player != null)
    {
      player.placeBomb();
      return true;
    }
    else
      return false;
  }
  
  public void sendChatMessage(Session session, String message) 
          throws RemoteException
  {
    checkSession(session);
    
    String answer = players.get(session) + ": " + message;

    for(Session sess : clients.keySet())    
      clients.get(sess).receiveChatMessage(answer); // TODO: Spielende Player ausblenden
  }
  
  public void login(String nickname, ServerListenerInterface sli) 
          throws RemoteException
  {
    Session session = new Session();
    
    System.out.println(nickname + " hat sich eingeloggt");
    
    // register in Playerlist
    Player player = new Player(null, nickname);
    players.put(session, player);
    
    // Log-Message
    if(ServerControlPanel.getInstance() != null)
    {
      ServerControlPanel.getInstance().addLogMessages(nickname + " hat sich eingeloggt");
      ServerControlPanel.getInstance().addPlayer(player);
    }
    
    // register in Clientlist
    clients.put(session, sli);
    
    // loggedin action
    sli.loggedIn(session);
    
    // Build list of usernames
    ArrayList<String> nicknames = new ArrayList<String>();
    for(Session sess : players.keySet())    
      nicknames.add(players.get(sess).getNickname());
    
    // Notify all users of the new user 
    for(Session sess : clients.keySet())    
      clients.get(sess).userListUpdate(nicknames);
    
    // Updates GameList
    gameListUpdate();
  }
  
  
  public void refresh() throws RemoteException
  {
     // Updates GameList
    gameListUpdate();
    
     // Build list of usernames
    ArrayList<String> nicknames = new ArrayList<String>();
    for(Session sess : players.keySet())    
      nicknames.add(players.get(sess).getNickname());
    
    // Notify all users of the new user 
    for(Session sess : clients.keySet())    
      clients.get(sess).userListUpdate(nicknames);
    
    
  }
  
  /**
   * Stopps and deletes a game.
   * @param gameName
   * @return
   * @throws java.rmi.RemoteException
   */
  public boolean closeGame(String gameName) throws RemoteException
  {    
    // delete player <-> game connection
    for(Entry<Session, Game> e : playerToGame.entrySet())
    {
      if(e.getValue().equals(gameName))
        playerToGame.remove(e);
    }
    
    // All players of this Game will logged out        
    logoutMessage(games.get(gameName));
    
    // close the game
    games.remove(gameName).setRunning(false);         
    
    // Log-Message
    if(ServerControlPanel.getInstance() != null)
    {
      ServerControlPanel.getInstance().addLogMessages("Spiel: " + gameName +" wurde durch Server beendet");      
    }
    
    refresh();
    
    return true;
  }
  
   /**
   * 
   * @param session
   * @param gameName
   * @return
   * @throws java.rmi.RemoteException
   */
  public boolean joinViewGame(Session session, String gameName) throws RemoteException
  {
    checkSession(session);
    
    Game game = games.get(gameName);    
    Player player = players.get(session);  
     // Notify the client that it has joined the game
    this.clients.get(session).gameJoined(gameName);
    return false;
  }
  
  /**
   * 
   * @param session
   * @param gameName
   * @return
   * @throws java.rmi.RemoteException
   */
  public boolean joinGame(Session session, String gameName) throws RemoteException
  {
    checkSession(session);
    
    Game game = games.get(gameName);
   
    playerToGame.put(session, game);
    if(game == null)
      return false; // No such game
    
    // Add the client as player
    game.getPlayerSessions().add(session);
    
    Player player = players.get(session);   
    game.addPlayer(player);
    
    // Notify the client that it has joined the game
    this.clients.get(session).gameJoined(gameName);
   
    // Sends loginMessage to Waiting Panel
    for(Session sess : game.getPlayerSessions())
      clients.get(sess).receiveChatMessage(players.get(session).getNickname() + " has joined Game");

    // Check if the game has now four players.
    // If this is the case, we have to send a game start
    // message to all players.
    if(game.getPlayerSessions().size() == 4)
    {
      for(Session sess : game.getPlayerSessions())
      {
        this.clients.get(sess).gameStarted(false);
      }
      for(Session sess : game.getSpectatorSessions())
      {
        this.clients.get(sess).gameStarted(true);
      }
    }
    
    gameListUpdate();
    
    return false;
  }
  
  /**
   * Creates a new game identified by the given name.
   * @param session
   * @param gameName
   * @return
   * @throws java.rmi.RemoteException
   */
  public boolean createGame(Session session, String gameName) 
          throws RemoteException
  {
    checkSession(session);
    
    if(games.containsKey(gameName)) // There is a game with this name
      return false;
    else
    {
      // Create a new Game
      Game game = new Game(gameName, session);
      games.put(gameName, game);

      // Log-Message
      if(ServerControlPanel.getInstance() != null)
      {
        ServerControlPanel.getInstance().addLogMessages("Spiel: " + gameName +" wurde erstellt");
        ServerControlPanel.getInstance().addGame(game);
      }
      
      // Send a game list update
      gameListUpdate();
      
      return true;
    }
  }    

  /**
   * 
   * @param session
   * @param gameName
   * @return
   * @throws java.rmi.RemoteException
   */
  public boolean startGame(Session session, String gameName) 
          throws RemoteException
  {
    checkSession(session);
    System.out.println("Session ok");
    if(!games.containsKey(gameName)) // No such game
      return false;
    else
    {
      System.out.println("Spiel vorhanden ok");
      Game game = this.games.get(gameName);
      
      // Check if the client is the creator of the game,
      // say it is allowed to start the game
      if(game.getCreator().equals(session))
      {
        System.out.println("Creator ok");
        // Send game start message
        List<Session> sessions      = game.getPlayerSessions();
        List<Session> specSessions  = game.getSpectatorSessions();
        for(Session sess : sessions)
        {
          ServerListenerInterface client = this.clients.get(sess);
          client.gameStarted(false);
        }
        
        // Send start message to all Spectators 
        for(Session sess : specSessions)
        {
          ServerListenerInterface client = this.clients.get(sess);
          client.gameStarted(true);
        }
        
        // Updates Playground when moved
        for(Session sess : game.getPlayerSessions())
          clients.get(sess).playgroundUpdate(game.getPlayground());
        
        // Updates Playground when moved
        for(Session sess : game.getSpectatorSessions())
          clients.get(sess).playgroundUpdate(game.getPlayground());
        
        // Add AI-controlled players if there are not enough human players
        game.addAI();
        game.setRunning(true);
        
        // Notify all other clients that this game has startet,
        // but do not send notifies to clients that are currently
        // playing
        gameListUpdate();
        
        return true;
      }
      else
        return false; // Not allowed
    }
  }
}
