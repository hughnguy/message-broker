pipeline {
    agent any
    tools {
        // Pipeline Maven Plugin: the quoted string contains the name of the installation
        maven "maven-3.3.9"

        jdk "java-1.7"
    }
    environment {
        DEPENDENCY_UPDATER_ENDPOINT = "http://dockerhost:8888/api/v1/dependency"
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
                        updateVersion()

                        sh 'mvn --batch-mode --update-snapshots --settings $MAVEN_SETTINGS -Dhttps.protocols=TLSv1.2 jar:jar deploy:deploy'

                        echo scm.userRemoteConfigs[0].url;

                        // Send post request here to slackbot to update projects that have dependency??
                        //updateBranchesForDependentRepos(env.POM_GROUP_ID, env.POM_ARTIFACT_ID, env.POM_VERSION)
                    }
                }
            }
        }
    }
    post {
        always {
            script {
                wipeWorkSpace();
            }
        }
    }
}

void updateBranchesForDependentRepos(String groupId, String artifactId, String version) {
    def repoUrl = sh(
        returnStdout: true,
        script: 'git config remote.origin.url'
    ).trim()

    try {
        def response = sh(
            returnStdout: true,
            script: "curl -XPOST -H \"Content-type: application/json\" -d '{\"repo\": \"${repoUrl}\",\"groupId\": \"${groupId}\",\"artifactId\": \"${artifactId}\",\"version\": \"${version}\"}' '${env.DEPENDENCY_UPDATER_ENDPOINT}'"
        ).trim()

        sh "echo 'Post response: '${response}"

        // readJSON / writeJSON requires Pipeline Utility Steps plugin
        def jsonResponse = readJSON text: response
        def statusCode = jsonResponse['status']

        if(statusCode != 200) {
            error("echo Incorrect status code returned: ${statusCode}");
        }

    } catch(err) {
        sh "echo ${err}"
        error("Failed to update branches for dependent repos.");
    }
}

void updateVersion() {
	if (fileExists("pom.xml")) {
		updatePomVersion()
	} else {
		error("This doesn't look like a maven or grails project, I don't know how to update the version here.")
	}
}

Map computeNewVersion(String groupId, String artifactId, String version) {
	Map groupIdInfo = computeGroupIdInfo(groupId)
	String[] versionParts = version.split("-")[0].split("\\.")
	versionParts[versionParts.length - 1] = "${env.BUILD_NUMBER}".toString()
	String shortCommit = sh(returnStdout: true, script: "git rev-parse --short HEAD").trim()
	version = versionParts.join(".") + "-" + shortCommit
	echo "********************\n********************\n******************** Version info: ${groupIdInfo.branch}:${artifactId}:${version}\n********************\n********************"
	env.POM_GROUP_ID = groupIdInfo.branch
	env.POM_ARTIFACT_ID = artifactId
	env.POM_VERSION = version
	return [groupId: groupIdInfo.branch, artifactId: artifactId, version: version]
}

void updatePomVersion() {
	def pom = readMavenPom file: 'pom.xml'
	Map versionInfo = computeNewVersion(pom.groupId, pom.artifactId, pom.version)
	pom.groupId = versionInfo.groupId
	pom.artifactId = versionInfo.artifactId
	pom.version = versionInfo.version
	writeMavenPom model: pom
}

Map computeGroupIdInfo(String groupId) {
	String baseGroupId = groupId
	String otherBranch = null
	if (baseGroupId.contains(".branch.")) {
		baseGroupId = baseGroupId.replaceAll("[.]branch[.].*\$", "")
		otherBranch = groupId.substring((baseGroupId+".branch.").length()).replace('.', '/')
	}
	String branchGroupId = baseGroupId
	if ("${env.BRANCH_NAME}" != "master") {
		branchGroupId = baseGroupId + ".branch.${env.BRANCH_NAME.replace('/', '.').replaceAll("[^-a-zA-Z0-9_.]", "-")}"
	}
	if ("${env.BRANCH_NAME}" == otherBranch) {
		otherBranch = null
	}
	return [base: baseGroupId, branch: branchGroupId, otherBranch: otherBranch]
}

void wipeWorkSpace() {
	try {
		deleteDir()
	} catch (e) {
	  echo "WARNING: could not delete the workspace. the error is ${e}"
	}
}