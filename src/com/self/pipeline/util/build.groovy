package com.self.pipeline.util

def call(Map pipelineParams)
{     
        sh '''
            cd usermanagement_javasqlproject
            mvn clean install
        '''
}
