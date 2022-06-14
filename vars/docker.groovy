def call() {
    node {
        sh 'rm -rf *'
        git branch: 'main', credentialsId: 'GitHub-Cred', url: "https://github.com/ashwinreddy9966/${COMPONENT}"

        stage('Docker Build') {
            sh "ls -ltr && pwd && whoami && ifconfig"
            sh "docker build . "
        }
    }
}

