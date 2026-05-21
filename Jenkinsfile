pipeline {
    agent any

    environment {
        IMAGE_NAME = "myapp"
        CONTAINER_NAME = "myapp"
        PORT = "8081"
    }

    stages {
        stage('Build') {
            steps {
                sh '''
                	chmod +x gradlew
                	./gradlew clean build --no-daemon
                '''
            }
        }

        stage('Docker Build') {
            steps {
                sh """
		                if docker image inspect ${IMAGE_NAME}:latest > /dev/null 2>&1; then
	                    docker image tag ${IMAGE_NAME}:latest ${IMAGE_NAME}:backup
	                  fi
	                    docker build -f dockerfile_was -t ${IMAGE_NAME}:latest .
                """
            }
        }

        stage('Stop Existing Container') {
            steps {
                sh """
                    docker stop ${CONTAINER_NAME} || true
                    docker rm ${CONTAINER_NAME} || true
                """
            }
        }

        stage('Run New Container') {
            steps {
            	withCredentials([file(credentialsId: 'env_credential', variable: 'ENV_FILE')]) {
				    sh """
				        set -e
				
				        CONTAINER_NAME='${CONTAINER_NAME}'
				        PORT='${PORT}'
				        IMAGE_NAME='${IMAGE_NAME}'
				
				        docker run -d \
				            --env-file "\$ENV_FILE" \
				            -v /home/ubuntu/docker_srv/was_home/logs:/var/was_home/logs \
				            --name \$CONTAINER_NAME \
				            -p \$PORT:8080 \
				            \$IMAGE_NAME:latest
				    """
				}
            }
        }
        
        stage('Health Check') {
            steps {
                sh '''
                    echo "Waiting for application start..."

                    sleep 15

                    curl -f http://localhost:$PORT || exit 1
                '''
            }
        }
    }

    post {
        success {
            echo 'Deploy success'
        }

        failure {
            echo 'Deploy failed'
            
            withCredentials([file(credentialsId: 'env_credential', variable: 'ENV_FILE')]) {
	            sh """
	                echo "Rollback start..."
	                
	                CONTAINER_NAME='${CONTAINER_NAME}'
			        PORT='${PORT}'
			        IMAGE_NAME='${IMAGE_NAME}'
	
	                docker stop \$CONTAINER_NAME || true
	                docker rm \$CONTAINER_NAME || true
	                
	                if docker image inspect $IMAGE_NAME:backup > /dev/null 2>&1; then
	                	docker run -d \
			            --env-file "\$ENV_FILE" \
			            -v /home/ubuntu/docker_srv/was_home/logs:/var/was_home/logs \
			            --name \$CONTAINER_NAME \
			            -p \$PORT:8080 \
	                    \$IMAGE_NAME:backup
	
	                    echo "Rollback completed"
	                else
	                    echo "No backup image found"
	                fi
	            """
            }
        }
    }
}