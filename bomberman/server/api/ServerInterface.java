package bomberman.server.api;

import bomberman.server.Game;
import bomberman.client.api.ServerListenerInterface;
import java.rmi.Remote;
import java.rmi.RemoteException;

import bomberman.server.Session;

/**
 * Main interface for the game server. 
 * @author Christian Lins
 * @author Kai Ritterbusch
 */
public interface ServerInterface extends Remote
{
  void login(String nickname, ServerListenerInterface sli)
    throws RemoteException;
  
  void logout(Session session)
    throws RemoteException;
  
  void sendChatMessage(Session session, String message)
    throws RemoteException, InvalidSessionException;
  
  boolean createGame(Session session, String gameName)
    throws RemoteException, InvalidSessionException;

  boolean joinGame(Session session, String gameName)
    throws RemoteException, InvalidSessionException;
  
  boolean startGame(Session session, String gameName)
    throws RemoteException, InvalidSessionException;
  
    /**
   * Is called when a Client has pressed its moving keys.
   * It is not possible to move in both directions at once,
   * so either x or y must be zero. If they are both != zero
   * the method will always return false.
   * @param session Session that identifies the Client.
   * @param x Direction on x-axis.
   * @param y Direction on y-axis
   * @return 
   * @throws java.rmi.RemoteException
   */
  boolean move(Session session, int x, int y) 
    throws RemoteException, InvalidSessionException;
  
  /**
   * Is called when a client has pressed 'Space' which is
   * usually the key for placing a bomb.
   * @param session
   * @return
   * @throws java.rmi.RemoteException
   */
  boolean placeBomb(Session session)
    throws RemoteException, InvalidSessionException;
  

}
