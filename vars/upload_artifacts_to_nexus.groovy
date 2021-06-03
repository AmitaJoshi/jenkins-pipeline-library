def call(Map pipelineParams) 
{
     pipeline
       {
         node(pipelineParams.BUILD_NODE)
	        { 
          if(!pipelineParams.SKIP_CHECKOUT)
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
              stage("upload artifacts in Nexus")
              {

              }
            stage("deploy")
              {
                sh '''
                cd $WORKSPACE/$REPO/target
                mkdir -p /opt/deployments/$CURR_DATE
                cp $ARTIFACTID-$VERSION.$PACKAGING /opt/deployments/$CURR_DATE
                '''
              }
          }
