def call() {
    node {
        sh 'rm -rf *'
        git branch: 'main', url: "https://github.com/ashwinreddy9966/${COMPONENT}"
        env.APP_TYPE = "python"
        sh "ls -ltr"
//        common.lintCheck()
        env.ARGS="-Dsonar.sources=."
        common.sonarCheck()
        common.testCases()
    }
}
//def call() {
//    pipeline {
//        agent any
//        environment { SONAR = credentials('SONAR') }
//        stages {
//            stage('Lint Checks') {
//                steps {
//                    script { common.lintCheck() }
//                }
//            }
//            stage('SonarScan') {
//                steps {
//                    script {
//                        env.ARGS="-Dsonar.sources=."
//                        common.sonarCheck() }
//                }
//            }
//            stage('Test Cases') {
//                parallel {
//                    stage('Unit Tests') {
//                        steps {
//                            sh "echo UNIT TESTS Completed"
//                        }
//
//                    }
//                    stage('Integration Tests') {
//                        steps {
//                            sh "echo INTEGRATION TESTS Completed"
//                        }
//                    }
//                    stage('Functional Tests') {
//                        steps {
//                            sh "echo FUNCTIONAL TESTS Completed"
//                        }
//                    }
//                }
//            }
//        } // end of stages
//    }
//}