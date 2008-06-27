/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bomberman.server.rmi;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.rmi.server.RMIServerSocketFactory;

/**
 *
 * @author chris
 */
public class RMIServerSocketFactoryImpl 
        implements RMIServerSocketFactory, Serializable
{
  public ServerSocket createServerSocket(int port)
          throws IOException
  {
    return new RMIServerSocket(port);
  }
}
