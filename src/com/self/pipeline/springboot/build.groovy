package com.self.pipeline.springboot

def call(Map pipelineParams) 
{
env.REPO = pipelineParams.REPO
sh 
    '''
        echo "create docker registry and build a docker image"
    ''' 
}