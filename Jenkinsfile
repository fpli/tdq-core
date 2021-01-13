pipeline {
  agent {
    label 'raptor-builder'
  }
  stages {
    stage('Build') {
      steps {
        sh 'mvn clean install'
      }
    }
  }
}
