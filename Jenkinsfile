pipeline {
    agent any

    tools {
        // Install the Maven version configured as "M3" and add it to the path.
        maven "Maven 3.8.6"
    }

    stages {
        stage('Build Jar file') {
            steps {
                // Get some code from a GitHub repository
                // git 'https://github.com/jglick/simple-maven-project-with-tests.git'

                // Run Maven on a Unix agent.
                // sh "mvn -Dmaven.test.failure.ignore=true clean package"

                // To run Maven on a Windows agent, use
                bat "mvn -Dmaven.test.failure.ignore=true clean package"
            }

            post {
                // If Maven was able to run the tests, even if some of the test
                // failed, record the test results and archive the jar file.
                success {
                    //junit '**/target/surefire-reports/TEST-*.xml'
                    archiveArtifacts 'target/*.jar'
                    //bat 'java -jar -Dspring.profiles.active=jenkins target/spring-boot-complete-0.0.1-SNAPSHOT.jar'
                }
            }
        }
        
        stage('Build Docker image') {
            steps {
                bat 'docker build -t spring-boot-image .'
                bat 'docker run -p 8080:8080 -p 6379:6379 spring-boot-image'
            }
        }
        
    }
}
