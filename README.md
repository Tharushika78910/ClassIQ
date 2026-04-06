# ClassIQ – Teacher Grade Book and Report Card System

## Project Overview

ClassIQ is a Java-based application developed to help teachers manage student academic records efficiently.  
The system allows teachers to view student information, record marks, generate report cards, and provide feedback based on student performance in an organized way.

Additionally, students can view their grades, understand how they are calculated, and download their report cards.

The system has been extended with localization support, allowing users to interact with the application in multiple languages, making it suitable for diverse educational environments.

The goal of this project is to demonstrate software development concepts, modern development tools, and internationalization, software quality assurance, and DevOps practices.

---

## Features

* Manage student information
* Enter students marks
* Display grading criteria
* View student grades
* Generate report cards
* Display teacher/student dashboard interface
* Provide feedback on performance
* Multilingual UI support (Localization)
* Dynamic language switching
* RTL (Right-to-Left) layout support
* Localized database content support

---

## Technologies Used

* **JavaFX** for the user interface
* **IntelliJ IDEA** Integrated Development Environment
* **Maven** for project build management
* **MariaDB** HeidiSQL for Database
* **JUnit** for testing
* **JaCoCo** Code coverage reporting
* **Jenkins** for Continuous Integration
* **Docker** for containerization
* **VcXsrv / Xming** – X server for displaying JavaFX GUI from Docker
* **GitHub** for version control
* **i18n / Localization Framework** – UI translation support
* **Static Analysis Tools** – Code quality

---

## Localization Support

- The system now supports multiple languages, including non-Latin languages such as Sinhala and Arabic.

### Supported Languages

- English (en)
- Sinhala (si)
- Arabic (ar)

### Features

- Language selector available in UI
- Dynamic language switching (no restart required)
- RTL layout support for Arabic
- Locale-based formatting (dates, numbers)
- Externalized UI text using translation files

### How to change the language

1. Open the application.
2. Select language from the language dropdown.
3. UI updates instantly.

---

## Database Localization

To support multilingual data, the ClassIQ system implements database-level localization using the translation table approach.

### Approach Used

The project uses the **Translation Table Approach**, which separates core data from translated content.  
This makes the database more scalable, cleaner, and easier to maintain when adding new languages.

### Main Tables

The main tables store core system data:

- `student`
- `teacher`
- `gradecategory`
- `app_user`

These tables keep the original data used by the system.

### Translation Tables

Additional translation tables were created to store multilingual values:

- `student_translation`
- `teacher_translation`
- `gradecategory_translation`
- `subject_translation`
- `role_translation`

Each translation table stores:

- the related reference ID
- `language_code` (`en`, `si`, `ar`)
- translated text values

### Example

Example of the translation table structure:

```sql
CREATE TABLE student_translation (
    student_id INT NOT NULL,
    language_code VARCHAR(5) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    PRIMARY KEY (student_id, language_code),
    FOREIGN KEY (student_id) REFERENCES student(student_id)
);