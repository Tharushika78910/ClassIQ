# Sprint 4 ( 03/03/2025 - 10/03/2025 )

---

## Sprint Goal

---

The goal of Sprint 4 is to finalize the ClassIQ prototype, containerize the complete application using Docker, publish the Docker image to Docker Hub, and prepare the system for final demonstration

## Selected Product Backlog Items

---

- Finalize authentication, feedback, and data visualization features
- Fix remaining UI and backend bugs
- Create Dockerfile for full application
- Build and test Docker image locally
- Push Docker image to Docker Hub
- Test deployment using Docker Play
- Prepare final presentation & documentation
- Update GitHub README and Sprint Reports


## Planned Tasks/Breakdown

---

- Finalize Product Functionality

In Sprint 4, the team completes the remaining feature implementations and fixes any UI rendering issues. The authentication system and database operations are tested to make sure they work correctly. The team also validates that data input, retrieval, and display function properly. Finally, integration testing is performed to ensure all parts of the system work together smoothly.


- Create Docker Image (GUI Docker Image)

For Sprint 4, the team focuses on containerizing and sharing the full application and create a Docker image for the complete system (frontend and backend) by writing a Dockerfile with the correct build steps, base image, dependencies, application code, and startup command. After that, build the image using docker build and test it locally using docker run to confirm everything works inside the container.

- Push Docker Image to Docker Hub

In this task make the image publicly accessible by creating a Docker Hub account, tagging the image using the Docker Hub username and a version, and uploading it with docker push. Once uploaded, verify that the image is publicly available on Docker Hub. Finally,  test the deployed image using Docker Play.


- Prototype Sharing & Presentation  

This task defines prepare a PowerPoint presentation to present the project to the class and also collect feedback from classmates to improve the project. Finally,  update the project documentation on GitHub and submit the final Sprint Report.

  

## Team Capacity & Assumptions

---

The team members have knowledge of Java development, UI design, database management, and Docker containerization. They can implement features, fix bugs, test the system, and deploy the application using Docker and GitHub.


## Definition of Done


___


A backlog item is considered done when the feature is fully implemented and the application should run without crashes and be tested manually to confirm it works correctly. In addition, the Docker image should build successfully and run properly in a container. The image must be publicly available on Docker Hub, and the project documentation should be updated on GitHub. Finally, the system should be ready for the live demonstration.