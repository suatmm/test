String getChangedFilesList() {
  changedFiles = []
  for (changeLogSet in currentBuild.changeSets) { 
      for (entry in changeLogSet.getItems()) { // for each commit in the detected changes
          for (file in entry.getAffectedFiles()) {
              changedFiles.add(file.getPath()) // add changed file to list
          }
      }
  }
  return changedFiles
}

println currentBuild.changeSets
println getChangedFilesList()

// def call(Map inputPipelineParams) {
//   pipelineParams = inputPipelineParams
  
//   properties([
//     buildDiscarder(logRotator(numToKeepStr: getNumberOfBuildsToStore())),
//     disableConcurrentBuilds(),
//     parameters(getParamsList())
//   ])

//   pipeline {
//     agent {
//       kubernetes {
//         label "${getJobName()}-${currentBuild.timeInMillis}"
//         defaultContainer "jnlp"
//         yaml libraryResource("jenkins-agents/${getAgentContainerName()}.yaml")
//       }
//     }

//     stages {
//       stage("Build with Packer") {
//         steps {
//           container(name: "${getAgentContainerName()}", shell: "/bin/bash") {
//             script {
              


//               sh """
//                 packer validate ${getPackerTemplate()} 
//               """
//               if (params.DEPLOY) {
//                 sh """
//                   packer build ${getPackerTemplatePath()} ${getPackerAdditionalData()}
//                 """
//               }
//             }
//           }
//         }
//       }
//     }
//   }
// }

// @groovy.transform.Field
// defaultParamsList = [
//   booleanParam(
//     name: 'DEPLOY',
//     defaultValue: false,
//     description: 'Deploy (dry-run by default)'),
//   string(
//     name: 'PACKER_OPTIONAL_DATA',
//     defaultValue: "",
//     description: 'Optional input data for helm upgrade command', )
// ]

// String getAgentContainerName() {
//   return pipelineParams.agentContainerName ?: 'packer'
// }

// String getJobName() {
//   String srcJobName = env.JOB_NAME
//   return srcJobName.replaceAll('/','-')
// }
// String getPackerTemplatePath() {
//   return pipelineParams.packerTemplatePath ?: 'packer.json'
// }

// String getPackerAdditionalData() {
//   return params.PACKER_OPTIONAL_DATA ?: (pipelineParams.packerAdditionalData ?: '')
// }

// String getNumberOfBuildsToStore() {
//   return pipelineParams.numberOfBuildsToStore ?: '3'
// }

// String getParamsList() {
//   def additionalParamsNames = pipelineParams.additionalParams.collect { 
//     param -> return param.getArguments().get('name') 
//   }
//   def defaultParamsListFiltered = defaultParamsList.findAll { 
//     param -> !(additionalParamsNames.contains(param.getArguments().get('name'))) 
//   }
//   return defaultParamsListFiltered + pipelineParams.additionalParams
// }