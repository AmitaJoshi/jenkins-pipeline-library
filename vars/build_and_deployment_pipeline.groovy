def call(Map pipelineParams) {
  try{
    timeout(time: 60, unit: 'MINUTES') {
      env.BRANCH = pipelineParams.BRANCH
      env.REPO = pipelineParams.REPO
      pipeline {
       
        node(pipelineParams.BUILD_NODE)
	      {
          stage("Code Checkout") 
		{
		
          env.SCM_URL=REPO
    echo "Code checkout from SCM Repo"
    sh ''' 
    FILE=/root/workspace/*
if [[ -f "$FILE" ]]; then
    echo "$FILE exists."
fi
  	  cd /root/workspace
		rm -rf *
        rm -rf ${REPO}
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
	}
      
      }
	    post{
		    always{
		    cleanWs()
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
