/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bomberman.util;

/**
 * A pair of two objects.
 * @author Christian Lins (christian.lins@web.de)
 */
public class Pair<T1, T2>
{
  private T1 t1;
  private T2 t2;
  
  public Pair(T1 a, T2 b)
  {
    this.t1 = a;
    this.t2 = b;
  }
  
  public T1 getA()
  {
    return this.t1;
  }
  
  public T2 getB()
  {
    return this.t2;
  }
  
  public void setA(T1 a)
  {
    this.t1 = a;
  }
  
  public void setB(T2 b)
  {
    this.t2 = b;
  }
}
