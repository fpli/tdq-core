def slack_channel = "sojourner-dev"
pipeline {
  agent {
    label 'raptor-io-builder'
  }
  stages {
    stage('Build') {
      steps {
        slackSend(channel: slack_channel, message: "<${BUILD_URL}|${JOB_NAME} #${BUILD_NUMBER}>: Started to build...")
        sh './scripts/generate_build_num.sh'
        sh 'mvn clean test verify package sonar:sonar'
      }
    }
    stage('Deploy') {
      steps {
        sh './scripts/upload_rheos_portal.sh rt-pipeline'
        sh './scripts/upload_rheos_portal.sh dumper'
        sh './scripts/upload_rheos_portal.sh distributor'
      }
    }
  }
  post {
    success {
      slackSend(channel: slack_channel, color: "good", message: "<${BUILD_URL}|${JOB_NAME} #${BUILD_NUMBER}>: :beer: Success after ${currentBuild.durationString.replace(' and counting', '')}\n job version *${readFile('build_version_tmp.txt').trim()}* has been uploaded to Rheos portal")
    }
    failure {
      slackSend(channel: slack_channel, color: "danger", message: "<${BUILD_URL}|${JOB_NAME} #${BUILD_NUMBER}>: :alert: Failure after ${currentBuild.durationString.replace(' and counting', '')}")
    }
    aborted {
      slackSend(channel: slack_channel, color: "warning", message: "<${BUILD_URL}|${JOB_NAME} #${BUILD_NUMBER}>: :warning: Aborted after ${currentBuild.durationString.replace(' and counting', '')}")
    }
    unstable {
      slackSend(channel: slack_channel, color: "warning", message: "<${BUILD_URL}|${JOB_NAME} #${BUILD_NUMBER}>: :warning: Unstable after ${currentBuild.durationString.replace(' and counting', '')}")
    }
  }
}
