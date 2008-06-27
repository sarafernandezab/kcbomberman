/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bomberman.server.rmi;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author chris
 */
public class RMIClientSocket extends Socket
{
  public RMIClientSocket(String host, int port) throws IOException, UnknownHostException
  {
    System.out.println("DEBUG: Connecting to " + host + ":" + port);
    connect(new InetSocketAddress(host, port));
  }
}
