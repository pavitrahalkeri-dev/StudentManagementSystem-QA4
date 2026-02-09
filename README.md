# Student Management System (QA-4)

## Description
This is a JavaFX-based Student Management System developed as part of
Qualitative Assessment 4 (QA-4). The application uses SQLite database
with JDBC to perform CRUD operations.

## Features
- Add Student
- View Students
- Update Student Department
- Delete Student

## Technologies Used
- Java
- JavaFX
- SQLite
- JDBC

## Database Details
- Database Name: students.db
- Tables:
  - students
  - courses
  - departments

## How to Run the Project

### Compile
javac -cp ".:./lib/*" --module-path ./javafx-sdk-25.0.2/lib --add-modules javafx.controls,javafx.fxml application/*.java

### Run
java -cp ".:./lib/*" --module-path ./javafx-sdk-25.0.2/lib --add-modules javafx.controls,javafx.fxml application.Main
