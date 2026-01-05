#!/bin/bash

dnf install -y docker libxcrypt-compat certbot cronie mariadb105
systemctl start crond
systemctl enable crond

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
sed -i "s/##DOMAIN_NAME##/${DOMAIN_NAME}/g" /opt/abbot/docker-compose.yml

certbot certonly --non-interactive --agree-tos -d $DOMAIN_NAME -m $EMAIL --standalone
mkdir -p /opt/abbot/certs

cat << EOF > /opt/abbot/make-p12.sh
#!/bin/bash
DOMAIN_NAME=\$1
openssl pkcs12 -export \\
  -out /opt/abbot/certs/keystore.p12 \\
  -inkey /etc/letsencrypt/live/\$DOMAIN_NAME/privkey.pem \\
  -in /etc/letsencrypt/live/\$DOMAIN_NAME/fullchain.pem \\
  -passout  pass:changeit \\
  -name "abbotracemanager"
EOF
chmod +x /opt/abbot/make-p12.sh
/opt/abbot/make-p12.sh $DOMAIN_NAME

cat <<EOF > /opt/abbot/renew-certs.sh
#!/bin/bash
certbot renew --pre-hook "systemctl stop docker-compose-app" --post-hook "/opt/abbot/make-p12.sh $DOMAIN_NAME && systemctl start docker-compose-app" >> /var/log/le-renew.log
EOF

chmod +x /opt/abbot/renew-certs.sh
(crontab -l 2>/dev/null; echo "30 2 * * 1 /opt/abbot/renew-certs.sh") | crontab -

#
# Copy in the script required to make docker compose a daemon
#
cat <<EOF > /etc/systemd/system/docker-compose-app.service
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

cat <<EOF1 > /opt/abbot/dump-db.sh
#!/bin/bash
mysqldump -u AbbotUser -h localhost --protocol=TCP --port=3306 -p AbbotDB  > abbotdb-dump.sql
EOF1

chmod +x /opt/abbot/dump-db.sh

cat <<EOF2 > /opt/abbot/restore-db.sh
#!/bin/bash
mysql -u AbbotUser -h localhost --protocol=TCP --port=3306 -p AbbotDB  < abbotdb-dump.sql
EOF2

chmod +x /opt/abbot/restore-db.sh
