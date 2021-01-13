pipeline {
  agent {
    label 'raptor-io-builder'
  }
  stages {
    stage('Build') {
      steps {
        sh 'mvn clean install'
      }
    }
  }
}
