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

        stage('Package JAR') {
            steps {
                sh 'mvn package -DskipTests'
                sh 'cp target/*.jar target/app.jar' // Ensures a consistent JAR filename
            }
        }
 
        stage('Test') {
            steps {
                sh 'mvn verify jacoco:report'
            }
        }
 
        stage('SonarQube Analysis') {
            steps {
                sh '''
                    mvn sonar:sonar \
                      -Dsonar.projectKey=AnyDo \
                      -Dsonar.projectName='AnyDo' \
                      -Dsonar.host.url=http://Sonarqube-individual:9000 \
                      -Dsonar.token=$SONAR_TOKEN
                '''
            }
        }
    }
}
