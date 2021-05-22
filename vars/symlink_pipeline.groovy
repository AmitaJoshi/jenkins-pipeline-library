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
            env.REPO = pipelineParams.REPO
            env.POM_PATH = REPO+"/pom.xml"
            pom = readMavenPom file: POM_PATH
            env.VERSION = pom.version
            env.ARTIFACTID = pom.artifactId
            env.PACKAGING = pom.packaging
            env.CURR_DATE = new Date().format( 'yyyyMMdd' )
          stage("build")
              {
               
               sh '''
               cd $REPO
               mvn clean install
               ''' 
              }
          stage("deploy")
              {
                sh '''
                cd $WORKSPACE/$REPO/target
                mkdir -p /opt/deployments/$CURR_DATE
                cp $ARTIFACTID-$VERSION.$PACKAGING /opt/deployments/$CURR_DATE
                '''
              }
          stage("create symlink")
            {    
              //No need to specify $ outside shell to use variables
              env.CURR_ARTIFACT = ARTIFACTID+"-"+VERSION+"."+PACKAGING
              env.ARTIFACT = ARTIFACTID+"."+PACKAGING
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
                      ansible-playbook -e "CURR_ARTIFACT=$CURR_ARTIFACT CURR_DATE=$CURR_DATE ARTIFACT=$ARTIFACT" symlink_artifacts_playbook.yml -i inventory.txt
                  ''' 
              }
        }
       }
}
