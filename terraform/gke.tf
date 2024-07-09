variable "project_id" {
  description = "The GCP project ID"
  default     = "assignment-428215"
}

variable "gke_username" {
  default     = ""
  description = "gke username"
}

variable "gke_password" {
  default     = ""
  description = "gke password"
}

variable "gke_num_nodes" {
  default     = 1
  description = "number of gke nodes"
}

# GKE cluster
data "google_container_engine_versions" "gke_version" {
  location       = "us-central1-c"
  version_prefix = "1.29."
}

# Retrieve an access token as the Terraform runner
data "google_client_config" "provider" {}

provider "google" {
  project = var.project_id
  region  = "us-central1"
}

provider "kubernetes" {
  host                   = "https://${data.google_container_cluster.primary.endpoint}"
  token                  = data.google_client_config.provider.access_token
  cluster_ca_certificate = base64decode(data.google_container_cluster.primary.master_auth[0].cluster_ca_certificate)
}

resource "google_container_cluster" "primary" {
  name     = "cluster-assignment-3"
  location = "us-central1-c"

  # We can't create a cluster with no node pool defined, but we want to only use
  # separately managed node pools. So we create the smallest possible default
  # node pool and immediately delete it.
  remove_default_node_pool = true
  initial_node_count       = 1

  workload_identity_config {
    workload_pool = "${var.project_id}.svc.id.goog"
  }

  # Uncomment these lines if you have a custom VPC and subnet
  # network    = google_compute_network.vpc.name
  # subnetwork = google_compute_subnetwork.subnet.name
}

resource "google_container_node_pool" "primary_nodes" {
  name     = "primary-node-pool"
  location = "us-central1-c"
  cluster  = google_container_cluster.primary.name

  version    = data.google_container_engine_versions.gke_version.release_channel_latest_version["STABLE"]
  node_count = var.gke_num_nodes


  node_config {
    oauth_scopes = [
      "https://www.googleapis.com/auth/logging.write",
      "https://www.googleapis.com/auth/monitoring",
      "https://www.googleapis.com/auth/devstorage.read_only"
    ]

    labels = {
      env = "assignment3-428215"
    }

    machine_type = "e2-small"
    tags         = ["gke-node", "cluster-assignment-3"]

    metadata = {
      disable-legacy-endpoints = "true"
    }
    service_account="352845718952-compute@developer.gserviceaccount.com"
    disk_type = "pd-standard"
    disk_size_gb = 10
  }
}


output "endpoint" {
  value = google_container_cluster.primary.endpoint
}

output "cluster_ca_certificate" {
  value = google_container_cluster.primary.master_auth[0].cluster_ca_certificate
}

output "token" {
  value = data.google_client_config.provider.access_token
  sensitive=true
}