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
               export JAVA_HOME="/usr/lib/jvm/java-11-openjdk-11.0.11.0.9-0.el8_3.x86_64"
               echo " build a docker image"
               cd $REPO
               mvn deploy -P docker -Ddocker.host=${DOCKER_HOST} -Ddocker.registry.name=${DOCKER_REGISTRY}
              ''' 
          }
            }
        }
       }
}