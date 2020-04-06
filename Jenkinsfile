#!groovy
pipeline {
    agent any

    triggers {
        pollSCM('H/10 * * * *') //polling for changes, here once a minute
    }

    stages {
        stage('Checkout') {
            steps {
                node {
                    checkout scm
                }
            }
        }
        stage("Build") {
            steps {
                script {
                    sh './gradlew clean'
                    sh './gradlew desktop:sprites'
                    sh './gradlew html:dist'
                }
            }
        }

    }
}