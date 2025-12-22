# 1. VPC
resource "aws_vpc" "lab02_vpc" {
  cidr_block = "10.0.0.0/16"
  tags = { Name = "Lab02-VPC" }
}

# 2. Public Subnet
resource "aws_subnet" "public_subnet" {
  vpc_id                  = aws_vpc.lab02_vpc.id
  cidr_block              = "10.0.1.0/24"
  map_public_ip_on_launch = true
  availability_zone       = "us-east-1a"
  tags = { Name = "Lab02-Public-Subnet" }
}

# 3. Internet Gateway
resource "aws_internet_gateway" "igw" {
  vpc_id = aws_vpc.lab02_vpc.id
  tags = { Name = "Lab02-IGW" }
}

# 4. Route Table cho Public Subnet
resource "aws_route_table" "public_rt" {
  vpc_id = aws_vpc.lab02_vpc.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw.id
  }
  tags = { Name = "Lab02-Public-RT" }
}

resource "aws_route_table_association" "public_assoc" {
  subnet_id      = aws_subnet.public_subnet.id
  route_table_id = aws_route_table.public_rt.id
}

# 5. Security Group (Mở cổng 22 và 80)
resource "aws_security_group" "web_sg" {
  name        = "lab02_web_sg"
  description = "Allow SSH and HTTP"
  vpc_id      = aws_vpc.lab02_vpc.id

  ingress {
    description = "SSH from anywhere"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"] # Checkov sẽ cảnh báo dòng này
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# 6. EC2 Instance (Tự động tìm Amazon Linux 2 AMI)
data "aws_ami" "amazon_linux" {
  most_recent = true
  owners      = ["amazon"]
  filter {
    name   = "name"
    values = ["amzn2-ami-hvm-*-x86_64-gp2"]
  }
}

resource "aws_instance" "web_server" {
  ami           = data.aws_ami.amazon_linux.id
  instance_type = "t2.micro"
  subnet_id     = aws_subnet.public_subnet.id
  vpc_security_group_ids = [aws_security_group.web_sg.id]
  key_name = "quyen-ec2"
  tags = { Name = "Lab02-EC2-Terraform" }
}
