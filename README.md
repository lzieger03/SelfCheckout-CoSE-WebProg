# SelfCheckOut-CoSe-WebProg

## Aufbau 

1. Root-Verzeichnis:
    - README.md: Projektbeschreibung, Installationsanweisungen und Usage-Informationen.
    - package.json oder pom.xml/build.gradle: Abhängigkeiten und Skripte (je nach Frontend- und Backend-Technologien wie Node.js, Maven oder Gradle).
    - .gitignore: Dateien, die nicht ins Repository geladen werden sollen.
    - Dockerfile und docker-compose.yml (falls Docker genutzt wird).
    - tsconfig.json oder babel.config.json (je nach verwendeter Sprache und Transpiler).

2. Frontend-Verzeichnis (z.B. client):
    - src/: Enthält den gesamten Quellcode des Frontends.
    - components/: Wiederverwendbare UI-Komponenten.
    - pages/: Seitenkomponenten, falls ein Framework wie React/Next.js verwendet wird.
    - assets/: Statische Ressourcen wie Bilder, Fonts oder Stylesheets.
    - services/: API-Aufrufe oder andere Services.
    - App.js oder index.js: Hauptkomponente, die das gesamte Frontend rendert.
    - public/: Statische Dateien wie das HTML-Template oder Favicons.
    - tests/: Tests für Frontend-Komponenten (z.B. Jest, Mocha).

3. Backend-Verzeichnis (z.B. server):
    - src/: Quellcode des Backends.
    - controllers/: Logik zur Verarbeitung von Anfragen.
    - models/: Datenbankmodelle.
    - routes/: Definiert API-Routen.
    - services/: Interaktion mit externen Services oder Datenbanken.
    - middlewares/: Middlewares für Authentifizierung, Validierung etc.
    - config/: Konfigurationen (z.B. für Datenbank, API-Keys).
    - app.js oder server.js: Startpunkt der Anwendung.
    - tests/: Tests für das Backend (z.B. Unit-Tests für Controller oder Services).

4. Gemeinsames Verzeichnis (falls nötig):
    - shared/: Gemeinsamer Code, der sowohl im Frontend als auch im Backend verwendet werden kann, z.B. Typdefinitionen, Validierungslogik oder Utility-Funktionen.

5. DevOps/CI-CD-Verzeichnis:
    - scripts/: Build-, Deploy- oder Hilfsskripte.
    - .github/workflows/: CI/CD-Konfigurationsdateien für GitHub Actions oder andere CI/CD-Tools.

6. Konfigurationsdateien:
    - .env: Umgebungsvariablen (nicht ins Repository hochladen).
    - jest.config.js oder karma.config.js: Test-Konfigurationen.
    - eslint.json oder .prettierrc: Linter- und Formatter-Einstellungen.