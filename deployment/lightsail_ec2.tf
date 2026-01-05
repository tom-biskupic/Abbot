


resource "aws_lightsail_instance" "abbot_instance" {
  name              = var.name
  availability_zone = var.availability_zone
  blueprint_id      = var.blueprint_id
  bundle_id         = var.bundle_id
  user_data         = "export BRANCH=${var.branch} && export DOMAIN_NAME=${var.hostname}.${var.domain_name} && curl https://raw.githubusercontent.com/tom-biskupic/Abbot/refs/heads/${var.branch}/lightsail-compose.sh | bash"
  key_pair_name     = aws_lightsail_key_pair.aws_light_sail_abbot_key_pair.id
}

resource "aws_lightsail_instance_public_ports" "abbot_instance" {
  instance_name = aws_lightsail_instance.abbot_instance.name

  port_info {
    protocol  = "tcp"
    from_port = 22
    to_port   = 22
  }

  port_info {
    protocol  = "tcp"
    from_port = 443
    to_port   = 443
  }

  #
  # Needed for certbot to do http-01 challenge
  #
  port_info {
    protocol  = "tcp"
    from_port = 80
    to_port   = 80
  }
}

resource "aws_lightsail_static_ip_attachment" "abbot_instance" {
  static_ip_name = aws_lightsail_static_ip.www_ip.id
  instance_name  = aws_lightsail_instance.abbot_instance.id
}
