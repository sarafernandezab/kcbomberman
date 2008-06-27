/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bomberman.server.rmi;

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author chris
 */
public class RMIServerSocket extends ServerSocket
{
  public RMIServerSocket(int port) throws IOException
  {
    super(port, 255, null);
  }
}
