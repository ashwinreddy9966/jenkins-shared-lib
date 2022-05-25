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
                sh "cd ${TERRAFROM_DIR}"
                sh "cp env-${ENV}/Terrafile . ; terrafile"
                sh "terraform init -backend-config=env-${ENV}/${ENV}-backend.tfvars"
            }

            stage('Terraform Plan') {
                sh "cd ${TERRAFROM_DIR}"
                sh "terraform plan -var-file=env-${ENV}/${ENV}.tfvars"
            }

            stage('Terraform Apply') {
                sh "cd ${TERRAFROM_DIR}"
                sh "terraform apply -auto-approve -var-file=env-${ENV}/${ENV}.tfvars"
            }
        }
    }
}
