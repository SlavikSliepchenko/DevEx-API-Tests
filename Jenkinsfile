pipeline {
  agent any

  environment {
    JAVA_HOME = '/usr/lib/jvm/java-17-amazon-corretto'
    MVN      = '/opt/maven/bin/mvn'
    BASEPATH = "/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin"
  }

  options {
    timestamps()
    ansiColor('xterm')
    durabilityHint('PERFORMANCE_OPTIMIZED')
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
        sh 'pwd && ls -lah'
      }
    }

    stage('Env check') {
      steps {
        sh '''
          set -euxo pipefail
          PATH="/opt/maven/bin:$BASEPATH:${JAVA_HOME}/bin"
          echo "PATH=$PATH"
          /opt/maven/bin/mvn -v
          java -version
          git --version || true
        '''
      }
    }

    stage('Build') {
      steps {
        sh '''
          set -euxo pipefail
          PATH="/opt/maven/bin:$BASEPATH:${JAVA_HOME}/bin"
          /opt/maven/bin/mvn -B -e -DskipTests clean package
        '''
      }
    }

    stage('Test') {
      steps {
        sh '''
          set -euxo pipefail
          PATH="/opt/maven/bin:$BASEPATH:${JAVA_HOME}/bin"
          /opt/maven/bin/mvn -B -e test
        '''
      }
    }
  }

  post {
    always {
      junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
      archiveArtifacts artifacts: 'target/**/*.{log,txt}', allowEmptyArchive: true
    }
  }
}