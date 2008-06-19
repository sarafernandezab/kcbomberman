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

package bomberman.client;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

/**
 * Thread playing a AudioClip.
 * @author Christian Lins (christian.lins@web.de)
 */
class AudioThread extends Thread
{
  public static int Instances = 0;
  
  private AudioClip clip;
  
  public AudioThread(URL url)
  {
    clip = Applet.newAudioClip(url);
    setPriority(MAX_PRIORITY);
  }
  
  @Override
  public void run()
  {
    try
    {
      this.clip.play();
      
      // Rescue the thread from the garbage collection, 
      // because this causes the AudioClip to stop too early.
      // While sleeping the Thread does only consume little memory and
      // no CPU which can be accepted.
      sleep(10000); 
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }
}
