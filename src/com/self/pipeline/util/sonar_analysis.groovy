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
    timeout(time: 1, unit: 'HOURS') { // Just in case something goes wrong, pipeline will be killed after a timeout
        def qualitygate = waitForQualityGate()
        if (qualitygate.status != "OK") {
            error "Pipeline aborted due to quality gate failure: ${qualitygate.status}"
        }
    }
}