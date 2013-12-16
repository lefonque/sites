#!/bin/sh

# For execution based on crontab
/bin/sh ~/.bash_profile

# Root Directory (placed Agent Program in)
AGENT_ROOT_DIR=$(pwd)

# Execution
nohup java -Dconsumer.root.dir=$AGENT_ROOT_DIR -jar $AGENT_ROOT_DIR/epis-ws-agent-scheduler.jar&