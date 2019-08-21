@Library("jenkinsfile@jenkins") _
node("basic"){
    notify{
        stage("Checkout"){
            checkout scm
        }
        versioned {
            env.JAVA_HOME="${tool 'java7'}"
            env.PATH="${env.JAVA_HOME}/bin:${tool 'M3'}/bin:${env.PATH}"

            configFileProvider([configFile(fileId: 'maven-sonar-settings', variable: 'MAVEN_SETTINGS')]) {
                stage("Compile"){
                    sh "mvn -B -U -s $MAVEN_SETTINGS clean compile"
                }
                stage("Test") {
                    env.JAVA_HOME="${tool 'java7'}"
                    try {
                        sh "mvn -B -U -s $MAVEN_SETTINGS '-Dtest=*' jacoco:prepare-agent resources:testResources compiler:testCompile surefire:test jacoco:report"
                    } finally {
                        step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml'])
                        step([$class: 'JacocoPublisher', execPattern: 'target/coverage-reports/jacoco.exec'])
                    }
                    env.JAVA_HOME="${tool 'java7'}"
                }
                stage('PMD') {
                    try {
                        sh "mvn -B -U -s $MAVEN_SETTINGS pmd:check"
                    } finally {
                        step([$class: 'PmdPublisher', pattern: 'target/pmd.xml'])
                    }
                }
                stage('DRY') {
                    try {
                        sh "mvn -B -U -s $MAVEN_SETTINGS pmd:cpd-check"
                    } finally {
                        step([$class: 'DryPublisher', pattern: 'target/cpd.xml'])
                    }
                }
                stage('Findbugs') {
                    try {
                        sh "mvn -B -U -s $MAVEN_SETTINGS findbugs:check"
                    } finally {
                        step([$class: 'FindBugsPublisher', pattern: '**/findbugsXml.xml'])
                    }
                }
                stage("Sonar Analysis") {
                    env.JAVA_HOME="${tool 'java7'}"
                    sh "mvn -B -U -s $MAVEN_SETTINGS sonar:sonar | true"
                    env.JAVA_HOME="${tool 'java7'}"
                }
                stage('Tasks') {
                    step([$class: 'TasksPublisher', high: 'FIXME', low: '', normal: 'TODO', pattern: 'src/**/*.java'])
                }
                stage("Deploy") {
                    sh "mvn -B -U -s $MAVEN_SETTINGS -DaltDeploymentRepository=klipfolio-repository::default::$MAVEN_REPOSITORY_URL -DupdateReleaseInfo=true jar:jar deploy:deploy"
                     if ("${env.BRANCH_NAME}" == "master") {
                        kf_github.createAndPushVersionTag("v${KF_VERSION}")
                     }
                }
            }
        }
	}
}