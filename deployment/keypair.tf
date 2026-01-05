resource "tls_private_key" "abbot_key_pair" {
  algorithm = "RSA"
  rsa_bits  = 4096
}

resource "aws_lightsail_key_pair" "aws_light_sail_abbot_key_pair" {
  name   = var.key_pair_name
  public_key = tls_private_key.abbot_key_pair.public_key_openssh
}

#
# Store the private and public key in AWS Secrets Manager
#

locals {
  secret_content = {
    private_key = tls_private_key.abbot_key_pair.private_key_pem
    public_key = tls_private_key.abbot_key_pair.public_key_openssh
  }
}

resource "aws_secretsmanager_secret" "abbot_ssh_key" {
  name        = "abbotracemanager-ssh-key"
  description = "The private key for the example service"
}

resource "aws_secretsmanager_secret_version" "example_key_version" {
  secret_id     = aws_secretsmanager_secret.abbot_ssh_key.id
  secret_string = jsonencode(local.secret_content)
}
