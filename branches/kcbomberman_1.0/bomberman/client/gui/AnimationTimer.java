/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bomberman.client.gui;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Main-Class of the Timer-Classes
 * Implements a timerTask for animations
 * @author Kai Ritterbusch (kai.ritterbusch@fh-osnabrueck.de)
 */
abstract class AnimationTimer extends TimerTask
{
  protected ElementPainter painter;
  protected Timer          timer    = new Timer();
  
  /**
   * Initialize the Timer
   * @param painter
   * @param delay
   * @param period
   */
  public AnimationTimer(ElementPainter painter, int delay, int period)
  {
    this.painter = painter;    
    this.timer.schedule(this, delay, period);
  }
}
