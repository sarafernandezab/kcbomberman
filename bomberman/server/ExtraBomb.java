/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bomberman.server;

/**
 * Additional bomb extra.
 * @author Kai Ritterbusch (kai.ritterbusch@fh-osnabrueck.de)
 */
class ExtraBomb extends Extra 
{
  public static final String IMAGE_FILENAME = "resource/gfx/extras/extraBomb.png";
  
  public ExtraBomb(int x, int y)
  {
    super(x,y);
  }
  
  /**
  * Gets the filename of an Image
  * @return Filename
  */
  public String getImageFilename()
  {
    return IMAGE_FILENAME;
  }
}
