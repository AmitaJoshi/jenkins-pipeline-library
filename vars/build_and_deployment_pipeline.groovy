def call(Map pipelineParams) {
  try{
    timeout(time: 60, unit: 'MINUTES') {
      env.BRANCH = pipelineParams.BRANCH
      env.REPO = pipelineParams.REPO
      pipeline
       {
        node(pipelineParams.BUILD_NODE)
	    {
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
            cp $WORKSPACE/build_and_deployment_pipeline/usermanagement_javasqlproject/target/*.war /root/tomcat/webapps
            echo 'war deployed successfully'
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
