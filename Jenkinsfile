node("8_248_MAVEN") {
   checkout scm
   parameters {
        string(name:'code_branch', defaultValue: 'develop', description: '代码分支')
		string(name:'version', defaultValue: '1.0', description: '镜像版本')
		choice(name: 'sid_version', choices: ['1.9.1', '1.9.0', '1.8.0p2'], description: '版本')
		choice(name: 'host', choices: ['172.17.8.77', '172.17.8.79'], description: '部署的环境')
		string(name:'config_branch', defaultValue: 'empty', description: '配置文件分支')
		string(name:'script_branch', defaultValue: 'develop', description: '升级脚本分支')
		string(name:'script_url', defaultValue: 'empty', description: '升级脚本url')
		choice(name: 'update_db',  defaultValue: 'empty', description: '升级脚本执行的库')
		string(name:'script_version', defaultValue: 'empty', description: '升级脚本版本，批量执行脚本需填写，如R1.8.0p1')
		choice(name: 'build_env', choices: ['OpenSource', 'Localization'], description: '基于不同版本（开源或国产）构建')
   }
   
   stage('Maven comple and package') {
      script {
			sh '''
			    chmod +777 package gradlew
			    sh package
			'''	
    	}
   }
   stage('push image') {
      sshPublisher(publishers: [sshPublisherDesc(configName: '172.17.8.141', transfers: [sshTransfer(cleanRemote: false, excludes: '', execCommand: '''
                    #!/bin/bash
                    docker rmi --force `docker images | grep anka-authentication | awk '{print $3}'`
                    docker rmi --force `docker images | grep none | awk '{print $3}'`
                    cd /usr/local/docker-build/sourceid/anka-authentication
                    docker build -t 172.17.8.20:8082/sourceid/anka-authentication:${version} ./
                    docker login 172.17.8.20:8082 -u docker-build -p docker-build
                    docker push 172.17.8.20:8082/sourceid/anka-authentication:${version}''',
                execTimeout: 120000, 
                flatten: false, 
                makeEmptyDirs: false, 
                noDefaultExcludes: false,
                patternSeparator: '[, ]+', 
                remoteDirectory: 'docker-build/sourceid/anka-authentication',
                remoteDirectorySDF: false, 
                removePrefix: 'authentication-starter',
                sourceFiles: 'authentication-starter/build/libs/authentication-starter-7.0.4.jar')],
                usePromotionTimestamp: false, 
                useWorkspaceInPromotion: false, verbose: true)])
      
   }
   stage('predeploy') {
        sshPublisher(publishers: [sshPublisherDesc(configName: host, transfers: [sshTransfer(cleanRemote: false, excludes: '', execCommand: '''
                set -x
                source /etc/profile
                #获取部署环境kad版本号
                cd /opt/kad/workspace/ruijie-sourceid/conf/
                kad_version=$(cat all.yml | grep \'KAD_APP_VERSION\' | awk -F \':\' \'{print $2}\')
                kad_version=$(echo $kad_version | sed 's/\"//g')

                #更新kad.yml中组件镜像号
                sed -i 's?sourceid/anka-authentication\", version: \".*\"?sourceid/anka-authentication\", version: \"${version}\"?g' /opt/kad/down/sourceid-kad-${kad_version}/kad.yml


                
            ''',
            execTimeout: 150000,
            flatten: false,
            makeEmptyDirs: false,
            noDefaultExcludes: false,
            patternSeparator: '[, ]+',
            remoteDirectory: '',
            remoteDirectorySDF: false,
            removePrefix: '', sourceFiles: '')],
            usePromotionTimestamp: false,
            useWorkspaceInPromotion: false,
            verbose: true)])
   }
   
   stage('deploy') {
        sshPublisher(publishers: [sshPublisherDesc(configName: host, transfers: [sshTransfer(cleanRemote: false, excludes: '', execCommand: '''
                set -x
                source /etc/profile
				#通过kad工具部署组件
                cd /opt/kad/
                ansible-playbook -i /opt/kad/inventory/ /opt/kad/playbooks/sourceid/reconfig.yml --tags anka-authentication
				
            ''',
            execTimeout: 1500000,
            flatten: false,
            makeEmptyDirs: false,
            noDefaultExcludes: false,
            patternSeparator: '[, ]+',
            remoteDirectory: '',
            remoteDirectorySDF: false,
            removePrefix: '', sourceFiles: '')],
            usePromotionTimestamp: false,
            useWorkspaceInPromotion: false,
            verbose: true)])
   }
}