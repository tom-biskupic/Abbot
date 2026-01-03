
resource "aws_lightsail_domain" "abbot_domain" {
  domain_name = "example.com"
}

resource "aws_lightsail_domain_entry" "www" {
  domain_name = aws_lightsail_domain.abbot_domain.domain_name
  name        = "www"
  type        = "A"
  target      = aws_lightsail_static_ip.www_ip.ip_address
}
