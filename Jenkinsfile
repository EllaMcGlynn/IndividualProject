pipeline {
    agent any
 
    environment {
        SONAR_TOKEN = credentials('AnyDo') // Must be set in Jenkins credentials
    }
 
    tools {
        maven 'maven' // Name must match Maven tool installed via Jenkins -> Global Tool Config
    }
 
    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/EllaMcGlynn/IndividualProject.git', branch: 'dev'
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
 
        stage('SonarQube Analysis') {
            steps {
                sh '''
                    mvn sonar:sonar \
                    -Dsonar.projectKey=AnyDO \
                    -Dsonar.projectName="AnyDO" \
                    -Dsonar.host.url=http://sonarqube:9000 \
                    -Dsonar.token=${SONAR_TOKEN}
                '''
            }
        }
 
        stage('Docker Build') {
            steps {
                sh 'docker build -t anydo:latest .'
            }
        }
    }
}
