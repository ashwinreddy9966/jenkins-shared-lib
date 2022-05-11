def lintCheck() {
    sh "echo [[  INFO  ]] : Starting Lint Check for $COMPONENT"
    sh "echo [[  INFO  ]] : Lint Checks Completed"
}

def call() {
    pipeline {
        agent any
        environment { SONAR = credentials('SONAR') }
        environment { NEXUS = credentials('NEXUS') }
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
            stage('Preparing the Artifacts'){
                when {
                    expression { env.TAG_NAME != null }
                }
                steps {
                    sh "npm install && ls -ltr && ls -ltr  node_modules"
                    sh "zip ${COMPONENT}.zip node_modules server.js"
                    sh "ls -ltr"

                }
            }
            stage('Uploading the Artifacts'){
                when {
                    expression { env.TAG_NAME != null }
                }
                steps {
                    sh "curl -F -v -u ${NEXUS}:${NEXUS_PSW} --upload-file ${COMPONENT}.zip http://172.31.5.224:8081/repository/${COMPONENT}/${COMPONENT}.zip"
                }
            }
        } // end of stages
    }
}