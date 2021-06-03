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
                echo 'workspace is' $WORKSPACE
		            rm -rf $WORKSPACE/*
   		          echo 'deleted'
		            git clone --single-branch --branch ${BRANCH} ${SCM_URL}
                ''' 
	              echo "Checkout is completed!"
            }
            env.REPO = pipelineParams.REPO
            /*env.POM_PATH = REPO+"/pom.xml"
            pom = readMavenPom file: POM_PATH
            env.VERSION = pom.version
            env.ARTIFACTID = pom.artifactId
            env.PACKAGING = pom.packaging
            env.CURR_DATE = new Date().format( 'yyyyMMdd' )*/
            stage("build")
              {
               sh '''
               cd $REPO
               mvn clean install
               ''' 
              }
              stage("upload artifacts in Nexus")
              {
                nexusArtifactUploader artifacts: [
                  [
                    artifactId: 'dummy_webapp', 
                    classifier: '', 
                    file: 'target/dummy_webapp-0.0.1-SNAPSHOT.war', 
                    type: 'war'
                  ]
                  ], 
                  credentialsId: 'nexus_credentials', 
                  groupId: 'com.amita', 
                  nexusUrl: '192.168.1.19', 
                  nexusVersion: 'nexus2', 
                  protocol: 'http', 
                  repository: 'http://192.168.1.19:8081/repository/nexus_optimizer_repo/',
                  version: '0.0.1-SNAPSHOT'
              }
            
          }
       }
}
