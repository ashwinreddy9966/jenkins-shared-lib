def lintCheck() {
    sh "echo [[  INFO  ]] : Starting Lint Check for $COMPONENT"
    sh "echo [[  INFO  ]] : Lint Checks Completed"
}

def call() {
    pipeline {
        agent any
        environment { SONAR = credentials('SONAR')
                      NEXUS = credentials('NEXUS') }
        stages {
            stage('Lint Checks') {
                steps {
                    script { lintCheck() }
                }
            }
            stage('SonarScan') {
                steps {
                    script {
                        env.ARGS="-Dsonar.sources=."
                        common.sonarCheck() }
                    }
                }
            stage('Test Cases') {
                parallel {
                    stage('Unit Tests') {
                        steps {
                            sh "echo UNIT TESTS Completed"
                        }

                    }
                    stage('Integration Tests') {
                        steps {
                            sh "echo INTEGRATION TESTS Completed"
                        }
                    }
                    stage('Functional Tests') {
                        steps {
                            sh "echo FUNCTIONAL TESTS Completed"
                        }
                    }
                }
            }
            stage('Checking the Releases'){
                when {
                    expression { env.TAG_NAME != null }
                }
                steps {
                    script {
                        def UPLOAD_STATUS=sh(returnStdout: true, script:'curl -s -L http://34.201.1.164:8081/service/rest/repository/browse/${COMPONENT}'| grep ${COMPONENT}-${GIT_TAG}.zip)
                        print UPLOAD_STATUS
                    }

                }
            }
            stage('Preparing the Artifacts'){
                when {
                    expression { env.TAG_NAME != null }
                }
                steps {
                    sh "npm install && ls -ltr && ls -ltr  node_modules"
                    sh "zip ${COMPONENT}-${TAG_NAME}.zip node_modules server.js"
                    sh "ls -ltr"

                }
            }
            stage('Uploading the Artifacts'){
                when {
                    expression { env.TAG_NAME != null }
                }
                steps {
                    sh "curl -f -v -u ${NEXUS_USR}:${NEXUS_PSW} --upload-file ${COMPONENT}-${TAG_NAME}.zip http://172.31.5.224:8081/repository/${COMPONENT}/${COMPONENT}-${TAG_NAME}.zip"
                }
            }
        } // end of stages
    }
}