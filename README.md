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

- Sprint  1: 
- [Project Planning Report](Documents/Project%20Planning%20Report.pdf)

- [Project Vision Report](Documents/Project%20Vision%20Report.pdf)
  
- Sprint 2:  
- [View Database Schema](Documents/db/schema.sql)

- Sprint 3: -
- [View ClassIQ UI Report](Documents/Images/ClassIQ_UI.pdf)

- Sprint 4:  
- [View Docker Image](Documents/Images/Jenkins%20Stage%20View%20Sprint%204.png)
- [Code Coverage](Documents/Images/code%20coverage.png)

- Sprint 5: 
- [Download Excel](Documents/Localization_Reports/Localization_String_List.xlsx)

- Sprint 6:
- [Database Localization Report](Documents/Localization_Reports/Database_Localization_Report.pdf)
- [View Statistical Code Review Report](Documents/Statistical_Code_Review_Report_Sprint_6.pdf)

- Sprint 7: - Quality Assurance
- [Open Folder - Result_after_SonarQube_with_Jenkins_and_Docker](Documents/Result_after_SonarQube_with_Jenkins_and_Docker)
- [Open Folder - Acceptance Criteria](Documents/Acceptance%20Criteria)
- [Open Folder-Heuristic_Evaluation_Reports](Documents/Heuristic_Evaluation_Reports)
- [View Bug Issue Report](Documents/ClassIQ_Bug_Issue_Report.pdf)

- Sprint 8:- Documentation & Finalization
- [Open Diagrams](Diagrams)
- [Open Docs](Documents)

---


## 4. Sprint 1 – Project Planning & Vision

•	Created project plan and timeline

•	Developed product backlog

•	Defined project scope and identified risks

•	Validated project vision

•	Defined acceptance criteria for core system features

- Sprint 1: [View Reports](Documents/Sprint_reports/Sprint_1)

---


## 5. Sprint 2 – Requirements & Database

•	Identified functional requirements (login, grading, report generation)

•	Created Use Case Diagram

•	Designed ER Diagram

•	Selected MariaDB database

•	Implemented database schema and relationships

•	Planned unit testing strategy using JUnit

- Sprint 2:  [View Reports](Documents/Sprint_reports/Sprint_2)

---


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

---


## 7. Sprint 4 – Docker Containerization

•	Containerized the application using Docker

•	Created Dockerfile

•	Built Docker image

•	Tested containerized application

#### Purpose of Docker

•	Ensure consistent development environment

•	Simplify deployment

•	Improve testing reliability

- Sprint 4:  [View Reports](Documents/Sprint_reports/Sprint_4)

---


## 8. Sprint 5 – UI Localization & Kubernetes

Localization Support

•	Supported languages: English, Sinhala, Arabic

•	Used resource bundles for translations

•	Enabled dynamic language switching

•	Implemented RTL support for Arabic

Note: Kubernetes was not implemented in this project.

#### How to Change Language

1. Open the application
2. Select a language from the dropdown
3. UI updates instantly

- Sprint 5:  [View Reports](Documents/Sprint_reports/Sprint_5)

---


## 9. Sprint 6 – Database Localization

Sprint 6 focused on implementing multilingual support at the database level.

#### Approach

•	Used translation table approach

•	Stored core data in main tables

•	Stored translations in separate tables

•	Used COALESCE queries for language fallback

#### Schema Changes

•	Added translation tables such as:

o	student_translation

o	teacher_translation

o	subject_translation

#### Validation

•	Tested multilingual data retrieval

•	Verified correct display of language-specific content

•	Ensured fallback to default language when translations are missing

- Sprint 6:  [View Reports](Documents/Sprint_reports/Sprint_6)

---


## 10. Sprint 7 – Quality Assurance

Sprint 7 focused on ensuring system quality through testing, evaluation, and code analysis.

#### Activities
•	Conducted SonarQube code analysis

•	Performed functional testing (login, grading, report generation)

•	Conducted usability evaluation using heuristic evaluation principles

•	Identified usability issues and improvements

•	Documented bugs and tracked issues

•	Generated code coverage reports using JaCoCo

#### Testing Types
•	Functional Testing – verified system features

•	Non-Functional Testing – usability and performance evaluation

#### Artifacts
•	Heuristic Evaluation Report

•	Bug Reports

•	Acceptance Criteria validation

#### Tools
•	SonarQube

•	JUnit

•	JaCoCo

#### Results
•	Quality Gate: PASSED

•	Bugs identified and fixed

•	Code smells reduced

•	Improved usability

#### Performance Testing
JMeter was not implemented. Basic performance testing was conducted manually.

- Sprint 7:  [View Reports](Documents/Sprint_reports/Sprint_7)

---


## 11. Sprint 8 – Documentation & Finalization
•	Completed technical documentation

•	Prepared user documentation

•	Organized repository structure

•	Finalized sprint reports and diagrams

- Sprint 8:  [View Reports](Documents/Sprint_reports/Sprint_8)

---


## 12. How to Run the Project

#### Prerequisites

* Java JDK 17+
* Maven
* MariaDB / MySQL
* Docker (optional)

#### Run Locally

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
#### Run with Docker 
Hub Repository -  poornimj/classiq

Build Docker Image  - docker build -t poornimj/classiq .

Run Docker Container (PowerShell)  -   docker rm -f classiq-container

docker run -d --name classiq-container -p 8080:8080 `
-v C:\classiq-reports:/app/reports `

-e DB_HOST=host.docker.internal `

-e DB_PORT=3306 `

-e DB_NAME=classiq `

-e DB_USER=root `

-e DB_PASS=root123 `

poornimj/classiq:latest

Access Application   - http://localhost:8080

---

#### GUI Support (Xming / VcXsrv)
•	Install VcXsrv or Xming

•	Start before running Docker

•	Allow firewall access

---


## 13. Testing Instructions

Testing can be executed using Maven from the project root directory. The project includes test files under `src/test/java`, separated into backend and frontend test packages. Backend tests are organized into `controller`, `model`, and `service` folders, while frontend tests include areas such as `student`, `teacher`, and `LoginPageViewTest`.

#### Run Unit Tests

To run all unit tests, use the following command:

```bash
mvn test
```
---
### Generate Coverage Report

mvn clean verify

The generated coverage report can be accessed at: target/site/jacoco/index.html

---

### Performance Testing

Basic performance testing was conducted manually.

---


## 14. Repository Structure

#### Main Directories

•	/backend – business logic

•	/frontend – UI components

•	/resources – configuration and localization

•	/tests – test cases

•	/docs – project documentation

•	/diagrams – UML diagrams

•	/docker – Dockerfile

•	/ci-cd – Jenkins file

---


## 15. Authors

Include:

Team members: 
- Poornima Jayamanna - Scrum Master, Database Design, SonarQube Analysis, CI/CD (Jenkins) Creation, Backend Development, Documentation Lead  

- Hathadura Chathurika - Scrum Master, Frontend Development, UI Testing, Documentation Support   

- Dilmi Merenchi Kankanamge - Scrum Master, System Design (UML Diagrams), Testing, Documentation(Bug Reporting) , Backend Development 

- Roshini Fernando  - Scrum Master, Frontend Development, Documentation (Heuristic Evaluation), presentation preparation

- Kumudu Nallaperuma -   Scrum Master, SonarQube Analysis Support, Quality Assurance, Backend Development, Documentation       

---

Course name and semester:
- Software Engineering Project TX00EY30-3011

- Semester 1 & 2, 2026
