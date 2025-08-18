pipeline {
    agent any

    tools {
        maven 'Maven3'   // имя Maven из Jenkins → Manage Jenkins → Tools
    }

    stages {
        stage('Checkout SCM') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean install -DskipTests'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
    }
}