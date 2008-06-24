/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bomberman.server;

import bomberman.server.api.Element;
import bomberman.server.api.Explodable;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

/**
 *
 * @author chris
 */
public class ServerLoop extends Thread
{

  private Server server;

  public ServerLoop(Server server)
  {
    this.server = server;
  }

    /*
   * Get randomized extras or null after explosion 
   * @return Element or null
   */
  private Element createRandomizedExtra(int x, int y)
  {
    Random rn = new Random();
    float i = rn.nextFloat();
    if(i < 0.1)
      return new ExtraDistance(x, y);
    else if(i < 0.2)
      return new ExtraBomb(x, y);
    else      
      return null;
  }
  
  /**
   * @return false if the bombing blast must continue in this direction.
   */
  private boolean processExplElement(Game game, Element e, int x, int y, int k)
  {
    if (e != null)
    {
      if (e instanceof ExplodableWall)
      {
        game.getPlayground().setElement(x, y, 0, createRandomizedExtra(x, y));
        return true;
      }
      else if (e instanceof Player)
      {
        System.out.println(e + " died!");
        if (e instanceof AIPlayer)
        {
          ((AIPlayer) e).die();
        }
        else
        {
          for (Entry<Session, Player> ent : this.server.getPlayers().entrySet())
          {
            if (ent.getValue().equals((Player) e))
            {
              try
              {
                this.server.getClients().get(ent.getKey()).playerDied(x, y, ent.getValue().getID());
                this.server.refresh();
                this.server.getPlayerToGame().remove(ent.getKey());
                game.removePlayer(ent.getKey());
                this.server.refresh();
              }
              catch (RemoteException re)
              {
                re.printStackTrace();
              }
            }
          }
        }

        // Remove player from game
        game.removePlayer(x, y, (Player) e);
        game.getPlayground().setElement(x, y, ((Player) e).getID(), null);

        return false;
      }
      else if (e instanceof Extra)
      {
        // Delete extra
        game.getPlayground().setElement(x, y, 0, null);
        return true;
      }      
      else if(e instanceof Bomb)
      {
        ((Bomb)e).explode();
        return false;
      }  
      else if(e instanceof SolidWall)
      {       
        return true;
      }
    }

    return false;
  }

  @Override
  public void run()
  {
    for (;;)
    {
      try
      {
        for (Entry<String, Game> entry : this.server.getGames().entrySet())
        {
          Game game = entry.getValue();

          // Check if there are enough real players or any
          // spectators left for gaming
          if (game.isRunning() &&
                  (game.getPlayerCount() == 1 ||
                  game.getPlayerSessions().size() == 0))
          {
            game.setRunning(false);

            // Send won game message to the remaining user
            // (if not AIPlayer)
            if (game.getPlayerSessions().size() > 0)
            {
              this.server.getClients().get(game.getPlayerSessions().get(0)).gameStopped(2);

            // We have to store the game result in the Highscore list
            }
            this.server.getGames().remove(game);
            break; // Stop the for-loop
          }

          // Check if it is necessary to send playground update
          // messages to the Clients
          if (game.isPlaygroundUpdateRequired())
          {
            // Updates Playground when moved
            for (Session sess : game.getPlayerSessions())
            {
              this.server.getClients().get(sess).playgroundUpdate(game.getPlayground());
            }
            // Updates Playground for Spectator when moved
            for (Session sess : game.getSpectatorSessions())
            {
              this.server.getClients().get(sess).playgroundUpdate(game.getPlayground());
            }
          }
        }

        // Process all explosions
        while (!this.server.getExplosions().isEmpty())
        {
          List<Object> explData = this.server.getExplosions().remove();
          Game game = (Game) explData.get(0);
          if (!game.isRunning()) // Game could be stopped until now
          {
            continue;
          }

          int x = (Integer) explData.get(1);
          int y = (Integer) explData.get(2);
          int dist = (Integer) explData.get(3);
          for (Session sess : game.getPlayerSessions())
          {
            this.server.getClients().get(sess).explosion(x, y, dist);
          }
          for (Session sess : game.getSpectatorSessions())
          {
            this.server.getClients().get(sess).explosion(x, y, dist);
          }

          boolean top = false;
          boolean bottom = false;
          boolean left = false;
          boolean right = false;

          
          // Delete exploded elements
          for (int i = 1; i <= dist; i++)
          {
            for (int k = 0; k < 5; k++)
            {
              // on the bomb
              Element el = game.getPlayground().getElement(x, y)[k];
              processExplElement(game, el, x, y, k);
              
              if (!right)
              {
                Element e = game.getPlayground().getElement(x + i, y)[k];
                right = processExplElement(game, e, x + i, y, k);
              }
              if (!left)
              {
                Element e = game.getPlayground().getElement(x - i, y)[k];
                left = processExplElement(game, e, x - i, y, k);
              }
              if (!top)
              {
                Element e = game.getPlayground().getElement(x, y - i)[k];
                top = processExplElement(game, e, x, y - i, k);
              }
              if (!bottom)
              {
                Element e = game.getPlayground().getElement(x, y + i)[k];
                bottom = processExplElement(game, e, x, y + i, k);
              }
            }
          }
        }

        Thread.sleep(100);
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
      }
    }
  }
}
