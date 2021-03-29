# Routes

|Done?|Method|URL|Auth?|Content?|
|---|---|---|---|---|
|yes|POST|/users|no|yes|
|yes|POST|/sessions|no|yes|
|yes|GET|/users/{username}|yes|no|
|yes|PUT|/users/{username}|yes|yes|
|yes|GET|/stats|yes|no|
| |GET|/scoreboard|yes|no|
|yes|GET|/history|yes|no|
| |GET|/tournament|yes|no|
|yes|POST|/history|yes|yes|

## Registrierung

Neuter wird in der DB erstellt. Username ist unique.

POST /users

Content: `{"Username":"kienboec", "Password":"daniel"}`

Returns: OK

## Login

Prüft die Logindetails und sucht den Token des Nutzers aus der DB.

POST /sessions

Content: `{"Username":"kienboec", "Password":"daniel"}`

Returns: Token of user on success

## Get User Details

Sucht den Nutzer in der Datenbank und liest alle Infos aus, falls der eingeloggte User.username == {userneme}.

GET /users/{username}

Token-Authorization erforderlich

Content: --

Returns: Informationen des Nutzers {username}

## Edit Account

Ersetzen die übergebenen Felder, falls der eingelogte User.username == {username}

PUT /users/{username}

Token-Authorization erforderlich

Content: `{"Name": "Kienboeck",  "Bio": "me playin...", "Image": ":-)"}`

## Statistiken

GET /stats

Token-Authorization erforderlich

Return: statistische Informationen des Nutzers (Elo, PushUp Anzahl Geramt)

## Scoreboard

GET /scoreboard

Token-Authorization erforderlich

Return: List<Statistiken> von allen Nutzern (Elo, PushUp Anzahl) sortiert nach Elo

## History

Liste der einzelnen Trainingseinheiten mit Anzahl der PushUps und Dauer.

GET /history

Token-Authorization erforderlich

Return: List<Trainings> von dem eingeloggten Nutzer

## Tournament Infos

Gibt zurück, ob gerade ein Turnier stattfindet. Falls ja gibt es auch die entsprechenden Infos zurück (Eventuell die
Anzahl der aktiven Teilnehmer).

z.B.:
(tournament started; 2 participants; altenhof in front; write start-time)
oder "no tournament"

GET /tournament

Token-Authorization erforderlich

Return:List<tournament> von dem eingeloggten Nutzer

## Trainingseinheit hinzufügen

Fügt einen Eintrag in die Liste der vergangenen Trainingseinheiten (=Histroy) hinzu. Dies startet automatisch ein
Turnier (für die nächsten 2min), falls derzeit keins läuft.

POST /history

Token-Authorization erforderlich

Content: `{"Name": "PushUps",  "Count": 40, "DurationInSeconds": 60}`

Return: OK

##  