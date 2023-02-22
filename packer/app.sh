#!/bin/bash

#Initial
sudo apt-get update
sudo apt-get upgrade -y


#JAVA INSTALL
sudo apt install -y openjdk-18-jdk
sudo apt install -y openjdk-18-jre


#POSTGRESS INSTALL
sudo apt install -y postgresql postgresql-contrib
sudo systemctl start postgresql.service
sudo -u postgres psql --command="ALTER ROLE postgres WITH PASSWORD 'postgres';" --command="\du"
sudo -u postgres createdb --owner=postgres cloud


#CREATE WEBAPP DIRECTORY
mkdir webapp/
chmod 777 webapp/

#Copy the jar into your folder
sudo cp /home/ubuntu/webapp-0.0.1-SNAPSHOT.jar /home/ubuntu/webapp/webapp-0.0.1-SNAPSHOT.jar

#Starting the application as a service
sudo cp /tmp/webapp.service /etc/systemd/system/webapp.service
sudo systemctl daemon-reload
sudo systemctl enable webapp.service
sudo systemctl start webapp.service

chmod 755 webapp/