def sonarCheck() {
    sh "sonar-scanner -Dsonar.host.url=http://172.31.8.247:9000 -Dsonar.projectKey=${COMPONENT} -Dsonar.sources=. -Dsonar.login=${SONAR_USR}  -Dsonar.password=${SONAR_PSW}"
    sh "sleep 10"
   // sh "/bin/sonar-quality-gate.sh ${SONAR_USR} ${SONAR_PSW} 172.31.8.247 ${COMPONENT}"
}

