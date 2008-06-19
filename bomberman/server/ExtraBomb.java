/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bomberman.server;

/**
 * Extras
 * @author Kai Ritterbusch (kai.ritterbusch@fh-osnabrueck.de)
 */
class ExtraBomb extends Extra 
{
  public static final String IMAGE_FILENAME = "resource/gfx/extras/extraBomb.png";
  
  public ExtraBomb(int x, int y)
  {
    super(x,y);
  }
  
  public String getImageFilename()
  {
    return IMAGE_FILENAME;
  }
}
