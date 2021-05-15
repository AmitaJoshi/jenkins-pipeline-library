package com.self.pipeline.util;

def call(Map pipelineParams)
{
    env.SCM_URL=pipelineParams.GIT_URL+"/"+pipelineParams.GIT_GROUP+"/"+pipelineParams.REPO+".git"
    env.BRANCH=pipelineParams.BRANCH
    sh
    '''
        echo 'workspace cleanup'
        rm -rf $WORKSPACE/*
        echo 'deleted'
        git clone --single-branch --branch${BRANCH} ${SCM_URL}
    '''
}