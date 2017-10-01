// Server-Side Data Backup Service
// example:
// 1) rent a server (an executor of the backup script) and make sure the server is always up
// 2) write a backup script
// 3) configure a Cron job at the executor to run the backup script
//    it should pull the data from the MySQL database, package it, and upload it to a safe place, ex. Amazon S3 bucket
// 3) we need to back up (or update) the backup scripts too
//    i.e. SSH the updated script to the executor whenever it is changed
//
// ThreeCopies.com is a web service for the above task
// 1) a hosted executor of the backup script, which you can edit through a web interface
// 2) it starts a Docker container and runs your backup script inside
// 3) the backup script will start every hour, every day and every week (hence the name: "three copies")
//    i.e. the $period environment variable has the value of either hour, day, or week
//
// ex. the backup script for backing up a remote MySQL database
// # skip the hourly backup
// if [ "${period}" == "hour" ]; then exit 0; fi
//
// # dump the entire database into a file (dump a remote database to a local file)
// mysqldump --lock-tables=false --host=db.thepmp.com --user=thepmp --password=**** --databases thepmp > thepmp.sql
//
// # compress the local file
// filename="$(date "+%Y-%m-%d-%H-%M").tgz"
// tar czf "${filename}" thepmp.sql
//
// # upload the local file to a Amazon S3 bucket (or use scp, ftp, etc.)
// echo "[default]" > ~/.s3cfg
// echo "access_key=AKIAICJKH*****CVLAFA" >> ~/.s3cfg
// echo "secret_key=yQv3g3ao654Ns**********H1xQSfZlTkseA0haG" >> ~/.s3cfg
// s3cmd --no-progress put "${tgz}" "s3://backup.yegor256.com/${tgz}"
