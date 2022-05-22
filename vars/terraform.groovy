def call() {

    properties([
            parameters([
                    choice(choices: 'dev\nprod', description: "Select Environment", name: "ENV"),
            ]),
    ])
    node {
        sh 'rm -rf *'
        git branch: 'main', url: "https://github.com/ashwinreddy9966/${REPONAME}"

        stage('Terraform Init'){
            sh "cp env-${ENV}/Terrafile . ; terrafile"
            sh "terraform init -backend-config=env-${ENV}/${ENV}-backend.tfvars"

        }
        stage('Terraform Plan'){
            sh "terraform plan -var-file=env-${ENV}/${ENV}.tfvars"

        }
    }
}
