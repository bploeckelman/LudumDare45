#!groovy
pipeline {
    agent any

    triggers {
        pollSCM('H/15 * * * *') //polling for changes, here once a minute
    }

    stages {
        stage("Build") {
            steps {
                script {
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
                                                            execCommand: "run commands after copy?"
                                                    )
                                            ])
                            ])
                }
            }
        }

    }
}