/**
* Unless we decide otherwise (through a jenkins-infra/helpdesk issue with discussion):
* only run on trusted.ci.jenkins.io on the principal branch.
**/

final String jenkinsUsageStatsCli = '/opt/jenkins-usage-stats/build/jenkins-usage-stats'

if (infra.isTrusted() && env.BRANCH_IS_PRIMARY) {
    node('census') {
        withEnv(["JENKINS_USAGE_STATS_CLI=${jenkinsUsageStatsCli}"]) {
            checkout scm

            // Sanity checks
            sh '''
            rsync --version
            "${JENKINS_USAGE_STATS_CLI}" --help
            '''

            // Determine which month/year need to be processed (current on the weekly cron execution or from parameter for manual builds?)

            // Retrieve log files from usage.jenkins.io VM to the local census.jenkins.io VM

            // Import log files from local census.jenkins.io disk into the local PostgreSQL database

            // Generate CSV reports from the local PostgreSQL database to local disk

            // Publish CSV reports from local disk to GitHub repository

        }
    }
}
