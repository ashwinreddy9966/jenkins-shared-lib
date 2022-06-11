def call() {
    if(!env.TERRAFORM_DIR)
    {
        env.TERRAFORM_DIR = "./"
    }
    properties([
            parameters([
                    choice(choices: 'dev\nprod', description: "Select Environment", name: "ENV"),
                    string(choices: 'APP_VERSION',description: 'Choose App Version To Deploy : Ignore this VPC and DB', name: "APP_VERSION")
            ]),
    ])
    node {
        ansiColor('xterm') {
            git branch: 'main', url: "https://github.com/ashwinreddy9966/${REPONAME}"

            stage('Terraform Initialisization') {
                sh '''
                echo $TERRAFORM_DIR
                cd ${TERRAFORM_DIR}
                pwd ; ls -ltr
                terrafile -f  env-${ENV}/Terrafile
                terraform init -backend-config=env-${ENV}/${ENV}-backend.tfvars -reconfigure
                '''
            }
            stage('Terraform Plan') {
                sh '''
                cd ${TERRAFORM_DIR}
                export TF_VAR_APP_VERSION=${APP_VERSION}
                terraform plan -var-file=env-${ENV}/${ENV}.tfvars
                '''
            }

            stage('Terraform Apply') {
                sh '''
                cd ${TERRAFORM_DIR}
                export TF_VAR_APP_VERSION=${APP_VERSION}
                terraform apply -auto-approve -var-file=env-${ENV}/${ENV}.tfvars
                '''
            }
        }
    }
}
