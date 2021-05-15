def call(Map pipelineParams)         
{         
    sh '''
	    sh /root/tomcat/bin/shutdown.sh 
	    sleep 10
        cp $WORKSPACE/usermanagement_javasqlproject/target/*.jar /root/tomcat/webapps
	    cd /root/tomcat/webapps
	    chmod 755 *.jar
	    export BUILD_ID=dontkillme
	    /root/tomcat/bin/startup.sh
    '''
}