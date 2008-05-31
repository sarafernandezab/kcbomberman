/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bomberman.server.api;

import java.rmi.RemoteException;

/**
 * This exception is thrown when the client submits an
 * invalid session. A valid client program must show the
 * login screen as a result to this exception.
 * @author Christian Lins
 */
public class InvalidSessionException extends RemoteException
{

}
