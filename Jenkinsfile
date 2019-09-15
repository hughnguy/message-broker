library(
    identifier: 'jenkins-shared-library-ciproject@master',
    retriever: modernSCM([
        $class: 'GitSCMSource',
        remote: 'https://github.com/hughnguy/jenkins-shared-library-ciproject.git',
        credentialsId: 'jenkins-shared-library-credentials'
    ])
)

pipeline {
    agent any
    tools {
        // Pipeline Maven Plugin: the quoted string contains the name of the installation
        maven "maven-3.3.9"

        jdk "java-1.7"
    }
    options {
        timestamps ()
    }
    stages {
        stage('Build Java') {
            steps {
                // Config File Provider Plugin (Manage Jenkins > Managed Files > Add a new config > Simple XML File > Add the file in the continuous-integration-project nexus how-to)

                configFileProvider([configFile(fileId: 'maven-settings', variable: 'MAVEN_SETTINGS')]) {
                    sh 'mvn --batch-mode --update-snapshots --settings $MAVEN_SETTINGS clean compile'
                }
            }
        }
        stage('Unit Tests') {
            steps {
                configFileProvider([configFile(fileId: 'maven-settings', variable: 'MAVEN_SETTINGS')]) {
                    sh 'mvn --batch-mode --update-snapshots --settings $MAVEN_SETTINGS compiler:testCompile surefire:test'
                }
            }
        }
        stage('Deploy artifacts to Nexus') {
            steps {
                configFileProvider([configFile(fileId: 'maven-settings', variable: 'MAVEN_SETTINGS')]) {
                    script {
                        dependency_helper.updateVersion(true, false)

                        // Deploy updated version
                        sh 'mvn --batch-mode --update-snapshots --settings $MAVEN_SETTINGS -Dhttps.protocols=TLSv1.2 jar:jar deploy:deploy'

                        dependency_helper.updateBranchesForDependentRepos()
                    }
                }
            }
        }
    }
    post {
        always {
            script {
                util_helper.wipeWorkSpace();
            }
        }
    }
}
