/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bomberman;

/**
 *
 * @author chris
 */
public class Main 
{
  public static void main(String[] args)
  {
    for(int n = 0; n < args.length; n++)
    {
      if(args[n].equals("--client"))
      {
        new bomberman.client.Main(args).start();
      }
      else if(args[n].equals("--server"))
      {
        new bomberman.server.Main().start();
      }
    }
  }
}
