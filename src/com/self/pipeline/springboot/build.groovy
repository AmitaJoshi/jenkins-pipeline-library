package com.self.pipeline.springboot

def call(Map pipelineParams) 
{
env.REPO = pipelineParams.REPO
env.DOCKER_HOST = pipelineParams.DOCKER_HOST
env.DOCKER_REGISTRY = pipelineParams.DOCKER_REGISTRY
sh '''
        echo " build a docker image"
        cd $REPO
        mvn deploy -P docker -Ddocker.host=${DOCKER_HOST} -Ddocker.registry.name=${DOCKER_REGISTRY}
    ''' 
}