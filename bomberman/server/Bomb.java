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

import bomberman.server.api.Explodable;
import bomberman.server.api.Element;
import java.io.Serializable;

/**
 * The BOMB!
 * @author Kai Ritterbusch (kai.ritterbusch@fh-osnabrueck.de)
 * @author Christian Lins (christian.lins@web.de)
 */
class Bomb extends Element implements Explodable, Serializable
{
  // Transient fields
  private transient Player    player;
  private transient BombTimer timer;
  
  // Serializable fields
  private int stage = 1;
  
  public Bomb(int x, int y, Player player)
  {
    super(x, y);
    this.player = player;
    
    timer = new BombTimer(this);
  }
  
  /**
   * Bomb explode
   */
  void explode()
  {
    System.out.println(this + " explodiert!");
    player.bombs.remove(this);
    player.game.getPlayground().setElement(gridX, gridY, 0, null);
    
    Server.getInstance().notifyExplosion(player.game, gridX, gridY, player.bombDistance);
    timer.cancel();
  }
  
  /**
   * Gets filename of the Image
   * @return
   */
  @Override
  public String getImageFilename()
  {
    return "resource/gfx/bomb/bomb" + stage + ".png";
  }
  
  /**
   * Updates Playground
   * @return
   */
  int tick()
  {
    player.game.forcePlaygroundUpdate();
    return ++stage;
  }
}
