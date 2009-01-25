/*
 *  KC Bomberman
 *  Copyright 2008 Christian Lins <christian.lins@web.de>
 *  Copyright 2008 Kai Ritterbusch <kai.ritterbusch@googlemail.com>
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package bomberman.server;

import bomberman.server.api.Element;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

/**
 * The Server loop. This thread is running in a loop while the
 * RMI @see{Server} is running.
 * @author Christian Lins (christian.lins@web.de)
 * @author Kai Ritterbusch (kai.ritterbusch@fh-osnabrueck.de)
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
        
        // Remove player from game (happens for both AI and Non-AI players)
        game.removePlayer((Player)e);
        
        // If e is an AIPlayer then it must die.
        // A Non-AI Player will trigger the if-clause in the following
        // for-loop.
        if (e instanceof AIPlayer)
        {
          ((AIPlayer) e).die();
        }
        
        // Loop through all Non-AI players
        List<Session> sessions = new ArrayList<Session>(game.getPlayerSessions());
        for (Session sess : sessions)
        {
          try
          {
            this.server.getClients().get(sess).playerDied(x, y, ((Player)e).getID());
            
            // Save this death to highscore list. 
            // Note: !(e instanceof AIPlayer) is obsolet at this point
            if (e.equals(this.server.getPlayers().get(sess)))
            {
              // Send youDied() message to client
              this.server.getClients().get(sess).youDied();

              // Remove session from game; this is important, otherwise
              // the game would not stop
              game.removePlayer(sess);
              this.server.getPlayerToGame().remove(sess);
            }
          }
          catch (Exception re)
          {
            re.printStackTrace();
          }
        }

        try
        {
          // Store the game result in the highscore
          this.server.getHighscore().hasLostGame(((Player)e).getNickname());
          // And update the game and user list on client side
          this.server.refresh();
        }
        catch(Exception ex)
        {
          ex.printStackTrace();
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
        // Stop bombing blast
        return true;
      }
    }
    // Default: continue with bombing blast.
    return false;
  }

  /**
   * This method runs in a loop while the associated @see{Server} is running.
   */
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
                  (game.getPlayerSessions().size() == 0 && game.getSpectatorSessions().size() == 0)))
          {
            game.setRunning(false);

            // Send won game message to the remaining user
            // (if not AIPlayer)
            if (game.getPlayerSessions().size() > 0)
            {
              this.server.getClients().get(game.getPlayerSessions().get(0)).gameStopped(2);
              
              // We have to store the game result in the Highscore list
              this.server.getHighscore().hasWonGame(game.getPlayers().get(0).getNickname());
            }
            
            // Send gameStopped message to spectators if existing
            if(game.getSpectatorSessions().size() > 0)
            {
              for(Session sess : game.getSpectatorSessions())
              {
                this.server.getClients().get(sess).gameStopped(0);
              }
            }            
            System.out.println("remove Game ----------");
            
            for(Session sess : game.getPlayerSessions())
              this.server.getPlayerToGame().remove(sess);
            
            this.server.getGames().remove(game.toString());
            this.server.refresh();
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

        // To reduce server load, sleep for 100 milliseconds.
        Thread.sleep(100);
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
      }
    }
  }
}
