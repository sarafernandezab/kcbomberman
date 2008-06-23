/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bomberman.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Holds all persistant user data (except @see{Highscore}).
 * @author Christian Lins (christian.lins@web.de)
 */
public class Database implements Serializable
{
  private HashMap<String, String> data = new HashMap<String, String>();
  
  /**
   * Adds a new user to the Database. If the username already exists, it
   * will be overwritten with the new values.
   * @param username
   * @param password
   */
  public void addUser(String username, String password)
  {
    this.getData().put(username, password);
  }
  
  /**
   * @return A List with all user names that are known by this Database.
   */
  public List<String> getUsers()
  {
    return new ArrayList<String>(getData().keySet());
  }
  
  /**
   * Returns the password for the given username or null if
   * the username is not known.
   * @param username
   * @return
   */
  public String getPassword(String username)
  {
    return this.getData().get(username);
  }
  
  /**
   * Deletes the given username from this Database.
   * @param username
   */
  public void removeUser(String username)
  {
    this.getData().remove(username);
  }

  public HashMap<String, String> getData() 
  {
    return data;
  }
}
