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
        def filesDeleted = "git diff --name-only --diff-filter=D HEAD^ HEAD"
        def filesCreatedOrUpdated = "git diff --name-only --diff-filter=ACMR HEAD^ HEAD"
        def filesDeletedResult = sh( returnStdout: true, script: filesDeleted )
        def filesCreatedOrUpdatedResult = sh( returnStdout: true, script: filesCreatedOrUpdated )
        println "${filesDeletedResult}"
        println "${filesCreatedOrUpdatedResult}"
        // if( errors.trim() ){
        //     println "errors found: ${errors}"
        // }
        // sh """
        //   git diff --name-only HEAD^ HEAD
          
        // """
      }
    }
  }
}