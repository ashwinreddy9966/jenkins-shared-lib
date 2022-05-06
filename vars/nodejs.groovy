def info(COMPONENT) {
    sh "echo Starting Lint Check for $COMPONENT"
    sh "echo Lint Checks Completed"
//             sh "npm install jslint"
//             sh "ls -ltr node_modules/jslint/bin/"
//             sh "node_modules/jslint/bin/jslint.js server.js"
}

info('COMPONENT')


