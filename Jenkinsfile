/**
* Unless we decide otherwise (through a jenkins-infra/helpdesk issue with discussion):
* only run on trusted.ci.jenkins.io on the principal branch.
**/

final String jenkinsUsageStatsCli = '/opt/jenkins-usage-stats/build/jenkins-usage-stats'

if (infra.isTrustedCiController()) {
    node('census') {
        withEnv(["JENKINS_USAGE_STATS_CLI=${jenkinsUsageStatsCli}"]) {
            stage('Prepare') {
                checkout scm
                // Sanity checks
                sh '''
                rsync --version
                "${JENKINS_USAGE_STATS_CLI}" --help
                '''

                // Determine which month/year need to be processed (current on the weekly cron execution or from parameter for manual builds?)
            }

            stage('Import from usage') {
                // Retrieve log files from usage.jenkins.io VM to the local census.jenkins.io VM
                echo "TBD"
            }

            stage('Import to database') {
                // Import log files from local census.jenkins.io disk into the local PostgreSQL database
                echo "TBD"
            }

            stage('Report from database') {
                // Generate CSV reports from the local PostgreSQL database to local disk
                echo "TBD"
            }

            stage('Publish breport to GitHub') {
                // Publish CSV reports from local disk to GitHub repository
                echo "TBD"
            }

        }
    }
}
