#!/bin/sh

TMP_PATH=/servers/metamac/tmp
DEPLOY_TARGET_PATH=/servers/metamac/tomcats/metamac01/webapps
ENVIRONMENT_RELATIVE_PATH_FILE=WEB-INF/classes/metamac/environment.xml
LOGBACK_RELATIVE_PATH_FILE=WEB-INF/classes/logback.xml

scp -r etc/deploy deploy@estadisticas.arte-consultores.com:
scp metamac-srm-web/target/structural-resources-internal-*.war deploy@estadisticas.arte-consultores.com:structural-resources-internal.war
scp metamac-srm-external-web/target/structural-resources-*.war deploy@estadisticas.arte-consultores.com:structural-resources.war
ssh deploy@estadisticas.arte-consultores.com <<EOF

    chmod a+x deploy/*.sh;
    . deploy/utilities.sh
    
    sudo service metamac01 stop
    checkPROC "metamac"
    
    ###
    # STRUCTURAL-RESOURCES-INTERNAL
    ###
    # Backup Configuration
    sudo mv $DEPLOY_TARGET_PATH/structural-resources-internal/$ENVIRONMENT_RELATIVE_PATH_FILE $TMP_PATH/environment.xml_structural-resources-internal_tmp
    sudo mv $DEPLOY_TARGET_PATH/structural-resources-internal/$LOGBACK_RELATIVE_PATH_FILE $TMP_PATH/logback.xml_structural-resources-internal_tmp
    
    # Update Process
    sudo mv structural-resources-internal.war $DEPLOY_TARGET_PATH/structural-resources-internal.war
    sudo unzip $DEPLOY_TARGET_PATH/structural-resources-internal.war -d $DEPLOY_TARGET_PATH
    sudo rm -rf $DEPLOY_TARGET_PATH/structural-resources-internal.war
    
    # Restore Configuration
    sudo mv $TMP_PATH/environment.xml_structural-resources-internal_tmp $DEPLOY_TARGET_PATH/structural-resources-internal/$ENVIRONMENT_RELATIVE_PATH_FILE
    sudo mv $TMP_PATH/logback.xml_structural-resources-internal_tmp $DEPLOY_TARGET_PATH/structural-resources-internal/$LOGBACK_RELATIVE_PATH_FILE
    
    
    ###
    # STRUCTURAL-RESOURCES
    ###
    # Backup Configuration
    sudo mv $DEPLOY_TARGET_PATH/structural-resources/$ENVIRONMENT_RELATIVE_PATH_FILE $TMP_PATH/environment.xml_structural-resources_tmp
    sudo mv $DEPLOY_TARGET_PATH/structural-resources/$LOGBACK_RELATIVE_PATH_FILE $TMP_PATH/logback.xml_structural-resources_tmp
    
    # Update Process
    sudo mv structural-resources.war $DEPLOY_TARGET_PATH/structural-resources.war
    sudo unzip $DEPLOY_TARGET_PATH/structural-resources.war -d $DEPLOY_TARGET_PATH
    sudo rm -rf $DEPLOY_TARGET_PATH/structural-resources.war
    
    # Restore Configuration
    sudo mv $TMP_PATH/environment.xml_structural-resources_tmp $DEPLOY_TARGET_PATH/structural-resources/$ENVIRONMENT_RELATIVE_PATH_FILE
    sudo mv $TMP_PATH/logback.xml_structural-resources_tmp $DEPLOY_TARGET_PATH/structural-resources/$LOGBACK_RELATIVE_PATH_FILE
    
    sudo chown -R metamac.metamac /servers/metamac
    sudo service metamac01 start
    checkURL "http://estadisticas.arte-consultores.com/structural-resources-internal" "metamac01"
    checkURL "http://estadisticas.arte-consultores.com/structural-resources/latest" "metamac01"

EOF
