def call(Map pipelineParams) {
  try{
    timeout(time: 60, unit: 'MINUTES') {
      env.BRANCH = pipelineParams.BRANCH
      env.REPO = pipelineParams.REPO
      pipeline
       {
        node(pipelineParams.BUILD_NODE)
	    {
	  stage("playbook deploy") 
			{
           		sh '''
			cd ${REPO}
             		ansible-playbook playbook.yml -i inventory.txt -e "ENV=$ENVIRONMENT"
              		'''
         		}
          stage("Code Checkout") 
	        {
		        env.SCM_URL=REPO
                echo "Code checkout from SCM Repo"
                sh '''
  	        	echo 'workspace cleanup'$PWD
		        rm -rf $WORKSPACE/*
   		        echo 'deleted'
		        git clone --single-branch --branch ${BRANCH} ${SCM_URL}
                ''' 
                echo "Checkout is completed!"
            }
		stage("Build") {
           sh '''
            cd usermanagement_javasqlproject
            mvn clean install
        '''
        }
        stage("War Deployment"){
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
		    stage("upload artifacts to nexus")
		    {
			    sh '''
			    echo "artifacts uploaded to nexus server"
			    '''
		    }
	}
        }
	       
    }
  }
   
    catch (err) {
    echo "in catch block" 
    echo "Caught: ${err}" 
    currentBuild.result = 'FAILURE' 
    
    throw err
  }
}
