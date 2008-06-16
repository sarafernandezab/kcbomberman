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

import bomberman.client.api.ServerListenerInterface;
import java.awt.Point;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import bomberman.server.api.Element;
import bomberman.server.Game;
import bomberman.server.api.GameInfo;
import bomberman.server.api.InvalidSessionException;
import bomberman.server.api.ServerInterface;

/**
 * TODO: Shared resources of this class have to be synchronized
 * @author chris
 */
public class Server extends UnicastRemoteObject implements ServerInterface  
{
  public static final long serialVersionUID = 23482528;
  
  private HashMap<Session,Player>                   players = new HashMap<Session,Player>(); 
  private HashMap<Session,ServerListenerInterface>  clients = new HashMap<Session,ServerListenerInterface>(); 
  private HashMap<String, Game> games  = new HashMap<String, Game>();
  private HashMap<Session, Game> playerToGame  = new HashMap<Session, Game>();
  
  public Server() throws RemoteException
  {   
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
      gameList.add(new GameInfo(str));
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
  
  // Removes the player from game
  public void logout(Session session)  throws RemoteException          
  {
    System.out.println(players.get(session).getNickname() + " logout");
    
    players.remove(session);
    clients.remove(session);
  }
  
  public boolean move(Session session, int x, int y) throws RemoteException
  {
    checkSession(session);
    
    // get Game
    Game game = playerToGame.get(session);
    
    // get Player
    Player player = players.get(session);    
    
    // Debug
    System.out.println(player.getNickname() + " moved to (" + x + "/"+ y + ")" + "on Playground " + game.toString() );
    
    // Updates Playground when moved
    for(Session sess : game.getPlayers())
      clients.get(sess).playgroundUpdate(game.getPlayground());
    
    return false;
  }
  
  public boolean placeBomb(Session session) 
          throws RemoteException
  {
    checkSession(session);
    
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
    players.put(session, new Player(nickname));
    
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
    game.getPlayers().add(session);
    
    // Notify the client that it has joined the game
    this.clients.get(session).gameJoined(gameName);
   
    // Sends loginMessage to Waiting Panel
    for(Session sess : game.getPlayers())
      clients.get(sess).receiveChatMessage(players.get(session).getNickname() + " has joined Game");

    // Check if the game has now four players.
    // If this is the case, we have to send a game start
    // message to all players.
    if(game.getPlayers().size() == 4)
    {
      for(Session sess : game.getPlayers())
      {
        this.clients.get(sess).gameStarted();
      }
    }
    
   
    
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
        ArrayList<Session> sessions = game.getPlayers();
        for(Session sess : sessions)
        {
          ServerListenerInterface client = this.clients.get(sess);
          client.gameStarted();
        }
        
        // Notify all other clients that this game has startet,
        // but do not send notifies to clients that are currently
        // playing
        gameListUpdate();
        
        // Updates Playground when moved
        for(Session sess : game.getPlayers())
          clients.get(sess).playgroundUpdate(game.getPlayground());
        
        return true;
      }
      else
        return false; // Not allowed
    }
  }
}
