def call(Map pipelineParams) {
  try{
    timeout(time: 60, unit: 'MINUTES') {
      env.BRANCH = pipelineParams.BRANCH
      env.REPO = pipelineParams.REPO
      env.BUILD_NODE = pipelineParams.BUILD_NODE
      pipeline {
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
		     stage("playbook deploy") {
           sh '''
             ansible-playbook playbook.yml -i inventory.txt -e "ENV=$BUILD_NODE"
              '''
         }
        }
    catch (err) {
    echo "in catch block" 
    echo "Caught: ${err}" 
    currentBuild.result = 'FAILURE' 
    
    throw err
  }
}
