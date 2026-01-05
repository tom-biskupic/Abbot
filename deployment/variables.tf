variable "availability_zone" {
  description = "The availability zone to deploy the Lightsail instance in"
  type        = string
  default     = "ap-southeast-2a"
}

variable "blueprint_id" {
  description = "The blueprint ID for the Lightsail instance"
  type        = string
  default     = "amazon_linux_2023"
}

variable "bundle_id" {
  description = "The bundle ID for the Lightsail instance"
  type        = string
  default     = "small_3_2"
}

variable "name" {
  description = "The name of the Lightsail instance"
  type        = string
  default     = "AbbotRaceManager"
}

variable "domain_name" {
  description = "The domain name to be used for the Lightsail domain"
  type        = string
  default     = "abbotracemanager.com"
}

variable "key_pair_name" {
  description = "The name of the AWS key pair"
  type        = string
  default     = "abbot_key_pair"
}

variable "hostname" {
  description = "The hostname for the Lightsail instance"
  type        = string
  default     = "www2"
}

variable "aws_region" {
  description = "The AWS region to deploy resources in"
  type        = string
  default     = "ap-southeast-2"
}

variable "branch" {
  description = "The branch of the Abbot repository to deploy from"
  type        = string
  default     = "tls_support"
}