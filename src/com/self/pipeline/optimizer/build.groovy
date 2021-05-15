package com.self.pipeline.optimizer.build

def call(Map pipelineParams)
{     
        sh '''
            cd usermanagement_javasqlproject
            mvn clean install
        '''
}

