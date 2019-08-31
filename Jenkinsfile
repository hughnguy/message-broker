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
    // Config File Provider Plugin (Manage Jenkins > Managed Files > Add a new config > Simple XML File > Add the file in the continuous-integration-project nexus how-to)

    configFileProvider([configFile(fileId: 'maven-settings', variable: 'MAVEN_SETTINGS')]) {

        stages {
            stage('Build Java') {
                steps {
                    sh 'mvn --batch-mode --update-snapshots --settings $MAVEN_SETTINGS clean compile'
                }
            }
            stage('Unit Tests') {
                steps {
                    sh 'mvn --batch-mode --update-snapshots --settings $MAVEN_SETTINGS test'
                }
            }
            stage('Deploy artifacts to Nexus') {
                steps {
                    script {
                        updateVersion()

                        sh 'mvn --batch-mode --update-snapshots --settings $MAVEN_SETTINGS -Dhttps.protocols=TLSv1.2 deploy'
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