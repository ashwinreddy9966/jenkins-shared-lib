def lintCheck() {
    stage('Lint Checks') {
        if( env.APP_TYPE == "nodejs" ) {
                // sh "~/node_modules/bin/jslinst.js server.js"
                sh "echo [[  INFO  ]] : Starting Lint Check for $COMPONENT"
                sh "echo [[  INFO  ]] : Lint Checks Completed"
        }
        else if( env.APP_TYPE == "maven" ) {
            //  sh "mvn checkstyle:check"
            sh "echo [[  INFO  ]] : Starting Lint Check for $COMPONENT"
            sh "echo [[  INFO  ]] : Lint Checks Completed"
        }
        else if( env.APP_TYPE == "golang" ) {
            sh "echo [[  INFO  ]] : Starting Lint Check for $COMPONENT"
            sh "echo [[  INFO  ]] : Lint Checks Completed"
        }
        else if ( env.APP_TYPE == "python" ) {
            // sh "pylint *.py"
            sh "echo [[  INFO  ]] : Starting Lint Check for $COMPONENT"
            sh "echo [[  INFO  ]] : Lint Checks Completed"
        }
    }
}


def sonarCheck() {
    stage('Sonar Checks') {
        sh "echo Sonar Scan Completed and Qualified for ${COMPONENT}"
        // sh "sonar-scanner -Dsonar.host.url=http://172.31.8.247:9000 -Dsonar.projectKey=${COMPONENT} -Dsonar.login=${SONAR_USR}  -Dsonar.password=${SONAR_PSW} ${ARGS}"
        // sh "/bin/sonar-quality-gate.sh ${SONAR_USR} ${SONAR_PSW} 172.31.8.247 ${COMPONENT}"
    }
}

// Let's do that in parallel stages in groovy approach
// Defining a empty array and passing it and parallel(stages), telling that to run in parallel
def testCases() {
    stage('TestCases') {
        def stages = [:]

        stages["UnitTests"] = {
            sh "echo UNIT TESTS Completed"
        }
        stages["IntegrationTests"] = {
            sh "echo INTEGRATION TESTS Completed"
        }
        stages["FunctionalTests"] = {
            sh "echo FUNCTIONAL TESTS Completed"
        }
        parallel(stages)
    }
}

def artifacts() {
    stage('Checking the Releases') {
        env.UPLOAD_STATUS = sh(returnStdout: true, script: 'curl -s -L http://172.31.5.224:8081/service/rest/repository/browse/${COMPONENT}| grep ${COMPONENT}-${TAG_NAME}.zip || true')
        print UPLOAD_STATUS
    }
    if (env.UPLOAD_STATUS == "") {

        stage('Preparing the Artifacts') {
            if (env.APP_TYPE == "nodejs") {
                sh "npm install && ls -ltr && ls -ltr ~/node_modules"
                sh "zip ${COMPONENT}-${TAG_NAME}.zip ~/node_modules ~/server.js"
                sh "ls -ltr && pwd"
            } else if (env.APP_TYPE == "maven") {
                sh ''' 
                        mvn clean package 
                        mv target/${COMPONENT}-1.0.jar ${COMPONENT}.jar
                        zip -r ${COMPONENT}-${TAG_NAME}.zip ${COMPONENT}.jar
                   '''
            } else if (env.APP_TYPE == "python") {
                sh "zip ${COMPONENT}-${TAG_NAME}.zip *.py *.ini requirements.txt"
            } else if (env.APP_TYPE == "golang") {
                sh '''
                  ls -ltr                  
                  go mod init ${COMPONENT}
                  go get 
                  go build
                  zip -r ${COMPONENT}-${TAG_NAME}.zip ${COMPONENT}
                '''
            }
        }
            stage('Uploading the Artifacts') {
                withCredentials([usernamePassword(credentialsId: 'NEXUS', passwordVariable: 'NEXUS_PSW', usernameVariable: 'NEXUS_USR')]) {
                    sh "curl -f -v -u ${NEXUS_USR}:${NEXUS_PSW} --upload-file ${COMPONENT}-${TAG_NAME}.zip http://172.31.5.224:8081/repository/${COMPONENT}/${COMPONENT}-${TAG_NAME}.zip"
                }
            }
        }
    }
