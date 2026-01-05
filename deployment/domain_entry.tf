
resource "aws_lightsail_domain_entry" "www" {
  provider    = aws.global
  domain_name = var.domain_name
  name        = var.hostname
  type        = "A"
  target      = aws_lightsail_static_ip.www_ip.ip_address
}
