def call(Map pipelineParams) 
{
     pipeline
       {
           stage("Code Checkout") 
	            {
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
                cd $WORKSPACE/target
                cp *.war /root/tomcat/webapps 
                '''
              }
              stage("create symlink")
              {
                sh ""
              }
        }
}
