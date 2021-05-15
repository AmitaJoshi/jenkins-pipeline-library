package com.self.pipeline.optimizer

def call(Map pipelineParams)
{     
    env.REPO = pipelineParams.REPO
        sh '''
            cd $REPO
            mvn clean install
        '''
}

