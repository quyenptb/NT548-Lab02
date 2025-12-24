pipeline {
    agent any

    // Khai báo công cụ đã cấu hình trong Jenkins -> Tools
    tools {
        maven 'Maven3' 
    }

    environment {
        // Biến môi trường kết nối
        SCANNER_HOME = tool 'sonar-scanner'
        
        // Cấu hình mạng Docker
        DOCKER_NETWORK = 'devops-net'
        
        // Địa chỉ các service trong mạng Docker
        KAFKA_HOST = 'kafka'
        KAFKA_PORT = '29092'  // Port container nội bộ
        EUREKA_HOST = 'service-discovery'
        //POSTGRES_HOST = 'host.docker.internal'
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
                    // Skip test for now
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo "--- Analyzing with SonarQube ---"
                withSonarQubeEnv('sonar-server') {
                    sh """
                    ${SCANNER_HOME}/bin/sonar-scanner \
                    -Dsonar.projectKey=ecommerce-microservices \
                    -Dsonar.projectName='Ecommerce Microservices' \
                    -Dsonar.sources=. \
                    -Dsonar.java.binaries=target/classes \
                    -Dsonar.exclusions=**/*.xml,**/*.html \
                    -Dsonar.host.url=http://sonarqube:9000 \
                    -Dsonar.login=$SONAR_AUTH_TOKEN
                    """
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                script {
                    echo "--- Building Docker Images ---"
                    // Build Service Discovery
                    sh 'docker build -t my-discovery:v1 ./service-discovery'
                    
                    // Build Order Service
                    sh 'docker build -t my-order:v1 ./order-service'
                }
            }
        }

        stage('Deploy Microservices') {
            steps {
                script {
                    echo "--- Deploying Containers ---"
                    
                    // 1. Rm old container
                    sh 'docker rm -f discovery order || true'

                    // 2. Run Service Discovery
                    // Map port 8761 để xem UI trên Windows
                    sh """
                        docker run -d --name discovery \\
                        --network ${DOCKER_NETWORK} \\
                        -p 8761:8761 \\
                        my-discovery:v1
                    """

                    echo "Waiting for Service Discovery to warm up..."
                    sleep 15 // Wait 15 for Euruka running

                    // 3. Run Order Service
                    // Override config in application.yml
                    sh """
                        docker run -d --name order \\
                        --network ${DOCKER_NETWORK} \\
                        -p 8082:8082 \\
                        -e KAFKA_HOST=${KAFKA_HOST} \\
                        -e KAFKA_PORT=${KAFKA_PORT} \\
                        -e EUREKA_HOST=${EUREKA_HOST} \\
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
            echo 'Pipeline failed!'
        }
    }
}
