def lintCheck() {
    sh "mvn checkstyle:check"
    sh "echo [[  INFO  ]] : Starting Lint Check for $COMPONENT"
    sh "echo [[  INFO  ]] : Lint Checks Completed"
}

def sonarCheck() {
    sh "sonar-scanner -Dsonar.host.url=http://172.31.8.247:9000 -Dsonar.projectKey=${COMPONENT} -Dsonar.sources=. -Dsonar.login=${SONAR_USR}  -Dsonar.password=${SONAR_PSW}"
    sh "/bin/sonar-quality-gate.sh ${SONAR_USR} ${SONAR_PSW} 172.31.8.247 ${COMPONENT}"
}


def call() {
    pipeline {
        agent any
        stages {
            stage('Lint Checks') {
                steps {
                    script { lintCheck() }
                }
            }
            stage('SonarScan') {
                steps {
                    script { sonarCheck() }
                }
            }
        } // end of stages
    }
}