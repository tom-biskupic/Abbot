#!/bin/bash


docker run --name abbot-mysql --rm -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=AbbotDB -e MYSQL_USER=AbbotUser -e MYSQL_PASSWORD=password -v AbbotDB:/var/lib/mysql -d mysql:5.6.37 
