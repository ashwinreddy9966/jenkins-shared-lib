def call() {
    node {
        sh 'rm -rf *'
        git branch: 'main', credentialsId: 'GitHub-Cred', url: 'https://github.com/roboshop-devops-project/${COMPONENT}'
        env.APP_TYPE = "maven"
        common.lintCheck()
        env.ARGS="Dsonar.java.binaries=target/"
        common.sonarCheck()
        common.testCases()
        if(env.TAG_NAME != null) {
            common.artifacts()
        }
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