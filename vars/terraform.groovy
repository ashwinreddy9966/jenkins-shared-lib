def call() {
    if(!env.TERRAFORM_DIR)
    {
        env.TERRAFORM_DIR = "./"
    }
    properties([
            parameters([
                    choice(choices: 'dev\nprod', description: "Select Environment", name: "ENV"),
            ]),
    ])
    node {
        ansiColor('xterm') {
            sh 'rm -rf *'
            git branch: 'main', url: "https://github.com/ashwinreddy9966/${REPONAME}"

            stage('Terraform Initialisization') {
                sh "pwd ; ls -ltr"
                sh "cd ${TERRAFORM_DIR}"
                sh "pwd ; ls -ltr"
                sh "terrafile -f  env-${ENV}/Terrafile"
                sh "terraform init -backend-config=env-${ENV}/${ENV}-backend.tfvars"
            }

            stage('Terraform Plan') {
                sh "cd ${TERRAFORM_DIR}"
                sh "terraform plan -var-file=env-${ENV}/${ENV}.tfvars"
            }

            stage('Terraform Apply') {
                sh "cd ${TERRAFORM_DIR}"
                sh "terraform apply -auto-approve -var-file=env-${ENV}/${ENV}.tfvars"
            }
        }
    }
}
