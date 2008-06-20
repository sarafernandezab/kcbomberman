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
import java.io.InputStream;
import java.net.URL;
import javazoom.jl.player.Player;

/**
 * Thread playing a AudioClip.
 * @author Christian Lins (christian.lins@web.de)
 */
public class AudioThread extends Thread
{
  public static int Instances = 0;
  
  private AudioClip   clip = null;
  private InputStream in   = null;
  
  private AudioThread()
  {
    setPriority(MAX_PRIORITY);
  }
  
  public AudioThread(URL url)
  {
    this();
    
    this.clip = Applet.newAudioClip(url);
  }
  
  public AudioThread(InputStream in)
  {
    this.in = in;
  }
  
  @Override
  public void run()
  {
    try
    {
      if(clip != null)
      {
        this.clip.play();

        // Rescue the thread from the garbage collection, 
        // because this causes the AudioClip to stop too early.
        // While sleeping the Thread does only consume little memory and
        // no CPU which can be accepted.
        sleep(10000);
      }
      else
      {
        Player player = new Player(in);
        player.play();
      }
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }
}
