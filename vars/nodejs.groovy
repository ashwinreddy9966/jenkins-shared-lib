def lintCheck() {
    sh "echo [[  INFO  ]] : Starting Lint Check for $COMPONENT"
    sh "echo [[  INFO  ]] : Lint Checks Completed"
}

def sonarCheck() {
    sh "sonar-scanner -Dsonar.host.url=http://172.31.8.247:9000"
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