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

package bomberman.client.gui;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Christian Lins (christian.lins@web.de)
 */
class ExplosionTimer extends TimerTask
{
  private ElementPainter painter;
  private Timer          timer = new Timer();
  private int            calls = 0;
  
  public ExplosionTimer(ElementPainter painter, int delay, int period)
  {
    this.painter = painter;
    
    this.timer.schedule(this, delay, period);
  }
  
  @Override
  public boolean cancel()
  {
    this.timer.cancel();
    return super.cancel();
  }

  @Override
  public void run()
  {
    if(calls++ > 5)
      cancel();
    
    painter.nextExplosionImage();
    painter.repaint();
  }
}
