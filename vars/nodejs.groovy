def lintCheck() {
    sh "whoami"
    sh "pwd && ls -ltr"
    sh  "ls -ltr ~/node_modules/eslint/bin/eslint.js"
    sh "~/node_modules/eslint/bin/eslint.js *.js"
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