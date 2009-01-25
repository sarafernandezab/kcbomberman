/*
 *  KC Bomberman
 *  Copyright 2009 Christian Lins <cli@openoffice.org>
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

package bomberman.server.api;

import java.io.Serializable;
import java.util.List;

/**
 * Capsulates a method call and/or method result between server and client.
 * This is part of the Java RMI replacement. The class is serialized using
 * a platform and VM independent XML format.
 * @author Christian Lins (cli@openoffice.org)
 */
public class Event implements Serializable
{

  public static final int TYPE_CALL   = 0;
  public static final int TYPE_RESULT = 1;
  
  /** Name of the method being called on this event */
  private String   methodName = null;
  
  /** Method parameters, may be null */
  private Object[] arguments  = null;
  
  /** The result, may be null (e.g. on void methods) */
  private Object   result     = null;
  
  private int      type       = 0;
  
  /**
   * Most serializers need a default constructor.
   */
  public Event()
  {
  }
  
  public Event(String methodName, Object[] param)
  {
    this.arguments  = param;
    this.methodName = methodName;
  }
  
  public Event(String methodName, Object ret)
  {
    this.methodName = methodName;
    this.result     = ret;
  }

  public Object[] getArguments() 
  {
    return arguments;
  }

  public String getMethodName() 
  {
    return methodName;
  }

  public Object getResult() 
  {
    return result;
  }

  public void setArguments(Object[] arguments) 
  {
    this.arguments = arguments;
  }

  public void setMethodName(String methodName) 
  {
    this.methodName = methodName;
  }

  public void setResult(Object result) 
  {
    this.result = result;
  }
  
  public int getType()
  {
    return this.type;
  }
  
}
