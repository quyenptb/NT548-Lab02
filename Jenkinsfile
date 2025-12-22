pipeline {
    agent any
    
    environment {
        // Khớp với tên đã đặt trong Global Tool Configuration
        SCANNER_HOME = tool 'sonar-scanner' 
    }

    stages {
        stage('Checkout') {
            steps {
                // Thay link git của cậu vào đây nếu Jenkins không tự nhận
                checkout scm
            }
        }

        stage('SonarQube Analysis') {
            steps {
                // Khớp với tên trong Configure System
                withSonarQubeEnv('sonar-server') { 
                    sh """
                    ${SCANNER_HOME}/bin/sonar-scanner \
                    -Dsonar.projectKey=my-microservice \
                    -Dsonar.sources=./app \
                    -Dsonar.host.url=http://sonarqube:9000 \
                    -Dsonar.login=$SONAR_AUTH_TOKEN
                    """
                }
            }
        }

        stage('Build & Deploy Docker') {
            steps {
                script {
                    // Build Image
                    sh 'docker build -t my-microservice:v1 ./app'
                    
                    // Xóa container cũ nếu tồn tại (để chạy lại không lỗi)
                    sh 'docker rm -f lab02-app || true'
                    
                    // Deploy container mới (map port 3000)
                    sh 'docker run -d -p 3000:3000 --name lab02-app my-microservice:v1'
                }
            }
        }
    }
}
