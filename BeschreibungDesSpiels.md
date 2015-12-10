Das Spiel ist die Implementierung des bekannten Bomberman-Spielprinzips in Java. Ziel des Spiels ist es, alle Gegner und Hindernisse in einem labyrinthartigen Spielfeld durch geschicktes taktisches Platzieren von Bomben zu zerstören. Daher auch der Name Bomberman.

Das Spielfeld besteht aus zweidimensionalen Matrix von zerstörbaren und unzerstörbaren Wänden.

Durch das Legen von Bomben können zuvor unerreichbare Gebiete erschlossen werden, in denen
sich z.B. Powerups oder Gegner befinden. Durch die Aufnahme von Powerups kann der Charakter z.B. die Menge der legbaren Bomben oder deren Explosionsreichweite erhöhen.

Die Explosion wird durch Feuerstrahlen in alle vier Richtungen des zweidimensionalen Raums
dargestellt und bringt andere Bomben zur sofortigen Zündung, was gewisse Taktiken ermöglicht und erfordert.

Das Spiel läuft für alle Spieler gleichzeitig ab, d.h. alle Spieler können zur gleichen Zeit die Spielfigur bewegen und Bomben legen (Echtzeit!). Die Bewegung ist nur in X- und Y-Richtung möglich, sofern sie nicht durch Mauern behindert wird. Eine gelegte Bombe benötigt einige Sekunden bis sie explodiert. Sie kann nicht überquert werden. Dies erlaubt spannende Taktiken, z.B. kann man durch trickreiches Platzieren einer Bombe gegnerische Spieler in Mauerspalten quasi einsperren, der dadurch der Explosion der Bombe nicht entkommen kann.

Ein Spieler verliert, wenn er mit der Spielfigur in den Feuerstrahl einer Explosion gerät und die Spielfigur virtuell "stirbt". Die letzte verbliebene Spielfigur gewinnt das Spiel.


Wesentliche Spielelemente sind:
  * Die Spielfiguren
  * Die Bomben
  * Die Powerups
  * Die zerstörbaren oder unzerstörbaren Mauerelemente