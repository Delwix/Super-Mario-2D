pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                withAnt(installation:'ant'){
                    bat 'ant clean compile jar copy-media'
                }
            }
        }
        stage('test') {
            steps {
                bat 'echo this is a test'
            }
        }
    }
}
