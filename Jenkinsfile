pipeline {
    agent any
    tools {
        // Nodejs Plugin: the quoted string contains the name of the installation
        nodejs "nodejs-10.16.0"

        // Pipeline Maven Plugin: the quoted string contains the name of the installation
        maven "maven-3.3.9"
    }
    options {
        timestamps ()
    }
    stages {
        //stage('Build Java') {
            //steps {
                //sh 'mvn -B clean compile'
            //}
        //}
        //stage('Unit Tests') {
            //steps {
                //sh 'mvn test'
            //}
        //}
        stage('Deploy artifacts to Nexus') {
            steps {
                script {
                    updateVersion()
                    //sh 'mvn deploy'
                }
            }
        }
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
    echo "wut";
    echo version;
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