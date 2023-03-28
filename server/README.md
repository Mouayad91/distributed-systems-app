# PIB-VS_WiSe22_Gruppe05

Verteilter Terminkalender

Dies ist ein Gruppenprojekt, welches im Rahmen der Vorlesung "Verteilte Systeme" stattfindet. Das Ziel dieses Projektes ist es, einen Dienst zur Verwaltung von Terminen zu implementieren.  
Jedem User soll ein Terminkalender zur Verfügung gestellt werden. 
Außerdem sollen User Funktionen, wie hinzufügen, entfernen und ändern von Terminen ausführen können.
 Zudem sollen User andere User zu Terminen einladen können, welche dann ablehnen oder annehmen können sollen.

## Architektur

Stellen Sie die Architektur Ihres Projekts dar. Beginnen Sie mit einem Abschnitt zur Lösungsstrategie. D.h. eine kompakte Beschreibung der Kernidee und des Lösungsansatzes. Beschreiben Sie wichtige Designentscheidungen und begründen Sie diese.

#### Use Cases

Darstellung der Use Cases. Optional ergänzt durch User Stories aus denen die Use Cases abgeleitet werden.

#### Anforderungen
Stellen Sie in der unten aufgeführten Tabelle übersichtlich dar, welche Anforderungen Sie umgesetzt haben. Erläutern Sie **stichhaltig**, wie Sie die Anforderung umgesetzt haben. Stellen Sie dar, durch welche Architekturelmente und Designentscheidungen die Anforderungen erfüllt sind. Bei teilweise erfüllten Anforderungen gehen Sie darauf ein, welche Aspekte fehlen.

Wenn Sie weitere, über das Anforderungsdokument hinausgehende Anforderungen umgesetzt haben, listen Sie diese bitte hier auf und nummerieren Sie die Anforderungen mit E1 bis En.

Anforderung | Erfüllung (ja, nein, teilweise) | Erläuterung
-------- | -------- | --------
*MUST Have Anforderungen*
M1   |    |
M2   |    |
...   |    |
*SHOULD Have Anforderungen*
S1   |    |
S2   |    |
...   |    |
*COULD Have Anforderungen*
C1   |    |
C2   |    |
...   |    |
*++ Anforderungen*
++1   |    |
++2   |    |
...   |    |
*Weitere, eigene Anforderungen*
E1   |    |
E2   |    |
...   |    |


#### Lösungsstrategie
Geben Sie eine kompakte Beschreibung der Kernidee Ihres Lösungsansatzes. Stellen Sie dar, welche Technologien Sie einsetzen. Begründen Sie wichtige Designentscheidungen. Z.B. die Wahl der Middleware, der Programmiersprache, des Architekturansatzes etc.

Erläutern Sie, welche aus der Vorlesung bekannten Architekturprinzipien Sie umgesetzt haben und begründen Sie warum. Stellen Sie dar, in welcher Form diese Aspekte in ihre Lösung eingeflossen sind. Gehen Sie insb. auf folgende Aspekte ein:
- Transparancy: Welche Aspekte der Transparancy sind umgesetzt? Wie? Warum?
- Softwarearchitektur
- Systemarchitektur
- Request-Reply Pattern (Synchron oder Asynchron)
- Thin-Client vs. Fat-Client
- Stateless vs. stateful Server-Desgin
- Fehlersemantik
- Idempotenz
- Skalierbarkeit

Stellen Sie dar, wie sie die einzelnen Anforderungen umgesetzt haben.

#### Statisches Modell

###### Komponentendiagramm

###### Verteilungsdiagramm

###### Optional: Paketdiagramm

###### Klassendiagramme

###### API
Dokumentieren Sie die API Ihrer Software in geeigneter Art und Weise. Im Fall einer REST API z.B. so: https://gist.github.com/iros/3426278 oder mithilfe einer OpenAPI-Spezifikation.

Zur Dokumentation einer SOAP-Schnittstelle gibt es zahlreiche Tools, um die Dokumentation direkt aus dem WSDL-Dokument zu erzeugen. Z.B.: https://github.com/chenjianjx/wsdl2html

Im Falle einer RMI-Schnittstelle nutzen Sie Javadoc.

Falls Sie MQTT nutzen, dokumentieren Sie ihre MQTT-Topics.

###### Datenbankmodell
Dokumentieren Sie ihr Datenbankmodell. 

#### Dynamisches Modell
Beschreiben Sie den Ablauf Ihres Programms in Form von:

###### Aktivitätsdiagramm
Modellieren Sie den Ablauf ihrer Anwendungsfälle in Form von Aktivitätsdiagrammen.

###### Sequenzdiagramm
Darstellung des Nachrichtenaustausches an Prozessgrenzen

## Getting Started
Dokumentieren Sie, wie man ihr Projekt bauen, installieren und starten kann.

#### Vorraussetzungen
Welche Voraussetzungen werden benötigt, um ihr Projekt zu starten: Frameworks, Software, Libraries, ggf. Hardware.

#### Installation und Deployment
Beschreiben Sie die Installation und das Starten ihrer Software Schritt für Schritt.

## Built With
Geben Sie an, welche Frameworks und Tools Sie verwendet haben. Z.B.:

* [Dropwizard](http://www.dropwizard.io/1.0.2/docs/) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [ROME](https://rometools.github.io/rome/) - Used to generate RSS Feeds


## License

This project is licensed under ...

## Acknowledgments

* Hat tip to anyone whose code was used
* Inspiration
* etc
