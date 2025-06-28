pipeline {
  agent any

  environment {
    DOCKER_IMAGE_NAME = "jkliujun/user-service"
    DOCKER_CREDENTIALS_ID = "dockerhub-creds"
    GIT_CREDENTIALS_ID = "cicd-jkliujun-jenkins-github-token"
    HELM_CHART_PATH = "helm-chart"
    TIMESTAMP = ""  // set dynamically
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Generate Tag') {
      steps {
        script {
          TIMESTAMP = sh(script: "date +%Y%m%d%H%M%S", returnStdout: true).trim()
          env.TIMESTAMP = TIMESTAMP
        }
      }
    }

    stage('Build and Push Image via Jib') {
      steps {
        withCredentials([usernamePassword(credentialsId: "${DOCKER_CREDENTIALS_ID}", usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
          sh """
            cd grpc-proto
            mvn compile install -DskipTests
            cd ..
            cd user-service
            export DOCKER_IMAGE_NAME="jkliujun/user-service"
            mvn compile jib:build \
              -Djib.to.image=${DOCKER_IMAGE_NAME}:${TIMESTAMP} \
              -Djib.to.auth.username=$DOCKER_USERNAME \
              -Djib.to.auth.password=$DOCKER_PASSWORD \
              -Djib.container.environment=BUILD_DATE=${TIMESTAMP},DOCKER_NAME=${DOCKER_IMAGE_NAME}:${TIMESTAMP}
            cd ..
            cd order-service
            export DOCKER_IMAGE_NAME="jkliujun/order-service"
            mvn compile jib:build \
              -Djib.to.image=${DOCKER_IMAGE_NAME}:${TIMESTAMP} \
              -Djib.to.auth.username=$DOCKER_USERNAME \
              -Djib.to.auth.password=$DOCKER_PASSWORD \
              -Djib.container.environment=BUILD_DATE=${TIMESTAMP},DOCKER_NAME=${DOCKER_IMAGE_NAME}:${TIMESTAMP}
            cd ..
          """
        }
      }
    }

    stage('Update Helm Chart Image Tag') {
      steps {
        script {
          def valuesFile = "${HELM_CHART_PATH}/values.yaml"
          sh "yq e '.image.tag = \"${TIMESTAMP}\"' -i ${valuesFile}"
        }
      }
    }

    stage('Commit Helm Chart Change') {
      steps {
        sshagent([GIT_CREDENTIALS_ID]) {
          sh """
            git config user.name "jenkins"
            git config user.email "ci@jenkins"
            git add ${HELM_CHART_PATH}/values.yaml
            git commit -m "Update Docker tag to ${TIMESTAMP}"
            git push origin HEAD:main
          """
        }
      }
    }

  }
}
