#!/bin/bash

#
# Update everything
#
yum update -y

#
# Install docker and add the default user (ec2-user) to the docker group
#
yum install -y docker
usermod -aG docker ec2-user

#
# Install and setup docker-compose
#
curl -L https://github.com/docker/compose/releases/download/1.21.0/docker-compose-`uname -s`-`uname -m` | sudo tee /usr/local/bin/docker-compose > /dev/null
chmod +x /usr/local/bin/docker-compose
ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose

# 
#	Copy the dockerfile into /srv/docker 
#
mkdir /srv/docker
curl -o /srv/docker/docker-compose.yml https://raw.githubusercontent.com/tombi/abbot/master/docker-compose.yml

#
# Copy in the script required to make docker compose a daemon
#
curl -o /etc/systemd/system/docker-compose-app.service https://raw.githubusercontent.com/tombi/abbot/master/docker-compose-app.service
systemctl enable docker-compose-app
