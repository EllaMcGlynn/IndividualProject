pipeline {
    agent any
 
    environment {
        SONAR_TOKEN = credentials('AnyDo') // Matches Jenkins credentials ID
    }
 
    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/EllaMcGlynn/IndividualProject', branch: 'dev'
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
                        mvn clean verify sonar:sonar \
                        -Dsonar.projectKey=AnyDO \
                        -Dsonar.projectName="AnyDO" \
                        -Dsonar.host.url=http://sonarqube:9000 \
                        -Dsonar.token=${SONAR_TOKEN}
                    '''
                
            }
        }
 
        stage('Docker Build') {
            steps {
                sh 'docker build -t AnyDO:latest'
            }
        }
    }
}
