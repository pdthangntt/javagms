#!/bin/bash
# Database credentials
user="localsi"
password="gms12345"
host="localhost"

# Other options
backup_path="/var/www/db"
date=$(date +"%d-%b-%Y")
# Set default file permissions
umask 177
# Dump database into SQL file
db_name="dulieuhiv"
mysqldump --user=$user --password=$password --host=$host $db_name >$backup_path/$db_name-$date.sql

# Delete files older than 2 days
find $backup_path/* -mtime +2 -exec rm {} \;


#DB backup log
echo -e "$(date +'%d-%b-%y  %r '):ALERT:Database has been Backuped"    >>/var/log/DB_Backup.log
