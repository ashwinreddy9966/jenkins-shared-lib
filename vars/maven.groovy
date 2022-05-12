def call() {
    node {
        env.APP_TYPE = "maven"
        common.lintCheck()
        env.ARGS="-Dsonar.sources=-Dsonar.java.binaries=target/"
        common.sonarCheck()
        common.testCases()
    }
}

//
//def call() {
//    pipeline {
//        agent any
//        environment { SONAR = credentials('SONAR') }
//        stages {
//            stage('Lint Checks') {
//                steps {
//                    script { lintCheck() }
//                }
//            }
//            stage('SonarScan') {
//                steps {
//                    script {
//                        env.ARGS="-Dsonar.java.binaries=target/"
//                        common.sonarCheck() }
//                    }
//                }
//            stage('Test Cases') {
//                parallel {
//                    stage('Unit Tests') {
//                            steps {
//                                sh "echo UNIT TESTS Completed"
//                            }
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