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
        else ( env.APP_TYPE == "python" ) {
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
