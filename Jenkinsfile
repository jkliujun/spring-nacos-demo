pipeline {
  agent any

  environment {
    USER_DOCKER_IMAGE_NAME="jkliujun/user-service"
    ORDER_DOCKER_IMAGE_NAME="jkliujun/order-service"
    DOCKER_CREDENTIALS_ID = "dockerhub-creds"
    GIT_CREDENTIALS_ID = "cicd-jkliujun-ssh-key"
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
            mvn clean install -DskipTests -Dgpg.skip=true \
              -Duser.name=jenkins \
              -Djib.to.auth.username=$DOCKER_USERNAME \
              -Djib.to.auth.password=$DOCKER_PASSWORD \
              -Dtimestamp=${TIMESTAMP}
          """
        }
      }
    }

    stage('Update Helm Chart Image Tag') {
      steps {
        script {
          def valuesFile = "${HELM_CHART_PATH}/values.yaml"
          sh "yq e '.userService.image.tag = \"jenkins-dev-${TIMESTAMP}\"' -i ${valuesFile}"
          sh "yq e '.orderService.image.tag = \"jenkins-dev-${TIMESTAMP}\"' -i ${valuesFile}"
        }
      }
    }

    stage('Commit Helm Chart Change') {
      steps {
        sshagent(['cicd-jkliujun-ssh-key']) {
          sh """
            export GIT_SSH_COMMAND='ssh -o StrictHostKeyChecking=no'
            git config user.name "jenkins"
            git config user.email "ci@jenkins"
            git remote set-url origin git@github.com:jkliujun/spring-nacos-demo.git
            git add helm-chart/values.yaml
            git commit -m "Update tag : jenkins-dev-${TIMESTAMP}" 
            git push origin HEAD:main
          """
        }
      }
    }

  }
}
