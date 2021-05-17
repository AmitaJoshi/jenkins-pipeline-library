import com.self.pipeline.util.checkoutSCM;
import com.self.pipeline.springboot.*;

def call(Map pipelineParams) {
  try{
    timeout(time: 60, unit: 'MINUTES') {
      env.BRANCH = pipelineParams.BRANCH
      
      pipeline
       {
        if(pipelineParams.DEPLOYMENT_TYPE == "INSTALL_AND_DEPLOY")
        {
          SKIP_INSTALL = false
        }
        else if (pipelineParams.DEPLOYMENT_TYPE == "INSTALL")
        {
          SKIP_INSTALL = false
        }
        else
        {
           SKIP_INSTALL = true
        }
        node(pipelineParams.BUILD_NODE)
	      {
          stage("install and configure")
          {
            if(!SKIP_INSTALL)
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
              echo "Checkout is completed!"
          }
          stage("Build") 
          {
              new build().call(pipelineParams)
          }
          stage("App Deployment")
          {
            new app_deployment().call(pipelineParams)
          }
          stage("SonarQube-Analysis")
          {
            
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
