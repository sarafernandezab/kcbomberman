/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bomberman.client.gui;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Kai Ritterbusch (kai.ritterbusch@fh-osnabrueck.de)
 */
public class DieTimer extends AnimationTimer
{
  private ElementPainter painter;
  //private Timer          timer = new Timer();
  private int            calls = 0;
  
  public DieTimer(ElementPainter painter, int delay, int period)
  {
    super(painter, delay, period);
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
