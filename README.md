# Kubernetes Microservices Deployment 🚀

This project demonstrates the deployment of **two microservices** on **Google Kubernetes Engine (GKE)** using **Terraform** and **Google Cloud Build** for CI/CD automation. The services interact with each other and use a **Persistent Volume** for storing and retrieving files.

## **🛠 Technologies Used**
- **Google Kubernetes Engine (GKE)**
- **Terraform** for infrastructure provisioning
- **Docker** for containerization
- **Google Cloud Build** for CI/CD
- **Kubernetes Configurations (YAML)**
- **Persistent Volumes** for file storage
- **Java/Springboot** for microservices
- **REST APIs** for file handling & calculations

---

## **🚀 Microservices Overview**
### **📌 Microservice 1 (File Storage Service)**
- Accepts file uploads via REST API.
- Stores uploaded files in a **Persistent Volume (PV)**.
- Retrieves file operations using Microservice 2 when requested.

### **📌 Microservice 2 (Computation Service)**
- Fetches files from **Persistent Volume (PV)** on a call from the Microservice 1.
- Performs operations (e.g.file validation, Calculating sum ,etc.).
- Returns computed results as API responses to the Microservice 1.

---

⚡️ CI/CD with Google Cloud Build
cloudbuild.yaml automates the following:
- Builds Docker images for both microservices.
- Pushes images to Google Container Registry (GCR).
- Cleans up the old deployments if present on the GKE cluster.
- Deploys to the Kubernetes GKE cluster.
- If any new push is made to any of the repository then the deployment is updated automatically.
