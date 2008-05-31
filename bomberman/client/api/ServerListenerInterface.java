/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bomberman.client.api;

import bomberman.server.Playground;
import bomberman.server.Session;
import bomberman.server.api.GameInfo;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Callback interface for the clients.
 * @author Kai Ritterbusch
 * @author Christian Lins
 */
public interface ServerListenerInterface extends Remote 
{
  public void receiveChatMessage(String message)
    throws RemoteException;
  
  public void loggedIn(Session session)
    throws RemoteException;
  
  /**
   * Transmittes an updated playground to the client.
   * The Playground is complete, so that no inconsistencies can occur.
   * @throws java.rmi.RemoteException
   */
  public void playgroundUpdate(Playground playground)
    throws RemoteException;
  
  public void gameListUpdate(ArrayList<GameInfo> gameList)
    throws RemoteException;
  
  /**
   * Notifies the client that is has joined the game.
   * @param gameName
   * @throws java.rmi.RemoteException
   */
  public void gameJoined(String gameName)
    throws RemoteException;
  
  /**
   * Is called if the server starts the game this client has joined.
   * @throws java.rmi.RemoteException
   */
  public void gameStarted()
    throws RemoteException;
  
  public void userListUpdate(ArrayList<String> users)
    throws RemoteException;
}
