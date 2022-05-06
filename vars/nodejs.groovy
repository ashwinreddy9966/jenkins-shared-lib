def info(COMPONENT) {
    sh "echo [INFO] : Starting Lint Check for $COMPONENT"
    sh "echo [INFO] : Lint Checks Completed"
//             sh "npm install jslint"
//             sh "ls -ltr node_modules/jslint/bin/"
//             sh "node_modules/jslint/bin/jslint.js server.js"
}

info('COMPONENT')


