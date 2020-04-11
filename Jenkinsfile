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
                    env.GIT_REPO_NAME = env.GIT_URL.replaceFirst(/^.*\/([^\/]+?).git$/, '$1')
                    env.REMOTE_DIR =  "inthelifeofdoug.com/LudumDareBuilds/${env.GIT_REPO_NAME}"
                    mqttNotification brokerUrl: 'tcp://home.inthelifeofdoug.com:1883',
                            credentialsId: 'mqttcreds',
                            message: "{\"BuildNumber\": \"${BUILD_NUMBER}\", \"Status\": \"Building\", \"Title\": \"${env.GIT_REPO_NAME}\"}",
                            qos: '2',
                            topic: "jenkins/${env.GIT_REPO_NAME}"

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
                                                            remoteDirectory: "${env.REMOTE_DIR}",
                                                    )
                                            ])
                            ])
                }
            }
        }
        stage("Notify"){
            steps{
                script{
                    mqttNotification brokerUrl: 'tcp://home.inthelifeofdoug.com:1883',
                            credentialsId: 'mqttcreds',
                            message: "{Build # ${BUILD_NUMBER} : ${BUILD_RESULT}  See it here http://${env.REMOTE_DIR}",
                            qos: '2',
                            topic: "jenkins/${env.GIT_REPO_NAME}"
                }
            }
        }

    }
}