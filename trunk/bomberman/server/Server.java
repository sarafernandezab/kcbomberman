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

import bomberman.util.CHAP;
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
import bomberman.server.gui.UserListTableModel;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * TODO: Shared resources of this class have to be synchronized
 * @author chris
 */
public class Server extends UnicastRemoteObject implements ServerInterface  
{
  public static final long serialVersionUID = 23482528;
  
  private static Server instance;
  
  static Server getInstance()
  {
    return instance;
  }
  
  private CHAP                     chap         = new CHAP();
  private HashMap<Session,Player>  players      = new HashMap<Session,Player>(); 
  private HashMap<Session,ServerListenerInterface>  clients = new HashMap<Session,ServerListenerInterface>(); 
  private HashMap<String, Game>    games        = new HashMap<String, Game>();
  private HashMap<Session, Game>   playerToGame = new HashMap<Session, Game>();
  private HashMap<Session, String> playerToIP   = new HashMap<Session, String>();
  private Queue<List<Object>>      explosions   = new ArrayBlockingQueue<List<Object>>(25);
  private Logger logger = new Logger();
  private Database  database  = null;
  private Highscore highscore = null;
  
  public Server() throws RemoteException
  {
    instance = this;
    
    // Load database and highscore
    try
    {
      XStream xstream = new XStream(new DomDriver());
      this.database  = (Database)xstream.fromXML(new FileInputStream(ShutdownThread.DATABASE_FILE));
      this.highscore = (Highscore)xstream.fromXML(new FileInputStream(ShutdownThread.HIGHSCORE_FILE));
    }
    catch(Exception ex)
    {
      this.database  = new Database();
      this.highscore = new Highscore();
      System.out.println(ex.getLocalizedMessage());
      System.out.println("No persistent database/highscore found!");
    }

    Thread updater = new ServerLoop(this);
    //updater.setPriority(Thread.MIN_PRIORITY);
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
  
  HashMap<Session,ServerListenerInterface> getClients()
  {
    return this.clients;
  }
  
  Queue<List<Object>> getExplosions()
  {
    return this.explosions;
  }
  
  HashMap<String, Game> getGames()
  {
    return this.games;
  }
  
  HashMap<Session, Player> getPlayers()
  {
    return this.players;
  }
  
  HashMap<Session, Game> getPlayerToGame()
  {
    return this.playerToGame;
  }
  
  void notifyExplosion(Game game, int x, int y, int distance)
  {
    List<Object> data = new ArrayList<Object>();
    data.add(game);
    data.add(x);
    data.add(y);
    data.add(distance);
    this.explosions.add(data);
  }
  
  /**
   * Sends a game list update to all client that are not playing.
   */
  private void gameListUpdate() throws RemoteException
  {
    ArrayList<GameInfo> gameList = new ArrayList<GameInfo>();
    Set<String> gameNames = this.games.keySet();
    for(String str : gameNames)
    {
      Game   game     = this.games.get(str);
      Player creator  = players.get(game.getCreator());
      if(creator == null)
      {
        // Stop the game
        stopGame(game);
      }
      else
        gameList.add(new GameInfo(str, creator.getNickname(), game.isRunning(), game.getPlayerCount()));
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

  /**
   * Logout with username
   */ 
  public void logout(String userName) throws RemoteException
  {
    for(Entry<Session, Player> ent : players.entrySet())
    {
      if(ent.getValue().getNickname().equals(userName))
      {
        Game game = playerToGame.get(ent.getKey());
        if(game != null)
        {
          // send logoutmessage to other players in the game
          if(game.getCreator().equals(ent.getKey()))
            stopGame(game);
        }
        this.clients.get(ent.getKey()).loggedOut();
        players.remove(ent.getKey());        
        clients.remove(ent.getKey());
        
        refresh();
      }
    }
  }
  
  /*
   * Removes Player from Playground, e.g. when he presses the 
   * Escape-Key
   */  
  public void leaveGame(Session session) throws RemoteException
  {
    Game game = playerToGame.get(session);
    
    // Removes player from gameList
    playerToGame.remove(session); 
    
    // Removes player from game
    game.removePlayer(session);
    
    // Updates Playground when moved
    for(Session sess : game.getPlayerSessions())
      clients.get(sess).playgroundUpdate(game.getPlayground());
    
    if(game.getPlayerSessions().size() == 0)
      for(Session sess : game.getSpectatorSessions())
        clients.get(sess).playerLeftGame();
  }
  
  /**
   * Logs out a client. 
   */
  public void logout(Session session)  
    throws RemoteException          
  {
    // Log-Message
    if(ServerControlPanel.getInstance() != null)
    {
      ServerControlPanel.getInstance().addLogMessages(players.get(session).getNickname() + " logout");
      ((UserListTableModel)ServerControlPanel.getInstance().getTblUserList().getModel()).setDataForUsername(players.get(session).getNickname(), "offline");            
    }
    System.out.println(players.get(session).getNickname() + " logout");    
       
    Game game = playerToGame.get(session);
    
    // Send logoutmessage to other players in the game, if the player
    // was the game creator and the game is not running.
    // This is necessary, because if we did not cancel the Game no one
    // can ever start it if the creator has logged out
    if(game != null && !game.isRunning() && game.getCreator().equals(session))
      stopGame(game);
    
    // Remove all session references
    players.remove(session);
    clients.remove(session);
    playerToGame.remove(session);
    playerToIP.remove(session);
    
    refresh();
  }
  
  /**
   * Stopps a game and sends a gameStopped() message to all players
   * playing this specific game. Additionally this method stops all
   * AIPlayerThreads running with this game.
   * @param game
   * @throws java.rmi.RemoteException
   */
  public void stopGame(Game game) throws RemoteException
  {
    game.setRunning(false);
    
    // delete player <-> game connection
    for(Entry<Session, Game> e : playerToGame.entrySet())
    {
      if(e.getValue().equals(game.toString()))
        playerToGame.remove(e);
    }    
    
    // Send gameStopped() message to all players
    for(Session sess : game.getPlayerSessions())
    {
      ServerListenerInterface sli = clients.get(sess);
      if(sli != null)
        clients.get(sess).gameStopped(1); // Condition 1 means "Stopped by Server"
    }
    
    // Send gameStopped() message to all spectators
    for(Session sess : game.getSpectatorSessions())
    {
      ServerListenerInterface sli = clients.get(sess);
      if(sli != null)
        clients.get(sess).gameStopped(1);
    }
    
    // Log-Message
    if(ServerControlPanel.getInstance() != null)
    {
      ServerControlPanel.getInstance().removeGame(games.get(game.toString()));
      ServerControlPanel.getInstance().addLogMessages("Spiel: " + game.toString() +" wurde durch Server beendet");      
    }
    
    games.remove(game.toString());
    
    refresh();
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
        clients.get(sess).playgroundUpdate(game.getPlayground()); // TODO: sync
            
      // Updates Playground for Spectators when moved
      for(Session sess : game.getSpectatorSessions())
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
  
  /**
   * Client wants to login with the given username. This method is
   * part one of the Challenge Handshake Authentification Protocol (CHAP)
   * and returns a challenge that is valid for a few seconds (default: 30s).
   * @param username
   * @param sli
   * @return A challenge > 0 if the username is valid, otherwise 0 is
   * returned.
   * @throws java.rmi.RemoteException
   */
  public long login1(String username)
  {
    String pw = this.database.getPassword(username);
    if(pw == null)
      return 0;
    else
    {
      // Create a challenge
      return this.chap.createChallenge(username);
    }
  }
  
  /**
   * Second part of the Challenge Handshake Authentification Protocol (CHAP).
   * If the login is successful the Server will transmit a Session object
   * through the given @see{ServerListenerInterface}.
   * @param username
   * @param hash
   * @return
   * @throws java.rmi.RemoteException
   */
  public boolean login2(String username, long hash, ServerListenerInterface sli, String ip)
    throws RemoteException
  {
    if(this.chap.isValid(username)) // Has this username performed a login1?
    {
      String pw = this.database.getPassword(username);
      if(pw == null)
        return false;
      else
      {
        long challenge = this.chap.getChallenge(username);
        long myHash    = this.chap.createChecksum(challenge, pw);
        
        if(myHash == hash)
          return login(username, pw, sli, ip);
        else
          return false;
      }
    }
    else
      return false;
  }
  
  /**
   * This method is only used internally by login2 method.
   * @param nickname
   * @param password
   * @param sli
   * @param ip
   * @return
   * @throws java.rmi.RemoteException
   */
  private boolean login(String nickname, String password, ServerListenerInterface sli, String ip) 
    throws RemoteException
  {
    Session session = new Session();
    
    System.out.println(nickname + " hat sich eingeloggt");
    
    // Checks if user is allowed to login
    if(database.getPassword(nickname) == null)
      return false;
    else if(!database.getPassword(nickname).equals(password))
      return false;
    
    // register in Playerlist
    Player player = new Player(null, nickname);
    players.put(session, player);
    
    // register in IPlist
    playerToIP.put(session, ip);
    
    // Logger
    logger.addLogMessage("login", ip);    
    
    // Userlist update
    if(ServerControlPanel.getInstance() != null)
    {
      ((UserListTableModel)ServerControlPanel.getInstance().getTblUserList().getModel()).setDataForUsername(nickname, "online");      
    }
    
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
    
    return true;
  }
  
  /**
   * Sends both user and game list to all clients.
   * @throws java.rmi.RemoteException
   */
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
  public boolean stopGame(String gameName) throws RemoteException
  {
    // All players of this Game will logged out        
    stopGame(games.get(gameName));
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
    
    // Logger
    logger.addLogMessage("joinViewGame", playerToIP.get(session));
    
    Game game = games.get(gameName);    
    Player player = players.get(session);  
    game.getSpectatorSessions().add(session);
     // Notify the client that it has joined the game
    this.clients.get(session).gameJoined(gameName);
    
    if(game.isRunning())
    {
      this.clients.get(session).gameStarted(true);
      clients.get(session).playgroundUpdate(game.getPlayground());
    }       
      
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
   
    if(game == null)
      return false; // No such game
    
    Player player = players.get(session);   
    if(!game.addPlayer(player))
      return false;
    
    playerToGame.put(session, game);
    // Logger
    logger.addLogMessage("joinGame", playerToIP.get(session));
    
    // Add the client as player
    game.getPlayerSessions().add(session);
    
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
    
    // Logger
    logger.addLogMessage("createGame", playerToIP.get(session));
    
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
    
    // Logger
    logger.addLogMessage("login", playerToIP.get(session));
    
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

  public Database getDatabase()
  {
    return database;
  }

  public Highscore getHighscore()
  {
    return highscore;
  }
}
