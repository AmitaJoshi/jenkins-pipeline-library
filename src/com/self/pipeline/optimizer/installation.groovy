package com.self.pipeline.optimizer

def call(Map pipelineParams) 
{
env.ANSIBLE_REPO = pipelineParams.ANSIBLE_REPO
sh '''
  	    cd $ANSIBLE_REPO
        ansible-playbook playbook.yml -i inventory.txt -e "ENV=QA"
''' 
}  