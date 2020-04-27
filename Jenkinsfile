pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        sh 'mvn clean package -DskipTests -Pprod'
      }
    }

    stage('Upload Jar') {
      steps {
        sh '''cd ./rt-pipeline;
mvn job-uploader:upload -Dusername=2ec39f180f6448868967e6e661bb87ef  -Dpassword=e02KpbXKIyBQliBn2NDwFKv3GgrcvDs8kaAp4MnLRrBY1u1vwJYcY7ciN5Mq6htX  -Dnamespace=sojourner-ubd'''
      }
    }

  }
}