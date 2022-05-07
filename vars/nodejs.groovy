def lintCheck() {
    sh "echo [[  INFO  ]] : Starting Lint Check for $COMPONENT"
    sh "echo [[  INFO  ]] : Lint Checks Completed"
}

def call() {
    pipeline {
        agent any
        environment { SONAR = credentials('SONAR') }
        stages {
            stage('Lint Checks') {
                steps {
                    script { lintCheck() }
                }
            }
            stage('SonarScan') {
                steps {
                    env.ARGS="-Dsonar.sources=."
                    script { common.sonarCheck() }
                }
            }

        } // end of stages
    }
}