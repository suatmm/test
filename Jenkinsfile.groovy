library 'jenkins-library@DAP-1517_terraform_mvp'

podTemplate(yaml: libraryResource("jenkins-agents/terraform.yaml")) {
  node(POD_LABEL) {
    
    properties([
      buildDiscarder(logRotator(numToKeepStr: "3")),
      disableConcurrentBuilds(),
      parameters([
        booleanParam(
          name: 'APPLY',
          defaultValue: false,
          description: 'Apply'),
        booleanParam(
          name: 'PLAN',
          defaultValue: false,
          description: 'PLAN')
      ])
    ])

    stage('TEST GIT CHANGES') {
      container(name:'terraform', shell: "/bin/bash") {
        def scmVars = checkout(scm)
        // templateToProcessPaths = getChangedTemplateFiles()
        println scmVars

        // withEnv([
        //   "OS_DOMAIN_NAME=${utils.getVaultSecret('secret/infra/selectel/terraform', 'OS_DOMAIN_NAME', 'terraform')}",
        //   "OS_PASSWORD=${utils.getVaultSecret('secret/infra/selectel/terraform', 'OS_PASSWORD', 'terraform')}",
        //   "OS_REGION_NAME=${utils.getVaultSecret('secret/infra/selectel/terraform', 'OS_REGION_NAME', 'terraform')}",
        //   "OS_USERNAME=${utils.getVaultSecret('secret/infra/selectel/terraform', 'OS_USERNAME', 'terraform')}",
        //   "OS_TENANT_ID=${utils.getVaultSecret('secret/infra/selectel/terraform', 'OS_TENANT_ID', 'terraform')}",
        //   "SEL_TOKEN=${utils.getVaultSecret('secret/infra/selectel/terraform', 'SEL_TOKEN', 'terraform')}",
        //   "TF_VAR_DNS_KEY_SECRET=${utils.getVaultSecret('secret/infra/selectel/terraform', 'TF_VAR_DNS_KEY_SECRET', 'terraform')}",
        //   "TF_VAR_OPENSTACK_PASSWORD=${utils.getVaultSecret('secret/infra/selectel/terraform', 'TF_VAR_OPENSTACK_PASSWORD', 'terraform')}",
        //   "TF_VAR_OPENSTACK_USERNAME=${utils.getVaultSecret('secret/infra/selectel/terraform', 'TF_VAR_OPENSTACK_USERNAME', 'terraform')}",
        //   // "TF_PLUGIN_CACHE_DIR=\$HOME/.terraform.d/"
        // ]) {
        //   templateToProcessPaths.each { file ->
        //     if (fileExists(file)) {
        //       sh """
        //         cd ${file.minus('terragrunt.hcl')}
        //         terragrunt init
        //         terragrunt plan              
        //       """
        //       if (env.GIT_BRANCH == 'develop') {
        //         sh """
        //           cd ${file.minus('terragrunt.hcl')}
        //           terragrunt apply -auto-approve
        //         """
        //       }
        //     }
        //   }
        // }
      }
    }
  }
}

@NonCPS
List getChangedTemplateFiles() {
  changedFiles = []
  println currentBuild.changeSets
  for (changeLogSet in currentBuild.changeSets) { 
    for (entry in changeLogSet.getItems()) {
      for (file in entry.getAffectedFiles()) {
        String filePath = file.getPath()
        if (filePath.startsWith('environments/project-a') && filePath.endsWith('terragrunt.hcl')) {
          changedFiles.add(file.getPath())
        }
      }
    }
  }
  return changedFiles
}

// String getPackerKeys(scmVars) {
//   if (env.GIT_BRANCH == "master") {
//     flavor = "fd9aad45-220d-43a4-94d8-013f1021dfe8"
//     image_name_postfix = ""
//     networks = "faeaec0c-2dc7-4b30-8f40-3ca6838160cd"
//     tenant_id = "f170527a070e458ca71b1e7bfc882000"
//   } else {
//     flavor = "faf056f7-128e-43b5-a43b-372debdf71b6"
//     image_name_postfix = "-${scmVars.GIT_BRANCH}-${scmVars.GIT_COMMIT[0..7]}"
//     networks = "80d6604f-3425-49ff-a76a-c3ebf43bca83"
//     tenant_id = "79ba7bbe72d2441a8d2c7da52587fd96"
//   }
  
//   availability_zone = "ru-1a"
//   region = "ru-1"
//   volume_type = "basic.ru-1a"  
  
//   return "-var 'flavor=${flavor}' \
//     -var 'image_name_postfix=${image_name_postfix}' \
//     -var 'networks=${networks}' \
//     -var 'tenant_id=${tenant_id}' \
//     -var 'availability_zone=${availability_zone}' \
//     -var 'region=${region}' \
//     -var 'volume_type=${volume_type}'"
// }
