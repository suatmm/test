library 'jenkins-library@DAP-1517_terraform_mvp'

podTemplate(yaml: libraryResource("jenkins-agents/terraform.yaml")) {
  node(POD_LABEL) {
    
    properties([
      buildDiscarder(logRotator(numToKeepStr: "3")),
      disableConcurrentBuilds(),
    ])

    stage('TEST GIT CHANGES') {
      container(name:'terraform', shell: "/bin/bash") {
        def scmVars = checkout(scm)
        sh """
          git diff --name-only HEAD^ HEAD
          git diff --name-only --diff-filter=D HEAD^ HEAD
        """
      }
    }
  }
}