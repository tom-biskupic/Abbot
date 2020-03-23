#!/bin/bash

# install latest version of docker the lazy way
curl -sSL https://get.docker.com | sh

# make it so you don't need to sudo to run docker commands
usermod -aG docker ubuntu

# install docker-compose
curl -L https://github.com/docker/compose/releases/download/1.25.4/docker-compose-$(uname -s)-$(uname -m) -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose

# 
#	Copy the dockerfile into /srv/docker 
#
mkdir /srv
mkdir /srv/docker
curl -o /srv/docker/docker-compose.yml https://raw.githubusercontent.com/tom-biskupic/Abbot/master/docker-compose.yml

#
#	Add lets encrypt
#
git clone https://github.com/letsencrypt/letsencrypt /opt/letsencrypt

#
# Copy in the script required to make docker compose a daemon
#
curl -o /etc/systemd/system/docker-compose-app.service https://raw.githubusercontent.com/tom-biskupic/Abbot/master/docker-compose-app.service
systemctl enable docker-compose-app
