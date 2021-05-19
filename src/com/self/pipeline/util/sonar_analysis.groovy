package com.self.pipeline.util

def call(Map pipelineParams)
{
     env.REPO = pipelineParams.REPO
    withSonarQubeEnv('sonarQube'){
              sh '''
              cd $REPO 
              mvn sonar:sonar
              '''
            }
}