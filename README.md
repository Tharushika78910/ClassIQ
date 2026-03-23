# ClassIQ – Teacher  Grade Book and Report Card System

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
* * **VcXsrv/ Xming** – X server for displaying JavaFX GUI from Docker
* **GitHub** for version control
* **i18n / Localization Framework** – UI translation support
* **Static Analysis Tools** – Code quality


---

## Localization Support

- The system now supports multiple languages, including non-Latin languages such as Sinhalese and Arabic.

### Features

- Language selector available in UI
- Dynamic language switching (no restart required)
- RTL layout support for Arabic 
- Locale-based formatting (dates, numbers)
- Externalized UI text using translation files

### How to change the language

1. Open the application.
2. Select language from the language dropdown
3. UI updates instantly


---


## Project Structure

```
ClassIQ
│
├── src/main        → Main application source code
├── src/test        → Unit tests
├── target          → Compiled project files
├── Dockerfile      → Docker configuration
├── Jenkins file    → Jenkins automation pipeline
├── pom.xml         → Maven configuration
└── README.md       → Project documentation
```

---

## Software Development Methodologies

This project includes a flowchart that illustrates three software development methodologies: **SDLC ,Agile, and DevOps**. These methodologies show different approaches to building and maintaining software systems.

### Methodology Overview

| Methodology | Description                                                                                                                            |
| ----------- |----------------------------------------------------------------------------------------------------------------------------------------|
| **SDLC**    | The process that step by step moves such as requirement analysis, system design, implementation, testing, deployment, and maintenance. |
| **Agile**   | An method which work is completed in short periods called sprints. Each sprint includes planning, development, testing, and review.    |
| **DevOps**  | DevOps is a modern method where development and operations work together to automatically build, test, and deliver software.
## Use Case Diagram
The use case diagram shows how users interact with the system.
![Use case Diagram](Diagrams/UseCase_Diagram(ClassIQ).jpeg)

## ER Diagram
![ER Diagram](ClassIQ_codes/src/main/resources/images/ER_Diagram.png)

## GitHub Repository
https://github.com/Tharushika78910/ClassIQ.git

## Trello board link
https://trello.com/b/uLfnPk8H/product-description-goal


---

## Run ClassIQ Project

### 1. Clone the Repository

Clone the project from GitHub:

```
git clone https://github.com/Tharushika78910/ClassIQ.git
cd ClassIQ
```

---

### 2. CI/CD Pipeline (Automatic Build)

Push the project to GitHub. Jenkins will automatically run the pipeline and perform the following steps:

* Build the project using **Maven**
* Run **JUnit unit tests**
* Generate **JaCoCo code coverage report**
* Build the **Docker image**
* Push the Docker image to **Docker Hub**

Docker image repository:

```
https://hub.docker.com/r/poornimj/classiq
```

---

### 3. Start Required Services

Before running the application, make sure the following services are running:

**MariaDB**

* Start the MariaDB server.
* Ensure the database `classiq` exists.

**VcXsrv / Xming (For JavaFX GUI)**

Start **VcXsrv or Xming** to allow the Docker container to display the JavaFX graphical interface on Windows.

---

### 4. Pull Docker Image

Download the latest Docker image from Docker Hub:

```
docker pull poornimj/classiq:latest
```

---

### 5. Run the Docker Container

Run the application container :

```
docker run --rm -e DISPLAY=host.docker.internal:0.0 -e DB_HOST=host.docker.internal -e DB_PORT=3306 -e DB_NAME=classiq -e DB_USER=root -e DB_PASS=root123 poornimj/classiq:latest
```

---

### 6. Launch Application

After running the container, the **ClassIQ JavaFX application will open on the desktop**.

## Author

Group 2 -
Software Engineering Project 1 TX00EY27-3011
