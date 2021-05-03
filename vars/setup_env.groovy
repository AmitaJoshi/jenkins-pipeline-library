def call(Map pipelineParams) {
  try{
    timeout(time: 60, unit: 'MINUTES')
	{
      	env.BRANCH = pipelineParams.BRANCH
      	env.REPO = pipelineParams.REPO
      	env.BUILD_NODE = pipelineParams.BUILD_NODE
	env.GIT_GROUP = pipelineParams.GIT_GROUP
	env.ENVIRONMENT = pipelineParams.ENVIRONMENT
      	pipeline 
		{
       		node(pipelineParams.BUILD_NODE)
	      	{
          		stage("Code Checkout") 
			{
    			
    			echo "Code checkout from SCM Repo"
    			sh '''
  				echo 'workspace cleanup'$PWD
				rm -rf $WORKSPACE/*
   				echo 'deleted'
				git clone --single-branch --branch ${BRANCH} ${GIT_GROUP}${REPO}.git
    			''' 
    			echo "Checkout is completed!"
    			}
		     	stage("playbook deploy") 
			{
           		sh '''
			cd ${REPO}
             		ansible-playbook playbook.yml -i inventory.txt -e "ENV=$ENVIRONMENT"
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
