#!/bin/sh

# For execution based on crontab
/bin/sh ~/.bash_profile

# Root Directory (placed Agent Program in)
AGENT_ROOT_DIR=$(dirname $0)

# Job Name
JOB_NAME=$1

# Uncomment when PRODUCT stage
#java -Dconsumer.root.dir=$AGENT_ROOT_DIR -Djob.name=$JOB_NAME -jar $AGENT_ROOT_DIR/epis-ws-consumer.jar Biz >> $AGENT_ROOT_DIR/`date +%Y%m%d-%H%M%S`.biz.log

# For Testing(Comment when PRODUCT stage)
java -Dconsumer.root.dir=$AGENT_ROOT_DIR -Djob.name=$JOB_NAME -jar $AGENT_ROOT_DIR/target/epis-ws-consumer.jar Debug >> $AGENT_ROOT_DIR/`date +%Y%m%d-%H%M%S`.biz.debug.log