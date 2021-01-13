pipeline {
  agent {
    label 'raptor-io-builder'
  }
  stages {
    stage('Build') {
      steps {
        sh 'mvn clean test verify package'
      }
    }
    stage('Deploy') {
      steps {
        sh './scripts/generate_build_num.sh'
        sh './scripts/upload_rheos_portal.sh rt-pipeline'
        sh './scripts/upload_rheos_portal.sh dumper'
        sh './scripts/upload_rheos_portal.sh distributor'
      }
    }
  }
}
