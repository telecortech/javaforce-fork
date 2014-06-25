#!/bin/bash
USERNAME=digiboy86,javaforce
SITE=web.sf.net
FOLDER=/home/project-web/javaforce/htdocs/ubuntu
rsync -v *.deb *.gz *.list *.gpg $USERNAME@$SITE:$FOLDER
