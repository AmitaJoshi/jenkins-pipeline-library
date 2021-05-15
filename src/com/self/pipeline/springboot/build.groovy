package com.self.pipeline.springboot

def call(Map pipelineParams) 
{
env.REPO = pipelineParams.REPO
sh '''
        echo " build a docker image"
        mvn deploy -P docker -Ddocker.host=${DOCKER_HOST} -Ddocker.registry.name=${DOCKER_REGISTRY}
    ''' 
}