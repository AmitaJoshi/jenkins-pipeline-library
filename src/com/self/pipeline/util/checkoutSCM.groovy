package com.self.pipeline.util

def call(Map pipelineParams) 
{
env.SCM_URL= pipelineParams.GIT_URL+"/"+pipelineParams.GIT_GROUP+"/"+pipelineParams.REPO+".git"
env.BRANCH = pipelineParams.BRANCH
echo "Code checkout from SCM Repo"
sh '''
  	        	echo 'workspace cleanup'$PWD
		        rm -rf $WORKSPACE/*
   		        echo 'deleted'
		        git clone --single-branch --branch ${BRANCH} ${SCM_URL}
    ''' 
	 echo "Checkout is completed!"
}
