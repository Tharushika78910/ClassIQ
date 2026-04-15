# ClassIQ – Teacher Grade Book and Report Card System

## Project Overview

ClassIQ is a Java-based application developed to help teachers manage student academic records efficiently.
The system allows teachers to view student information, record marks, generate report cards, and provide feedback based on student performance in an organized way.

Additionally, students can view their grades, understand how they are calculated, and download their report cards.

The system supports **multilingual functionality**, allowing users to interact with the application in multiple languages, making it suitable for diverse educational environments.

This project demonstrates concepts in software development, internationalization (i18n), software quality assurance, and DevOps practices.

---

## Features

* Manage student information
* Enter student marks
* Display grading criteria
* View student grades
* Generate report cards
* Teacher and student dashboards
* Provide performance feedback
* Multilingual UI support
* Dynamic language switching
* RTL (Right-to-Left) layout support
* Localized database content support

---

## Technologies Used

* JavaFX – User Interface
* IntelliJ IDEA – IDE
* Maven – Build management
* MariaDB / HeidiSQL – Database
* JUnit – Testing
* JaCoCo – Code coverage
* SonarQube – Code quality analysis
* Jenkins – Continuous Integration
* Docker – Containerization
* VcXsrv / Xming – GUI support for Docker
* GitHub – Version control
* i18n Framework – Localization support

---

## Localization Support

### Supported Languages

* English (en)
* Sinhala (si)
* Arabic (ar)

### Features

* Language selector in UI
* Dynamic language switching (no restart required)
* RTL layout support for Arabic
* Locale-based formatting (dates, numbers)
* Externalized UI text using translation files

### How to Change Language

1. Open the application
2. Select a language from the dropdown
3. UI updates instantly

---

## Database Localization

The system implements database-level localization using the **Translation Table Approach**.

### Approach

* Core data is stored in main tables
* Translations are stored in separate tables
* Language-specific queries retrieve appropriate data

### Main Tables

* student
* teacher
* gradecategory
* app_user

### Translation Tables

* student_translation
* teacher_translation
* gradecategory_translation
* subject_translation
* role_translation

Each translation table includes:

* Reference ID
* Language code (en, si, ar)
* Translated values

### Example

```sql id="wzui9w"
CREATE TABLE student_translation (
    student_id INT NOT NULL,
    language_code VARCHAR(5) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    PRIMARY KEY (student_id, language_code),
    FOREIGN KEY (student_id) REFERENCES student(student_id)
);
```

### Query Example

```sql id="k3z4r2"
COALESCE(tr_req.first_name, tr_en.first_name, s.first_name)
```

---

## How to Run the Project

### Prerequisites

* Java JDK 17+
* Maven
* MariaDB / MySQL
* Docker (optional)

### Steps

1. Clone the repository

   ```
   git clone <https://github.com/Tharushika78910/ClassIQ.git>
   ```
2. Setup the database using provided SQL scripts
3. Configure database connection
4. Build and run the project

   ```
   mvn clean install
   mvn javafx:run
   ```
5. Launch the application

---

## Testing

* Verified multilingual data storage in the database
* Tested language-based data retrieval
* Verified UI updates dynamically
* Validated report generation in different languages

---

## Code Quality

* SonarQube used for static code analysis
* JaCoCo used for code coverage
* Issues and vulnerabilities resolved
* Quality Gate: Passed

---

## Documentation

* Sprint Plans
* Sprint Review Reports
* UML Diagrams (Sequence Diagram)
* Localization Excel Sheet

---

## Conclusion

ClassIQ demonstrates a complete multilingual academic management system with both UI and database localization.
The project follows Agile practices, integrates modern development tools, and ensures high-quality, scalable software design.
