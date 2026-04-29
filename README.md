  # ClassIQ – Teacher Grade Book and Report Card System
  

## 1. Project Title & Overview

ClassIQ is a Java-based application developed to help teachers manage student academic records efficiently.
The system allows teachers to view student information, record marks, generate report cards, and provide feedback based on student performance in an organized way.

Additionally, students can view their grades, understand how they are calculated, and download their report cards.

The system supports **multilingual functionality**, allowing users to interact with the application in multiple languages, making it suitable for diverse educational environments.

This project demonstrates concepts in software development, internationalization (i18n), software quality assurance, and DevOps practices.

---
### Problem Summary

In some educational systems, some teachers enter students’ performance or marks manually   
by writing on paper or computers. And they are calculating average and grades are also
manually. That method is not hundred percent accurate and not very efficient, it will make
teachers tired and time wasting much and, on the other hand, it is difficult with large numbers of
students.

---
###  Intended Audience/Users

- Teachers – manage student data, enter marks, calculate grades, generate reports
- Students – view grades and feedback, download report cards

---

### Technologies Used

#### Development

* JavaFX – User Interface
* IntelliJ IDEA – IDE
* Maven – Build management

#### Database

* MariaDB / HeidiSQL – Database

#### Testing & Quality

* JUnit – Testing
* JaCoCo – Code coverage
* SonarQube – Code quality analysis

#### DevOps
* Jenkins – Continuous Integration
* Docker – Containerization

#### Other Tools
* VcXsrv / Xming – GUI support for Docker
* GitHub – Version control
* i18n Framework – Localization support

---

### Overall duration

- 8 Sprints (2 weeks each) = 16 weeks total
---

## 2. Product Vision

### Vision Statement

Our vision is to create a simple, reliable, and efficient grad ebook system that supports teachers in managing student performance. The goal is to make grading easier, faster, and more accurate while demonstrating how a real-world educational application can be built using Java and modern software engineering practices.

Additionally, the system aims to be inclusive and accessible by supporting localization, allowing the application to be used in multiple languages such as Sinhala and Arabic. This ensures that teachers from diverse linguistic backgrounds can interact with the system comfortably and effectively

---
### Main Goals

- Deliver a fully functional grade book system within the 8-week project timeline
- Reduce manual grading workload for teachers through automation
- Maintain grading accuracy, consistency, and standardization
- Apply modern Java software development practices learned during the course
- Support localization by enabling the system to operate in multiple languages 
- Improve accessibility and usability for teachers from diverse linguistic backgrounds

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
### Definition of Success

The project will be successful if the grade book system is completed on time, meets requirements, supports multiple languages, provides accurate grading, and offers a reliable, user-friendly solution for teachers.

---
## 3. Project Plan & Sprint Structure

The project was organized using an Agile (Scrum) development methodology to allow for iterative progress and continuous improvement. The total development period of 8 weeks was divided into 4 sprints, each lasting 2 weeks. Each sprint focused on specific features such as core grade book functionality, user interface development, grading logic, and localization support including Sinhala and Arabic. Regular sprint planning, development, and review phases were followed to track progress, identify issues early, and ensure the system met requirements effectively.

### Sprint-Based Documentation Sections

- Sprint  1:  - 📎 [Project Planning Report](Documents/Project%20Planning%20Report.pdf)

- Sprint 1: - 📎 [Project Vision Report](Documents/Project%20Vision%20Report.pdf)
  
- Sprint 2: - 📎 [View Database Schema](Documents/db/schema.sql)

- Sprint 3: -📎 [View ClassIQ UI Report](Documents/Images/ClassIQ_UI.pdf)

- Sprint 4:  - Docker Containerization

- Sprint 5: - UI Localization

- Sprint 6: - Database Localization

- Sprint 7: - Quality Assurance

- Sprint 8:- Documentation & Finalization

---

## 4. Sprint 1 – Project Planning & Vision

•	Created project plan and timeline

•	Developed product backlog

•	Defined project scope and identified risks

•	Validated project vision

•	Defined acceptance criteria for core system features

- Sprint 1: [View Reports](Documents/Sprint_reports/Sprint_1)


## 5. Sprint 2 – Requirements & Database

•	Identified functional requirements (login, grading, report generation)

•	Created Use Case Diagram

•	Designed ER Diagram

•	Selected MariaDB database

•	Implemented database schema and relationships

•	Planned unit testing strategy using JUnit

- Sprint 2:  [View Reports](Documents/Sprint_reports/Sprint_2)


## 6. Sprint 3 – UI Implementation & CI

•	Developed UI using JavaFX

•	Implemented login page, teacher dashboard, student dashboard, and grading interface

•	Followed MVC design approach

•	Integrated Maven build system

CI/CD Pipeline (Jenkins)

•	Build

