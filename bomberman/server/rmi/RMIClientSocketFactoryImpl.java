/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bomberman.server.rmi;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.rmi.server.RMIClientSocketFactory;

/**
 *
 * @author chris
 */
public class RMIClientSocketFactoryImpl 
        implements RMIClientSocketFactory, Serializable
{
  public static String ServerHost = "localhost";
  public Socket createSocket(String host, int port)
          throws IOException
  {
    return new RMIClientSocket(ServerHost, port);
  }
}
