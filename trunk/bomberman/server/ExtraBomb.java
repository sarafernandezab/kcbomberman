/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bomberman.server;

/**
 *
 * @author kai
 */
class ExtraBomb extends Extra 
{
  public static final String IMAGE_FILENAME = "resource/gfx/extra/extraBomb.png";
  
  public ExtraBomb(int x, int y)
  {
    super(x,y);
  }
  
  public String getImageFilename()
  {
    return IMAGE_FILENAME;
  }
}
