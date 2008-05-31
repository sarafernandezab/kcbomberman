package bomberman.client;

import bomberman.client.gui.MainFrame;
import bomberman.server.Session;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import bomberman.server.api.ServerInterface;
import java.rmi.ConnectException;
import javax.swing.JOptionPane;

public class Main extends Thread
{
  public static ServerInterface Server;
  public static ServerListener  ServerListener;
  public static Session         Session;
  
  private String[] args;
  
  // Konstruktor mit uebergabe von Argumenten
  public Main(String[] args)
  {
    this.args = args;
  }
  
  @Override
  public void run()
  {
    Registry registry;
    try
    {
      // Create main frame
      new MainFrame().setVisible(true);
      
      ServerListener = new ServerListener();
      
      if(this.args.length <= 1)
        registry = LocateRegistry.getRegistry(Registry.REGISTRY_PORT);
      else
        registry = LocateRegistry.getRegistry(args[1], Registry.REGISTRY_PORT);
      
      Server = (ServerInterface)registry.lookup("Server");
    }
    catch (ConnectException ex)
    {
      JOptionPane.showMessageDialog(MainFrame.getInstance(), "Server läuft nicht!");
    }
    catch (Exception ex) 
    {
      ex.printStackTrace();
    }
  }
}
