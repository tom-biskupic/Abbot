#!/bin/bash

dnf install -y docker
dnf install -y libxcrypt-compat

# make it so you don't need to sudo to run docker commands
usermod -aG docker ec2-user

# install docker-compose
mkdir -p /usr/libexec/docker/cli-plugins
curl -SL https://github.com/docker/compose/releases/latest/download/docker-compose-linux-$(uname -m) -o /usr/libexec/docker/cli-plugins/docker-compose
chmod +x /usr/libexec/docker/cli-plugins/docker-compose


# 
#	Copy the dockerfile into /srv/docker 
#
mkdir -p /opt/abbot
curl -o /opt/abbot/docker-compose.yml https://raw.githubusercontent.com/tom-biskupic/Abbot/refs/heads/${BRANCH}/docker-compose.yml

#
#	Add lets encrypt
#
git clone https://github.com/letsencrypt/letsencrypt /opt/letsencrypt

#
# Copy in the script required to make docker compose a daemon
#
cat << EOF > /etc/systemd/system/docker-compose-app.service
[Unit]
Description=Docker Compose Application Service
Requires=docker.service
After=docker.service

[Service]
Type=oneshot
RemainAfterExit=yes
WorkingDirectory=/opt/abbot
ExecStart=/usr/libexec/docker/cli-plugins/docker-compose up -d
ExecStop=/usr/libexec/docker/cli-plugins/docker-compose down
TimeoutStartSec=0

[Install]
WantedBy=multi-user.target
EOF

systemctl daemon-reload
systemctl enable docker-compose-app
systemctl start docker-compose-app
