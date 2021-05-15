package com.self.pipeline.util

def call(Map pipelineParams)
{
    env.SCM_URL = pipelineParams.GIT_URL+"/"+pipelineParams.GIT_GROUP+"/"+pipelineParams.REPO+".git"
    env.branch = pipelineParams.BRANCH
    sh
        '''
        echo 'workspace cleanup'$PWD
        rm -rf $WORKSPACE/*
        echo 'deleted'
        git clone --single-branch --branch {$BRANCH} {$SCM_URL}
        '''
}