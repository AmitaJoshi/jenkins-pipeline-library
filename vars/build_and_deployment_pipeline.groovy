import com.self.pipeline.util.checkoutSCM;
import com.self.pipeline.optimizer.*;

def call(Map pipelineParams) 
{
      pipeline
      {
        node (pipelineParams.BUILD_NODE)
        {
          stage("Code Checkout") 
	        {
		        echo 'workspace cleanup'
            sh '''
            rm -rf $WORKSPACE/*
            echo 'deleted'
            git clone https://github.com/minaxijoshi3101/dummy_webapp.git
            echo"checkout is completed!"
            '''
          }
        }
	       
    }
  }
  

