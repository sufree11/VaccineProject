# VaccineProject

## Purpose
VaccineProject is a small Java application for managing a vaccine inventory. It models each vaccine batch as a linked-list entry containing a stack of vials, and provides both a console workflow and a JavaFX-based GUI for viewing stock, adding batches, dispensing doses, and removing empty or expired batches.

## Key Features
- Load vaccine batches from the provided `vaccines.txt` data file
- View inventory in a console-based manager
- Add new vaccine batches
- Dispense a vial from a selected batch
- Remove empty or expired batches
- Browse and manage inventory through a JavaFX interface

## Tech Stack
- Java
- Java Collections Framework (`LinkedList`, `Stack`, `Iterator`)
- JavaFX for the GUI
- Plain text file input for seed inventory data

## Project Structure
- `src/PROPOSAL/Vaccines.java` - vaccine batch model
- `src/PROPOSAL/VaccineManager.java` - console inventory manager
- `src/PROPOSAL/VaccineGUI.java` - JavaFX inventory application
- `vaccines.txt` - sample inventory records
- `.vscode/` - launch and JavaFX library settings for VS Code

## Installation
1. Install a compatible JDK and JavaFX SDK.
2. Open the project in your IDE or editor.
3. Ensure the JavaFX libraries referenced in `.vscode/settings.json` and `.vscode/launch.json` point to your local JavaFX installation.
4. Run `PROPOSAL.VaccineGUI` for the GUI or `PROPOSAL.VaccineManager` for the console app.
5. If needed, update the file path used by `VaccineManager` so it points to your local copy of `vaccines.txt`.
