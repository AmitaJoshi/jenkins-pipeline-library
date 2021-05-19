def call(Map pipelineParams) {
     pipeline
       {
         node(pipelineParams.BUILD_NODE)
	      {
          stage("Code Checkout") 
	        {
            git 'https://github.com/minaxijoshi3101/dummy_webapp.git'
          }
          stage("compile package")
          {
            def mvnHome = tool name: 'maven-3', type: 'maven'
            sh "${mvnHome}/bin/mvn package"
          }
           stage("SonarQube Analysis")
          {
            def mvnHome = tool name: 'maven-3', type: 'maven'
            withSonarQubeEnv('sonarQube')
            {
            sh "${mvnHome}/bin/mvn sonar:sonar"
            }
          }
        }
      }
}