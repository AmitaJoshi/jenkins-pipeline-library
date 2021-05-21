def call(Map pipelineParams) 
{
     pipeline
       {
         node(pipelineParams.BUILD_NODE)
	        {
          stage("Code Checkout") 
	            {
                pipelineParams.put('GIT_GROUP',pipelineParams.GIT_GROUP)
                pipelineParams.put('BRANCH',pipelineParams.BRANCH)
                pipelineParams.put('REPO',pipelineParams.REPO)
                env.SCM_URL= pipelineParams.GIT_URL+"/"+pipelineParams.GIT_GROUP+"/"+pipelineParams.REPO+".git"
                env.BRANCH = pipelineParams.BRANCH
                echo "Code checkout from SCM Repo"
                sh '''
  	        	  echo 'workspace cleanup'$PWD
		            rm -rf $WORKSPACE/*
   		          echo 'deleted'
		            git clone --single-branch --branch ${BRANCH} ${SCM_URL}
                ''' 
	              echo "Checkout is completed!"
              }
          stage("build")
              {
               env.REPO = pipelineParams.REPO
               sh '''
               cd $REPO
               mvn clean install
               ''' 
              }
          stage("deploy")
              {
                sh '''
                cd $WORKSPACE/$REPO/target
                cp *.war /root/tomcat/webapps 
                '''
              }
          stage("create symlink")
            {            
              echo "checkout ansible"
              pipelineParams.put('GIT_GROUP',pipelineParams.ANSIBLE_GIT_GROUP)
              pipelineParams.put('BRANCH',pipelineParams.ANSIBLE_BRANCH)
              pipelineParams.put('REPO',pipelineParams.ANSIBLE_REPO)
              env.ANSIBLE_REPO = pipelineParams.ANSIBLE_REPO
              env.SCM_URL= pipelineParams.GIT_URL+"/"+pipelineParams.GIT_GROUP+"/"+pipelineParams.REPO+".git"
              env.BRANCH = pipelineParams.BRANCH
              echo "Code checkout from SCM Repo"
                sh '''
                            echo 'workspace cleanup'$PWD
                            rm -rf $WORKSPACE/*
                            echo 'deleted'
                            git clone --single-branch --branch ${BRANCH} ${SCM_URL}
                    ''' 
	                  echo "Checkout is completed!"
                sh '''
                      cd $ANSIBLE_REPO
                      ansible-playbook symlink_artifacts_playbook.yml -i inventory.txt
                  ''' 
              }
        }
       }
}
