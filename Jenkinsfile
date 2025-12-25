pipeline {
    agent any

    tools {
        maven 'Maven3' 
    }

    environment {
        SCANNER_HOME = tool 'sonar-scanner'
        
        DOCKER_NETWORK = 'devops-net'
        

        KAFKA_HOST = 'kafka'
        KAFKA_PORT = '29092'
        EUREKA_HOST = 'discovery'
         
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test (Maven)') {
            steps {
                script {
                    echo "--- Building Project with Maven ---"
                    sh 'cd ecommerce-system && mvn clean package -DskipTests'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo "--- Analyzing with SonarQube ---"
                script {
                    sh 'getent hosts sonarqube || echo "Cannot find host sonarqube"'
                }
                withSonarQubeEnv('sonar-server') {
                    sh """
                    ${SCANNER_HOME}/bin/sonar-scanner \
                    -Dsonar.projectKey=ecommerce-microservices \
                    -Dsonar.projectName='Ecommerce Microservices' \
                    -Dsonar.projectBaseDir=ecommerce-system \
                    -Dsonar.sources=. \
                    -Dsonar.java.binaries=. \
                    -Dsonar.exclusions=**/src/test/**,**/target/**,**/*.xml,**/*.html
                    """
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                script {
                    echo "--- Building Docker Images ---"
                    sh 'docker build -t my-discovery:v1 ./ecommerce-system/service-discovery'
                    sh 'docker build -t my-order:v1 ./ecommerce-system/order-service'
                }
            }
        }

        stage('Deploy Microservices') {
            steps {
                script {
                    echo "--- Deploying Containers ---"
                    
                    // Delete old container if any
                    sh 'docker rm -f discovery order || true'

                    // 1. Run Service Discovery
                    sh """
                        docker run -d --name discovery \
                        --network ${DOCKER_NETWORK} \
                        -p 8761:8761 \
                        my-discovery:v1
                    """

                    echo "Waiting 15s for Service Discovery to warm up..."
                    sleep 15 

                    // 2. Run Order Service
                    // --restart unless-stopped giúp nó tự chạy lại nếu crash
                    sh """
                        docker run -d --name order \
                        --network ${DOCKER_NETWORK} \
                        --restart unless-stopped \
                        -p 8082:8082 \
                        -e KAFKA_HOST=${KAFKA_HOST} \
                        -e KAFKA_PORT=${KAFKA_PORT} \
                        -e EUREKA_HOST=${EUREKA_HOST} \
                        my-order:v1
                    """
                }
            }
        }
    }
    
    post {
        success {
            echo 'Pipeline executed successfully!'
        }
        failure {
            echo 'Pipeline failed! Check logs.'
        }
    }
}