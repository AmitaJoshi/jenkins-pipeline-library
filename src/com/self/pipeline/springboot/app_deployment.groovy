package com.self.pipeline.springboot

def call(Map pipelineParams) 
{
    env.DOCKER_REGISTRY = pipelineParams.DOCKER_REGISTRY
    env.APP_NAME = pipelineParams.APP_NAME
	sh '''
  	    docker run -d --name $APP_NAME -p 8082:8080 $DOCKER_REGISTRY/$APP_NAME 
    ''' 
}