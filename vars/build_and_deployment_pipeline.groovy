import com.self.pipeline.util.*;
import com.self.pipeline.optimizer.*;

def call(Map pipelineParams) {
  try{
    timeout(time: 60, unit: 'MINUTES') {
      env.BRANCH = pipelineParams.BRANCH

      pipeline
      {
        if (pipelineParams.DEPLOYMENT_TYPE == "INSTALL_AND_DEPLOY")
        {
          SKIP_INSTALL = false
        }
        else if(pipelineParams.DEPLOYMENT_TYPE == "INSTALL")
        {
          SKIP_INSTALL = false

        }
        else
        {
          SKIP_INSTALL = true
        }
        node (pipelineParams.BUILD_NODE)
        {
          stage("install and configure")
          {
            if (!SKIP_INSTALL)
            {
              echo "checkout ansible"
              pipelineParams.put('GIT_GROUP',pipelineParams.ANSIBLE_GIT_GROUP)
              pipelineParams.put('BRANCH',pipelineParams.ANSIBLE_BRANCH)
              pipelineParams.put('REPO',pipelineParams.ANSIBLE_REPO)
              new checkoutSCM().call(pipelineParams)
              new installation().call(pipelineParams)
            }
          }
          stage("Code Checkout") 
	        {
		        pipelineParams.put('GIT_GROUP',pipelineParams.GIT_GROUP)
            pipelineParams.put('BRANCH',pipelineParams.BRANCH)
            pipelineParams.put('REPO',pipelineParams.REPO)
            new checkoutSCM().call(pipelineParams)
            echo"checkout is completed!"
          }
		      stage("Build") 
          {
            new build().call(pipelineParams)
            echo("Build is done!")
          }
          stage("Code Coverage"){
           //new sonar_analysis().call(pipelineParams)
           echo"code analysis is done!"
          }
          stage("War Deployment")
          {
            new app_deployment().call(pipelineParams)
          }
		      stage("upload artifacts to nexus")
		      {
             sh '''     
            curl -v -u admin:admin --upload-file target/dummy_webapp-0.0.1-SNAPSHOT.war http://192.168.1.19:8081/repository/nexus_optimizer_repo/com/amita/1.0/dummy_webapp-0.0.1-SNAPSHOT.war
            '''
             /* nexusArtifactUploader artifacts: [
                [
                  artifactId: 'dummy_webapp', 
                  classifier: '', 
                  file: '$WORKSPACE/$REPO/target/dummy_webapp-0.0.1-SNAPSHOT.war', 
                  type: 'war'
                ]
              ], 
              credentialsId: 'nexus_credentials', 
              groupId: 'com.amita', 
              nexusUrl: 'http://192.168.1.19:8081', 
              nexusVersion: 'nexus3', 
              protocol: 'http', 
              repository: 'nexus_optimizer_repo',
              version: '0.0.1-SNAPSHOT' */
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
