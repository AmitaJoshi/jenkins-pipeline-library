package com.self.pipeline.optimizer
def call(Map pipelineParams)         
{         
	env.REPO = pipelineParams.REPO
    sh '''
	    sh /root/tomcat/bin/shutdown.sh 
	    sleep 10
        cp $WORKSPACE/$REPO/target/*.war /root/tomcat/webapps
	    cd /root/tomcat/webapps
	    chmod 755 *.war
	    export BUILD_ID=dontkillme
	    /root/tomcat/bin/startup.sh
    '''
}