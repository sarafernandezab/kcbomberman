/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bomberman.client.gui;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author kai
 */
public class DieTimer extends TimerTask
{
  private ElementPainter painter;
  private Timer          timer = new Timer();
  private int            calls = 0;
  
  public DieTimer(ElementPainter painter, int delay, int period)
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
    
    painter.nextDieImage();
    painter.repaint();
  }
}
