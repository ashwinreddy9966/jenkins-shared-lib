def call() {
    node {
        sh 'rm -rf *'
        git branch: 'main', credentialsId: 'GitHub-Cred', url: "https://github.com/ashwinreddy9966/${COMPONENT}"
        env.APP_TYPE = "nodejs"
        common.lintCheck()
        env.ARGS="-Dsonar.sources=."
        common.sonarCheck()
        common.testCases()
        if(env.TAG_NAME != null) {
            common.artifacts()
        }
    }
}

