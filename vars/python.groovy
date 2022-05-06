def lintCheck() {
    sh "pip3 install pylint"
    sh "pylint payment.py"
    sh "echo [[  INFO  ]] : Starting Lint Check for $COMPONENT"
    sh "echo [[  INFO  ]] : Lint Checks Completed"
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
        } // end of stages
    }
}