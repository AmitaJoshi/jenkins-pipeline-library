package com.self.pipeline.springboot

def call(Map pipelineParams) 
{
    def mvnHome = tool name: 'maven' , type: 'maven'
   
    withSOnarQubeEnv('sonar')
    {
        sh "${mvnHome}/bin/mvn sonar:sonar"
    }
   
}