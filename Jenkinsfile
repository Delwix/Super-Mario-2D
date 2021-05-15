pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                withAnt(installation:'ant'){
                    bat 'ant compile'
                }
            }
        }
        stage('Test') {
            steps {
                bat 'ant unit-tests'
            }
        }
        stage('Package') {
            steps {
                bat 'ant package'
            }
        }
    }
}
