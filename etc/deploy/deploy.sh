#!/bin/bash

HOME_PATH=metamac-structural-resources
TRANSFER_PATH=$HOME_PATH/tmp
DEPLOY_TARGET_PATH_EXTERNAL=/servers/edatos-external/tomcats/edatos-external01/webapps
DEPLOY_TARGET_PATH_INTERNAL=/servers/edatos-internal/tomcats/edatos-internal01/webapps
ENVIRONMENT_RELATIVE_PATH_FILE=WEB-INF/classes/metamac/environment.xml
LOGBACK_RELATIVE_PATH_FILE=WEB-INF/classes/logback.xml
RESTART=1

if [ "$1" == "--no-restart" ]; then
    RESTART=0
fi


scp -r etc/deploy deploy@estadisticas.arte-consultores.com:$TRANSFER_PATH
scp metamac-srm-web/target/structural-resources-internal-*.war deploy@estadisticas.arte-consultores.com:$TRANSFER_PATH/structural-resources-internal.war
scp metamac-srm-external-web/target/structural-resources-*.war deploy@estadisticas.arte-consultores.com:$TRANSFER_PATH/structural-resources.war
ssh deploy@estadisticas.arte-consultores.com <<EOF

    chmod a+x $TRANSFER_PATH/deploy/*.sh;
    . $TRANSFER_PATH/deploy/utilities.sh

    ###
    # STRUCTURAL-RESOURCES-INTERNAL
    ###
    
    if [ $RESTART -eq 1 ]; then
        sudo service edatos-internal01 stop
        checkPROC "edatos-internal"
    fi

    # Update Process
    sudo rm -rf $DEPLOY_TARGET_PATH_INTERNAL/structural-resources-internal
    sudo mv $TRANSFER_PATH/structural-resources-internal.war $DEPLOY_TARGET_PATH_INTERNAL/structural-resources-internal.war
    sudo unzip $DEPLOY_TARGET_PATH_INTERNAL/structural-resources-internal.war -d $DEPLOY_TARGET_PATH_INTERNAL/structural-resources-internal
    sudo rm -rf $DEPLOY_TARGET_PATH_INTERNAL/structural-resources-internal.war

    # Restore Configuration
    sudo cp $HOME_PATH/environment_internal.xml $DEPLOY_TARGET_PATH_INTERNAL/structural-resources-internal/$ENVIRONMENT_RELATIVE_PATH_FILE
    sudo cp $HOME_PATH/logback_internal.xml $DEPLOY_TARGET_PATH_INTERNAL/structural-resources-internal/$LOGBACK_RELATIVE_PATH_FILE

    if [ $RESTART -eq 1 ]; then
        sudo chown -R edatos-internal.edatos-internal /servers/edatos-internal     
        sudo service edatos-internal01 start
    fi

    ###
    # STRUCTURAL-RESOURCES-EXTERNAL
    ###
    
    if [ $RESTART -eq 1 ]; then
        sudo service edatos-external01 stop
        checkPROC "edatos-external"
    fi

    # Update Process
    sudo rm -rf $DEPLOY_TARGET_PATH_EXTERNAL/structural-resources
    sudo mv $TRANSFER_PATH/structural-resources.war $DEPLOY_TARGET_PATH_EXTERNAL/structural-resources.war
    sudo unzip $DEPLOY_TARGET_PATH_EXTERNAL/structural-resources.war -d $DEPLOY_TARGET_PATH_EXTERNAL/structural-resources
    sudo rm -rf $DEPLOY_TARGET_PATH_EXTERNAL/structural-resources.war

    # Restore Configuration
    sudo cp $HOME_PATH/environment_external.xml $DEPLOY_TARGET_PATH_EXTERNAL/structural-resources/$ENVIRONMENT_RELATIVE_PATH_FILE
    sudo cp $HOME_PATH/logback_external.xml $DEPLOY_TARGET_PATH_EXTERNAL/structural-resources/$LOGBACK_RELATIVE_PATH_FILE

    if [ $RESTART -eq 1 ]; then
        sudo chown -R edatos-external.edatos-external /servers/edatos-external        
        sudo service edatos-external01 start
    fi

    echo "Finished deploy"

EOF
