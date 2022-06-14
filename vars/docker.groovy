def call() {
    node {
        sh 'rm -rf *'
        git branch: 'main', credentialsId: 'GitHub-Cred', url: "https://github.com/ashwinreddy9966/${COMPONENT}"

        stage('Docker Build') {
            sh "docker build -t 834725375088.dkr.ecr.us-east-1.amazonaws.com/${COMPONENT}:latest ."
        }
    }
}

