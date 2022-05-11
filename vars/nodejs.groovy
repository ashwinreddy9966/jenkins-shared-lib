env.APP_TYPE = "nodejs"

def call() {
    node {
        common.lintCheck()
        env.ARGS="-Dsonar.sources=."
        common.sonarCheck()
        common.testCases()
        }
    }



def call() {
    pipeline {
        agent any
        environment { SONAR = credentials('SONAR')
                      NEXUS = credentials('NEXUS') }
        stages {
            stage('Lint Checks') {
                steps {
                    script { common.lintCheck() }
                }
            }
            stage('SonarScan') {
                steps {
                    script {
                        env.ARGS="-Dsonar.sources=."
                        common.sonarCheck() }
                    }
                }

            stage('Checking the Releases'){
                when {
                    expression { env.TAG_NAME != null }
                }
                steps {
                    script {
                        env.UPLOAD_STATUS=sh(returnStdout: true, script:'curl -s -L http://34.201.1.164:8081/service/rest/repository/browse/${COMPONENT}| grep ${COMPONENT}-${TAG_NAME}.zip || true' )
                        print UPLOAD_STATUS
                    }
                }
            }
            stage('Preparing the Artifacts'){
                when {
                    expression { env.TAG_NAME != null }
                    expression { env.UPLOAD_STATUS == "" }
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
                    expression { env.UPLOAD_STATUS == "" }
                }
                steps {
                    sh "curl -f -v -u ${NEXUS_USR}:${NEXUS_PSW} --upload-file ${COMPONENT}-${TAG_NAME}.zip http://172.31.5.224:8081/repository/${COMPONENT}/${COMPONENT}-${TAG_NAME}.zip"
                }
            }
        } // end of stages
    }
}