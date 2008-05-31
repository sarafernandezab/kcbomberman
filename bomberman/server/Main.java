package bomberman.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main extends Thread
{
  @Override
  public void run()
  {
    try
    {
      Server server = new Server();
      
      // Erzeuge neue lokale Registry
      Registry registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
      
      // Binde Server an Namen
      registry.rebind("Server", server);
      
      System.out.println("Bombermanserver bereit ...");
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
      System.exit(1);
    }
  }
}