•	Test

•	Code Coverage (JaCoCo)

•	SonarQube Analysis

- Sprint 3:  [View Reports](Documents/Sprint_reports/Sprint_3)
- 
### CI/CD Pipeline (Jenkins)

Jenkins is used to automate the build and deployment process.

* Pipeline Stages
* Clean Workspace
* Checkout Code
* Build, Test and Coverage
* Publish Test Results
* Publish Coverage Report
* SonarQube Analysis
* Build Docker Image
* Push Docker Image to Docker Hub
* Run Docker Container

## 7. Sprint 4 – Docker Containerization

### Docker Setup

#### Docker Hub Repository - poornimj/classiq

#### Build Docker Image - docker build -t poornimj/classiq

#### Run Docker Container (PowerShell)

docker rm -f classiq-container

docker run -d --name classiq-container -p 8080:8080 `
-v C:\classiq-reports:/app/reports `
-e DB_HOST=host.docker.internal `
-e DB_PORT=3306 `
-e DB_NAME=classiq `
-e DB_USER=root `
-e DB_PASS=root123 `
poornimj/classiq:latest


## 8. Sprint 5 – UI Localization & Kubernetes

### Localization Support
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

## 9. Sprint 6 – Database Localization

### Database Localization

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

## 10. Sprint 7 – Quality Assurance

### Code Quality (SonarQube)

* SonarQube used for static code analysis
* JaCoCo used for code coverage
* Issues and vulnerabilities resolved
* Quality Gate: Passed
* Bugs and vulnerabilities identified and resolved
* Code smells reduced
* Quality Gate Status: PASSED
* Dashboard: http://localhost:9000/dashboard?id=ClassIQ


## 11. Sprint 8 – Documentation & Finalization

---

## 12. How to Run the Project

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
2. Set up the database using provided SQL scripts
3. Configure database connection
4. Build and run the project

   ```
   mvn clean install
   mvn javafx:run
   ```
5. Launch the application

---

### GUI Support (Xming / VcXsrv)

Since this is a JavaFX application, GUI support is required when running in Docker.

### Steps

 * Install VcXsrv or Xming
 * Start the server before running Docker
 * Allow firewall access

---

### Optional:

DISPLAY=host.docker.internal:0.0

---

## 13. Testing Instructions

Testing can be executed using Maven from the project root directory. The project includes test files under `src/test/java`, separated into backend and frontend test packages. Backend tests are organized into `controller`, `model`, and `service` folders, while frontend tests include areas such as `student`, `teacher`, and `LoginPageViewTest`.

### Run Unit Tests

To run all unit tests, use the following command:

```bash
mvn test
```

### Test Coverage Access

### Testing & Coverage

* Verified multilingual data storage in the database
* Tested language-based data retrieval
* Verified UI updates dynamically
* Validated report generation in different languages
* Unit testing done using JUnit
* JaCoCo used for code coverage

Test results can be viewed in the terminal after running mvn test. If a coverage tool such as JaCoCo is configured in the pom.xml, the coverage report can be generated using:
mvn test jacoco:report

The generated coverage report can be accessed at:
target/site/jacoco/index.html

### Performance Testing

Performance testing was not implemented as a separate testing phase in this project. However, basic performance was observed during manual testing by checking application response time, login behavior, grade calculation speed, and navigation between teacher and student interfaces.

---
## 14. Repository Structure

### Main Directories

- **/backend**  
  Represents the backend logic located in `src/main/java/backend`. It handles business logic, data processing, and core grade book functionalities.

- **/frontend**  
  Represents the frontend components located in `src/main/java/frontend`. It is responsible for the user interface and user interactions.

- **/resources**  
  Located in `src/main/resources`, this directory contains configuration files and localization resources, including support for Sinhala and Arabic languages.

- **/tests**  
  Located in `src/test/java`, this directory includes test cases for both backend and frontend components to ensure system reliability and correctness.

- **/docs**  
  Represented by the `Documents` folder, it contains project documentation such as reports and related materials.

- **/diagrams**  
  Represented by the `Diagrams` folder, it includes system architecture diagrams, UML diagrams, and design illustrations.

- **/docker**  
  Represented by the `Dockerfile`, used to containerize the application for deployment.

- **/ci-cd**  
  Represented by the `Jenkinsfile`, which defines the CI/CD pipeline for automated build, testing, and deployment.

- **/build**  
  Represented by the `target` directory, which stores compiled code and build outputs generated by Maven.

---
## 15. Authors

Include:

Team members: 
- Poornima Jayamanna
- Hathadura Chathurika
- Dilmi Merenchi Kankanamge
- Roshini Farnando
- Kumudu Nallaperuma         (names and roles)

Course name and semester:
- Software Engineering Project TX00EY30-3011 - Semester 1 & 2, 2026
