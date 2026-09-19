/**
* Unless we decide otherwise (through a jenkins-infra/helpdesk issue with discussion):
* only run on trusted.ci.jenkins.io on the principal branch.
**/

final String jenkinsUsageStatsCli = '/opt/jenkins-usage-stats/build/jenkins-usage-stats'
String reportYear
String reportMonth
String importMonth
String importYear

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

                // Determine which month/year need to be published (specified by user or defaults to last month)
                if (params.TARGET_MONTH) {
                    reportMonth = params.TARGET_MONTH.toString().trim()
                } else {
                    reportMonth = sh(script: '''
                    date +'%m' -d '1 month ago'
                    ''', returnStdout: true).trim()
                }
                if (params.TARGET_YEAR) {
                    reportYear = params.TARGET_YEAR.toString().trim()
                } else {
                    reportYear = sh(script: '''
                    date +'%Y' -d '1 month ago'
                    ''', returnStdout: true).trim()
                }
                echo "== I will generate report for: ${reportYear}:${reportMonth}"

                // To publish a report for a given month means we need to import the data from month + 1
                // Calculation is delegated to the Linux 'date' command: safer to run on agent and manages time properly.
                final String calculateImportDateCmd = "date +'%m-%Y' -d '${reportMonth}/01/${reportYear} + 1 month'"
                final String importDate = sh(script: calculateImportDateCmd, returnStdout: true).trim()
                importMonth = importDate.split('-')[0]
                importYear = importDate.split('-')[1]
                echo "== I will import data report for: ${importYear}:${importMonth}"
            }

            withEnv([
                "REPORT_YEAR=${reportYear}",
                "REPORT_MONTH=${reportMonth}",
                "IMPORT_YEAR=${importYear}",
                "IMPORT_MONTH=${importMonth}",
            ]) {
                stage('Import from usage') {
                    // Retrieve log files from usage.jenkins.io VM to the local census.jenkins.io VM
                    sshagent(credentials: ['usage-jenkins-io-usagestats-ssh-key'], executable: '', usernameVariable: 'USAGE_SSH_USERNAME') {
                        withCredentials([string(credentialsId: 'usage-jenkins-io-ssh-hostkey', variable: 'USAGE_JENKINS_IO_SSH_HOSTKEY')]) {
                            // Using a credential even if the(multi-line) value is not that sensitive
                            sh 'echo "${USAGE_JENKINS_IO_SSH_HOSTKEY}" > ~/.ssh/known_hosts'
                        }
                        sh '''
                        rsync -avt "${USAGE_SSH_USERNAME}"@usage.jenkins.io:/srv/usage/usage-stats/*"${IMPORT_YEAR}${IMPORT_MONTH}"* /srv/census/usage-stats/"${IMPORT_YEAR}${IMPORT_MONTH}/"
                        '''
                    }
                }

                stage('Import to database') {
                    // Import log files from local census.jenkins.io disk into the local PostgreSQL database
                    echo "TBD"
                }

                stage('Report from database') {
                    // Generate CSV reports from the local PostgreSQL database to local disk
                    echo "TBD"
                }

                stage('Publish report to GitHub') {
                    // Publish CSV reports from local disk to GitHub repository
                    echo "TBD"
                }
            }
        }
    }
}
