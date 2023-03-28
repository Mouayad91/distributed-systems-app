# PIB-VS_WiSe22_Gruppe05

Verteilter Terminkalender

Dies ist ein Gruppenprojekt, welches im Rahmen der Vorlesung "Verteilte Systeme" stattfindet. Das Ziel dieses Projektes ist es, einen Dienst zur Verwaltung von Terminen zu implementieren.  
Jedem User soll ein Terminkalender zur Verfügung gestellt werden. 
Außerdem sollen User Funktionen, wie hinzufügen, entfernen und ändern von Terminen ausführen können.
Zudem sollen User andere User zu Terminen einladen können, welche dann ablehnen oder annehmen können sollen.

## Architektur

#### Use Cases
![UseCaseVS drawio](https://user-images.githubusercontent.com/64977942/225944929-feacc2fe-b871-4a16-8b7e-4911eddd76f0.png)

#### Anforderungen

Anforderung | Kategorie | Erläuterung | Erfüllung (ja, nein, teilweise)
-------- | -------- | -------- | --------
**Basis-Anforderungen**
cal\_01 | must have  | Das System muss einem User die Möglichkeit bieten, einen Termin zu erstellen/(bearbeiten)/löschen.  | ✓
cal\_02 | must have  | Das System muss einem User die Möglichkeit bieten, einen Nutzer zu einem Termin einzuladen. | ✓
cal\_03 | must have  | Das System muss einem eingeladenen User die Möglichkeit bieten, eine Einladung zu erhalten, welche er annehmen/ablehnen kann. | ✓
cal\_04 | must have  | Das System muss Ereignisse in Form von Logs dokumentieren. | ✓
cal\_05 | must have  | Das System erlaubt nur dem Terminersteller, einen Termin zu ändern. | ✓
 ||
**User-Anforderungen**
usr\_01 | must have | Das System muss einem Admin die Möglichkeit bieten, neue Accounts anzulegen.  | ✓
usr\_02 | must have | Das System muss einem Admin die Möglichkeit bieten, Accounts zu löschen. | ✓
usr\_03 | must have | Das System muss einem Admin die Möglichkeit bieten, die Passwörter eines Users zu ändern. | ✓
usr\_04 | must have | Das System muss einem User die Möglichkeit bieten, sich einzuloggen. | ✓
usr\_05 | must have | Das System muss einem User die Möglichkeit bieten, sich ausloggen. | ✓
 ||
**Non-Funktionale Anforderungen**
nonFunc\_01 | must have |  Daten müssen permanent in einer Datenbank gespeichert sein.  | ✓
nonFunc\_02 | must have | User und Admin müssen über ein graphisches User Interface verfügen. | ✓
nonFunc\_03 | must have | Das System muss Termine auch zustellen, wenn der Empfänger zur Zeit des Versandes der Einladung nicht online ist. | ✓
nonFunc\_04 | must have | Das System muss die Nutzung mehrerer User gleichzeitig erlauben.   | ✓
nonFunc\_05 | must have | Das System muss Anfragen in eigenen Threads abwickeln. | ✓
||

#### [Anforderungen Projektarbeit VS (Moodle)](https://moodle.htwsaar.de/pluginfile.php/190250/mod_resource/content/0/Anforderungen%20Projektarbeit%20VS.pdf):

Anforderung | Erläuterung | Erfüllung (ja, nein, teilweise)
-------- | -------- | --------
**MUST Have Anforderungen**
M1   | Es muss ein Service zur Verwaltung von Terminkalendern implementiert werden.Der Service verwaltet für jeden Nutzer einen eigenen Terminkalender.  | ✓
M2   | Ein Nutzer muss in der Lage sein über einen Nutzer-Client auf den Kalender-Service zuzugreifen und Termine hinzuzufügen, zu entfernen und zu ändern. | ✓
M3 | Nutzer müssen die Möglichkeit haben andere Nutzer zu einem Termin hinzuzufügen. Nutzer müssen über eine eindeutige ID (oder einen eindeutigen Nutzernamen) adressierbar sein.  | ✓
M4   | Über einen Admin-Client müssen Administratoren das System konfigurieren und administrieren können. | ~
M5   | Die Client-Anwendung und der Admin-Client müssen über ein Command Line Interface verfügen | ✖
||
**SHOULD Have Anforderungen**
S1   |  Anfragen an den Kalender-Service müssen parallel in eigenen Prozessen oder Threads behandelt werden.  | ✓ 
S2   | Der Zustand des Kalender-Service muss persistent in einer Datenbank gespeichert und nach einem Serverabsturz wiederhergestellt werden. | ✖
S3 | Über den Admin-Client muss es möglich sein den Status des Kalender-Service zu überwachen und Statistiken abzurufen. | ~
S4   | Der Online-Status von Nutzern muss angezeigt werden.   | ✓ 
S5   | Nutzer müssen aktiv über neue Termineinladungen informiert werden. | ✓ 
||
**COULD Have Anforderungen**
C1 | Der Registry-Service muss horizontal skalierbar sein.  | ✖
C2 | Nachrichtenverluste müssen erkannt und automatisch behandelt werden. | ~
C3 | Es müssen Logdateien geschrieben werden, die es dem Administrator ermöglichen alle Zugriffe auf den Kalender-Service nachzuvollziehen.| ✓
C3 | Über einen Heartbeat-Mechanismus muss periodisch geprüft werden, ob alle angebundenen Nutzer-Clients noch verfügbar sind. | ✖
C5.| Über eine Suche muss es möglich sein Nutzer anhand ihres Namens und Vornamens zu finden. | ~ 
C6 | Der Nutzer-Client und der Admin-Client müssen über ein Graphisches User Interface verfügen. | ✓ 
|| 
**++ Anforderungen**
++1   | Client und Server müssen asynchron kommunizieren.  | ✓ 
++2   |  Es muss eine Three-Tier-Architektur umgesetzt werden, bei der die Datenbank auf einen eigenen Rechner ausgelagert wird  | ✓ 
++3 | Der Kalender-Service muss aus mehreren Instanzen, die in einer hierarchischen oder dezentralen Architektur miteinander verbunden sind, bestehen. Es darf keine zentrale Verwaltungsinstanz geben. Jede Service-Instanz verwaltet eine Teilmenge der Kalender. | ✖

### **Lösungsstrategie**

#### **Kernidee**
- Clients kommunizieren mit zentralem Server (Client-Server-Architektur)
- User kann sich anmelden und den Service nutzen
- Server verwaltet Kalender, eines jeden Users
- Server führt Validierung und Anwendungslogik aus (Thin-Clients)
- Synchrones Request-Reply Pattern (Client sendet Requests und wartet auf Antwort)


#### **Technologien**
- maven build tool (dependency-Verwaltung)
- Java Client
- JavaFX Frontend
- Java Server (Spring Boot)
- Middleware: RESTful API
- MySQL-Database

#### **Designentscheidungen**


Transparancy-Aspekt | Welche Aspekte der Transparancy sind umgesetzt? Wie?
-----| -----
access | Datenzugriffe finden über die Rest-Endpoints mit DTOs - Client keine Info über Model-Klassen und Speicherart
location | Örtlichkeit der gespeicherten Ressource Hostname gekapselt
migration | Garantiert durch Rest-Api (Zustandslos) 
relocation |  Garantiert durch Rest-Api (Zustandslos)
concurrency | Spring Data JPA kümmert sich um Nebenläufigkeit (Abarbeiten in eigenen Threads
failure | Datenzugriffe werde duchr Spring Data JPA mittels Transaktion ausgeführt, bei Fehlern wird der Client benachrichtigt und kann reagieren

#### Softwarearchitektur
Das Projekt wurde mittels Layered-Architecture umgesetzt.

![software](https://user-images.githubusercontent.com/64977942/225944182-5712a784-f2d5-4686-9cc6-834226ccd8fa.png)

Die Software wurde in die drei Schichten: Controller/Crud, Service und Repository eingeteilt. Jede Schicht ist für eine bestimmte Aufgabe verantwortlich.

![layeredArch](https://user-images.githubusercontent.com/64977942/226103753-d419357e-a22c-47e5-a0c3-526a6f84111e.svg)

#### Systemarchitektur
Das System wurde mittels Three-Tier-Architecture, wobei die komplette Application-Logic vom Server übernommen wird. Diese Architektur ermöglicht es Thin-Clients zu verwenden.

![System](https://user-images.githubusercontent.com/64977942/225944230-ceeedee2-dd7f-4011-a213-4f5d988c881b.png)

- Thin-Client
    - Logik auf Application Server ausgelagert, deswegen werde nur Anzeigeprozeduren durch den Client ausgeführt
- Stateless Server-Desgin
    - Rest-API -> Umsetzung Zustandsloser Kommunikation, Server hat keine Sessioninformation pro Client
- Fehlersemantik
    - Durch Rückgabe eines API-Error Objekt im Response Body + Passenenden Response Code
- Idempotenz
    - GET, PUT, DELETE sind idempotent (Lesen/ändern/löschen immer die angegebene Ressource)
    - POST ist nicht idempotent (Kein Ansprechenen einer bestimmten Ressource)
- Skalierbarkeit
    - Skallierbarkeit durch das Stateless Server-Desgin möglich, jedoch durch das Nutzen **einer** Datenbank (ohne Loadbalancer) kaum möglich. Des Weiteren sollte beim Entwickeln Zuständigkeiten in Form von Microservices (mit eigenen Aufgaben) gekapselt werden, um eine bessere Skalierbarkeit zu ermöglich.


#### Statisches Modell

###### Komponentendiagramm
![KD](https://user-images.githubusercontent.com/64977942/225904413-6873497d-c423-40da-a0ca-c55b944f04e2.JPG)

###### Verteilungsdiagramm
![Verteilungsdiagramm](https://user-images.githubusercontent.com/64977942/225903946-1f3e2fc0-b383-4232-8ad3-0077f03543c3.png)

###### Klassendiagramme
**Gesamtes Klassendiagramm (Server)** 
![diagram](https://user-images.githubusercontent.com/64977942/226102349-c40010a1-90de-4890-ade6-eca1dd183a67.svg)

**Model-Klassen Klassendiagramm (Server)** 
![modelClass](https://user-images.githubusercontent.com/64977942/226103729-9bfcd2ee-7a17-4ac5-b690-4896c3e4cf00.svg)

###### API
Dokumentieren Sie die API Ihrer Software in geeigneter Art und Weise. Im Fall einer REST API z.B. so: https://gist.github.com/iros/3426278 oder mithilfe einer OpenAPI-Spezifikation.

Die Dokumentation der REST-API wurde mithilfe der OpenAPI-Spezifikation durch [Swagger](https://swagger.io/) dokumentiert. Dokumentation ist beim Ausführen der Server-Anwendung unter:

```
http://localhost:8080/docs/swagger-ui/index.html
```

###### Datenbankmodell
![vsdb](https://user-images.githubusercontent.com/64977942/225902255-d8aed658-1f8a-463e-b9c9-ce74e7b860e3.png)

#### Dynamisches Modell

###### Aktivitätsdiagramm
![aktivitäts](https://user-images.githubusercontent.com/64977942/225903709-4e29bc92-9e27-4ef2-870b-2c7d425f89c0.jpeg)

###### Sequenzdiagramm
![login](https://user-images.githubusercontent.com/64977942/225903696-4c8ec90d-c719-4ba9-b797-150f30153c8b.jpg)

## Getting Started

#### Vorraussetzungen
* Maven: 3.8.1 (or higher)

***Java Version***
- Client: OpenJDK19
- Server: OpenJDK19

#### Installation und Deployment

**Github Projekt klonen**

Git Repository clonen:
```
git clone https://github.com/htw-saar/PIB-VS_WiSe22_Gruppe05.git
```

### Server

Navigieren Sie in den order ***/server*** und installieren Sie alle Dependencies:

```
cd /server
mvn install
```
Nach der Installation lässt sich das Spring-Projekt mit folgendem Befehl starten:
```
mvn spring-boot:run
```

### Server

Navigieren Sie in den order ***/client*** und installieren Sie alle Dependencies:

```
cd /client
mvn install
```
Nach der Installation lässt sich das Spring-Projekt analog zum Server mit folgendem Befehl starten:
```
mvn spring-boot:run
```



## Built With

* Dependency Management with [Maven](https://maven.apache.org/)
* Powered with [Spring Boot 🍃](https://spring.io/)
* Using the [MySQL Database](https://www.mysql.com/)
* Authentication via [JWT](https://jwt.io/)
* Client UI-Framework [JavaFX](https://openjfx.io/)


## License

This project is licensed under GNU General Public License v3.0

## Acknowledgments

* Bruno Leite [Spring-Boot-Exception-Handling](https://github.com/brunocleite/spring-boot-exception-handling)
- [Spring REST Docs](https://spring.io/projects/spring-restdocs)
