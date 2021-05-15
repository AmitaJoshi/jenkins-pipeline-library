package com.self.pipeline.optimizer

def call(Map pipelineParams)
{     
        sh '''
            cd usermanagement_javasqlproject
            mvn clean install
        '''
}

