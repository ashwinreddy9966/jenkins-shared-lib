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
            stage('Preparing the Artifactcs'){
                steps {
                    sh "echo Preparing the artifacts"
                }
            }
            stage('Uploading the Artifactcs'){
                steps {
                    sh "echo Uploading the artifacts"
                }
            }
        } // end of stages
    }
}