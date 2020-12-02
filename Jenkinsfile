pipeline{
    agent any
    tools{
        maven 'maven3'
    }
    stages{
        stage('Build'){
            steps{
                sh script: 'mvn clean package'
            }
        }
        stage('Upload War to Nexus'){
            steps{
                nexusArtifactUploader artifacts:[
                    [[artifactId: 'e_biller', classifier: '', file: 'target/e_biller0.0.1-SNAPSHOT.war', type: 'war']] credentialsId: '5a8324ff-4a2b-4d5a-8bb4-431eb4392bd5', groupId: 'mz.co.standardbank', nexusUrl: 'teomaz.com', nexusVersion: 'nexus3', protocol: 'http', repository: 'http://teomaz.com:8081/repository/Open_BankingUAT/', version: '0.0.1-SNAOSHOT'
                ]
            }
        }
    }
}
