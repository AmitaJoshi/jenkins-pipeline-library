package com.self.pipeline.springboot

def call(Map pipelineParams) 
{
env.REPO = pipelineParams.REPO
env.DOCKER_HOST = pipelineParams.DOCKER_HOST
env.DOCKER_REGISTRY = pipelineParams.DOCKER_REGISTRY
sh '''
        export JAVA_HOME="/usr/lib/jvm/java-11-openjdk-11.0.11.0.9-0.el8_3.x86_64"
        echo " build a docker image"
        cd $REPO
        mvn deploy -P docker -Ddocker.host=${DOCKER_HOST} -Ddocker.registry.name=${DOCKER_REGISTRY}
    ''' 
}