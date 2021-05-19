package com.self.pipeline.util

def call(Map pipelineParams) 
{
            def mvnHome = tool name: 'maven-3', type: 'maven'
            withSonarQubeEnv('sonarQube')
            {
            sh "${mvnHome}/bin/mvn sonar:sonar"
            }
}