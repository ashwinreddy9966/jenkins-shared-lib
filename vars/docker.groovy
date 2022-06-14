def call() {
    node {
        sh 'rm -rf *'
        git branch: 'main', credentialsId: 'GitHub-Cred', url: "https://github.com/ashwinreddy9966/${COMPONENT}"

            stage('Docker Build') {
                sh "docker build -t 834725375088.dkr.ecr.us-east-1.amazonaws.com/${COMPONENT}:latest ."
            }
        if(env.TAG_NAME != null) {
            stage('Docker Push') {

                sh "docker tag 834725375088.dkr.ecr.us-east-1.amazonaws.com/${COMPONENT}:latest 834725375088.dkr.ecr.us-east-1.amazonaws.com/${COMPONENT}:${TAG_NAME}"
                sh '''docker login -u AWS https://834725375088.dkr.ecr.us-east-1.amazonaws.com -p $(aws ecr get-login-password --region us-east-1)'''
                sh "docker push 834725375088.dkr.ecr.us-east-1.amazonaws.com/${COMPONENT}:${TAG_NAME}"
            }
        }
    }
}

