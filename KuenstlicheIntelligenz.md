## Einleitung ##

Eines der Ziele war die Entwicklung eines brauchbaren Computer-gesteuerten Gegners. Die Klasse `AIPlayer` implementiert einen solchen Spieler.
Der KI-Gegner ist Timer-gesteuert, d.h. seine Hauptroutine wird etwa alle hundert Millisekunden aufgerufen.

In dieser Hauptroutine führt der KI-Spieler nach Priorität folgende Aktionen aus:

  1. Laufe einen berechneten Pfad ab, falls vorhanden. Falls Pfad leer oder nicht ausführbar, gehe zu Schritt 2.
  1. Suche Pfad zu geeignetem Bombenziel (d.h. eine sprengbare Mauer oder ein Gegner)
  1. Suche Pfad zur Deckung, die Schutz vor Bomben bietet.

## Pfadsuche ##

Zur Pfadsuche verwenden wir den A`*`-Algorithmus, der im Umfeld der KI- und Spiele-
Programmierung recht bekannt ist. Er taucht in einer seiner Formen in nahezu jedem
professionellem Computerspiel auf.
Im Wesentlichen funktioniert der Algorithmus folgendermaßen:
Es gibt zwei Stapel mit Positionen: OpenNodes und ClosedNodes

OpenNodes enthält zu Beginn nur die Startposition. ClosedNodes ist leer.
Der Ablauf:
  1. Nimm eine Position aus OpenNodes. Falls nicht möglich --> Abbruch.
  1. Falls die Position der gewünschte Zielpunkt ist, berechne den Weg mit Hilfe von ClosedNodes zurück: --> Erfolg.
  1. Falls die Position nicht der gewünschte Zielpunkt ist, suche alle Nachbarpunkt der Position und füge sie OpenNodes hinzu. Falls es Nachbarn gab, füge die Position der ClosedNodes-Liste hinzu.

Dieser Ablauf beschreibt einen vereinfachten A`*`-Algorithmus, der nicht den optimalen Pfad
sondern den zuerst gefunden Pfad zurück gibt. Dies vereinfacht den Aufwand erheblich, bei langen Wegen kann der Pfad jedoch recht unrealistisch aussehen. In einer späteren Version werden wir dies vielleicht beheben.