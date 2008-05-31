package bomberman.server;

import bomberman.server.api.Element;

public class Player extends Element
{
  private String   nickname;
     
  public Player(String nickname)
  {
    this.nickname = nickname;
  }
     
  public String getNickname()
  {
    return this.nickname;
  }
  
  @Override
  public String toString()
  {
    return this.nickname;
  }
}
