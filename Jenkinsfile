#!groovy
pipeline {
    agent any

    triggers {
        pollSCM('H/15 * * * *') //polling for changes, here every 15 min
    }

    stages {
        stage("Build") {
            steps {
                script {
                    mqttNotification brokerUrl: 'tcp://home.inthelifeofdoug.com:1883', credentialsId: 'mqttcreds', message: 'Starting Build', qos: '2', topic: 'jenkins/LD45'

                    env.GIT_REPO_NAME = env.GIT_URL.replaceFirst(/^.*\/([^\/]+?).git$/, '$1')
                    sh './gradlew clean'
                    sh './gradlew desktop:sprites'
                    sh './gradlew html:dist'
                }
            }
        }
        stage("UploadSSH") {
            steps{
                script {
                    sshPublisher(
                            publishers: [
                                    sshPublisherDesc(
                                            configName: "wxpick",
                                            verbose: true,
                                            transfers: [
                                                    sshTransfer(
                                                            sourceFiles: "html/build/dist/**",
                                                            removePrefix: "html/build/dist/",
                                                            remoteDirectory: "inthelifeofdoug.com/LudumDareBuilds/${env.GIT_REPO_NAME}",
                                                    )
                                            ])
                            ])
                }
            }
        }
        stage("Notify"){
            steps{
                script{
                    mqttNotification brokerUrl: 'https://home.inthelifeofdoug.com:1883', credentialsId: 'mqttcreds', message: 'A Test', qos: '2', topic: 'jenkins/LD45'
                }
            }
        }

    }
}