#!/bin/bash

BACKUP_FILE="/home/carlos/backup/`date +%Y-%m-%d-%H-%M`.backup";

/usr/bin/pg_dump -T users -p 6543 -U diary --no-acl diary > $BACKUP_FILE
