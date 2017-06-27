#!/bin/sh

HOME_PATH=metamac-structural-resources
TRANSFER_PATH=$HOME_PATH/tmp
DEPLOY_TARGET_PATH=/servers/metamac/tomcats/metamac01/webapps
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

    if [ $RESTART -eq 1 ]; then
        sudo service metamac01 stop
        checkPROC "metamac"
    fi

    ###
    # STRUCTURAL-RESOURCES-INTERNAL
    ###

    # Update Process
    sudo rm -rf $DEPLOY_TARGET_PATH/structural-resources-internal
    sudo mv $TRANSFER_PATH/structural-resources-internal.war $DEPLOY_TARGET_PATH/structural-resources-internal.war
    sudo unzip $DEPLOY_TARGET_PATH/structural-resources-internal.war -d $DEPLOY_TARGET_PATH/structural-resources-internal
    sudo rm -rf $DEPLOY_TARGET_PATH/structural-resources-internal.war

    # Restore Configuration
    sudo cp $HOME_PATH/environment_internal.xml $DEPLOY_TARGET_PATH/structural-resources-internal/$ENVIRONMENT_RELATIVE_PATH_FILE
    sudo cp $HOME_PATH/logback_internal.xml $DEPLOY_TARGET_PATH/structural-resources-internal/$LOGBACK_RELATIVE_PATH_FILE


    ###
    # STRUCTURAL-RESOURCES-EXTERNA
    ###

    # Update Process
    sudo rm -rf $DEPLOY_TARGET_PATH/structural-resources
    sudo mv $TRANSFER_PATH/structural-resources.war $DEPLOY_TARGET_PATH/structural-resources.war
    sudo unzip $DEPLOY_TARGET_PATH/structural-resources.war -d $DEPLOY_TARGET_PATH/structural-resources
    sudo rm -rf $DEPLOY_TARGET_PATH/structural-resources.war

    # Restore Configuration
    sudo cp $HOME_PATH/environment.xml $DEPLOY_TARGET_PATH/structural-resources/$ENVIRONMENT_RELATIVE_PATH_FILE
    sudo cp $HOME_PATH/logback.xml $DEPLOY_TARGET_PATH/structural-resources/$LOGBACK_RELATIVE_PATH_FILE

    if [ $RESTART -eq 1 ]; then
        sudo chown -R metamac.metamac /servers/metamac
        sudo service metamac01 start
    fi

    #checkURL "http://estadisticas.arte-consultores.com/structural-resources-internal" "metamac01"
    #checkURL "http://estadisticas.arte-consultores.com/structural-resources/latest" "metamac01"
    echo "Finished deploy"

EOF
