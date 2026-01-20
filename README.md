NT548 - BÀI TẬP THỰC HÀNH 02
============================

**Quản lý và triển khai hạ tầng AWS và ứng dụng microservices với Terraform, CloudFormation, GitHub Actions, AWS CodePipeline và Jenkins**

Thông tin sinh viên
-------------------

*   **Nhóm sinh viên:** Phan Thị Bích Quyên (22521224), Trần Ngọc Long (22520822), Nguyễn Văn Nam (22520922)
    
*   **Môn học:** Công nghệ DevOps và Ứng dụng

*   **Giảng viên:** ThS Lê Anh Tuấn
    

MỤC LỤC
-------

1.  [Phần 1: Terraform & GitHub Actions](https://markdownlivepreview.com/#phần-1-terraform--github-actions)
    
2.  [Phần 2: CloudFormation & AWS CodePipeline](https://markdownlivepreview.com/#phần-2-cloudformation--aws-codepipeline)
    
3.  [Phần 3: Jenkins CI/CD cho Microservices](https://markdownlivepreview.com/#phần-3-jenkins-cicd-cho-microservices)
    

PHẦN 1: TERRAFORM & GITHUB ACTIONS
----------------------------------

Triển khai hạ tầng AWS (VPC, EC2, Network) tự động hóa bằng GitHub Actions và tích hợp **Checkov** để kiểm tra bảo mật.

### 1\. Cài đặt môi trường & Cấu hình

1.  **AWS Account:**
    
    *   Đảm bảo có tài khoản AWS với Access Key có quyền tạo resources (EC2, VPC).
        
2.  **GitHub Repository:**
    
    *   Code Terraform: /terraform
        
    *   Workflow: .github/workflows/terraform.yml
        
3.  **GitHub Secrets:**
    
    *   Vào Settings -> Secrets and variables -> Actions -> New Repository Secret
        
    *   Thêm:
        
        *   AWS\_ACCESS\_KEY\_ID: Access Key ID
            
        *   AWS\_SECRET\_ACCESS\_KEY: Secret Access Key
            

### 2\. Cách chạy (Triển khai)

1.  Thay đổi code trong thư mục /terraform (nếu cần).
    
2.  git add .git commit -m "Update infrastructure"git push origin main
    

GitHub Actions sẽ tự động chạy workflow:

1.  **Checkov:** Quét lỗi bảo mật.
    
2.  **Terraform Init & Plan:** Lên kế hoạch triển khai.
    
3.  **Terraform Apply:** Tạo hạ tầng trên AWS.
    

### 3\. Kiểm tra kết quả

*   **GitHub Actions:** Tab **Actions** hiển thị tick xanh cho tất cả steps.
    
*   **AWS Console:**
    
    *   EC2 Dashboard: Có instance Lab02-EC2-Terraform.
        
    *   VPC Dashboard: Có VPC Lab02-VPC cùng Subnet/Route Table.
        

PHẦN 2: CLOUDFORMATION & AWS CODEPIPELINE
-----------------------------------------

Triển khai hạ tầng AWS dùng **CloudFormation**, tích hợp **AWS CodeBuild** (linting) và **CodePipeline** (CD).

### 1\. Cài đặt môi trường

1.  **Source Code:**
    
    *   Template: cloudformation/template.yaml
        
    *   Buildspec: buildspec.yml
        
2.  **AWS CodeBuild:**
    
    *   Project: Lab02-Build
        
    *   Source: GitHub Repo
        
    *   Environment: Ubuntu (Latest), chọn _Use a buildspec file_
        
3.  **AWS CodePipeline:**
    
    *   Pipeline: Lab02-Pipeline
        
    *   Source: GitHub (nhánh main)
        
    *   Build: Project Lab02-Build
        
    *   Deploy: CloudFormation
        
        *   Action mode: Create/Update stack
            
        *   Stack name: Lab02-CFN-Stack
            
        *   Template artifact: BuildArtifact
            
        *   File: cloudformation/template.yaml
            

### 2\. Cách chạy

1.  Push code lên nhánh main.
    
2.  AWS CodePipeline tự động chạy:
    
    *   **Source:** Lấy code mới nhất.
        
    *   **Build:** Chạy cfn-lint.
        
    *   **Deploy:** CloudFormation tạo/update stack.
        

### 3\. Kiểm tra kết quả

*   **AWS CodePipeline Console:** 3 giai đoạn (Source, Build, Deploy) đều **Succeeded**.
    
*   **AWS CloudFormation Console:** Stack Lab02-CFN-Stack ở trạng thái CREATE\_COMPLETE hoặc UPDATE\_COMPLETE.
    
*   **AWS EC2 Console:** Có instance Lab02-CFN-Instance.
    

PHẦN 3: JENKINS CI/CD CHO MICROSERVICES
---------------------------------------

Xây dựng hệ thống CI/CD với Jenkins chạy trong Docker, tích hợp **SonarQube**, **Kafka** và triển khai **Microservices (Eureka, Order Service)**.

### 1\. Cài đặt môi trường (Local/VM)

Yêu cầu: Máy đã cài **Docker** và **Docker Compose**.

#### Bước 1: Khởi chạy hạ tầng



`   docker-compose -f docker-compose-infra.yml up -d   `

#### Bước 2: Cấu hình SonarQube (http://localhost:9000)

*   Login: admin/admin (đổi mật khẩu nếu được hỏi).
    
*   Tạo Token: User -> My Account -> Security -> Generate Token (lưu lại).
    
*   Tạo Project: my-microservice.
    

#### Bước 3: Cấu hình Jenkins (http://localhost:8080)

*   **Cài Plugins:**
    
    *   SonarQube Scanner
        
    *   Docker Pipeline
        
    *   Docker
        
    *   Maven Integration
        
*   **Cấu hình Tools (Manage Jenkins -> Tools):**
    
    *   JDK: Temurin-17 (install automatically từ adoptium).
        
    *   Maven: Maven3 (install automatically).
        
    *   SonarQube Scanner: sonar-scanner (install automatically).
        
*   **Cấu hình Credentials:**
    
    *   Global Credential (Secret text):
        
        *   ID: sonarqube-token
            
        *   Dán token từ SonarQube.
            
*   **Cấu hình System:**
    
    *   SonarQube servers:
        
        *   Tên: sonar-server
            
        *   URL: http://sonarqube:9000
            
        *   Token: sonarqube-token
            

### 2\. Cách chạy Pipeline

1.  Tạo **New Item** trong Jenkins -> Chọn **Pipeline** -> Đặt tên Lab02-CI.
    
2.  **Pipeline Definition:** Chọn Pipeline script from SCM.
    
3.  SCM: Chọn **Git**, điền URL Repository.
    
4.  Nhấn **Save** -> **Build Now**.
    

### 3\. Kiểm tra kết quả

*   **Jenkins:** Console Output báo SUCCESS.
    
*   **SonarQube:** Truy cập http://localhost:9000, thấy project ecommerce-microservices hiển thị báo cáo (Bugs, Vulnerabilities, Code Smells).
    
*   **Eureka Server:** Truy cập http://localhost:8761, kiểm tra danh sách _Instances currently registered with Eureka_, phải thấy ORDER-SERVICE.
    
*   http://localhost:8082/api/orders#

