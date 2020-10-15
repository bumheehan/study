
# 설치방법 


리눅스설치 과정
1. centos 7 minimal 설치 (8 지원 x)
2. 패키지
    - yum --enablerepo=extras install epel-release
    - net-tools, python-pip ,git
3. 노드 셋팅 
    - ssh key 생성 후 `ssh-copy-id 계정이름@서버IP` 으로 셋팅 
    - `ssh 서버IP` 접속하면 비밀번호 물어보는거 없이 정상 작동 확인
    - 자기자신한테도 보내야함

4. kubespray 클론 & 필요조건 설치
    - git clone https://github.com/kubernetes-sigs/kubespray.git
    - 내부 requirement.txt 보면 pip로 설치할 목록
    - sudo pip install -r requirements.txt 실행
        - ansible --version 으로 설치확인
5. kubespray template 사용
    - cp -rfp inventory/sample inventory/mycluster
    - inventory.ini 설정 후 실행
    - ansible-playbook -i inventory.ini -v --become --become-user=root ../../cluster.yml
    
    - error 
        - [Check if etcd cluster is healthy] 
            - 에러 내용 
            ```
            Error:  client: etcd cluster is unavailable or misconfigured; error #0: client: endpoint https://192.168.219.103:2379 exceeded header timeout

            error #0: client: endpoint https://192.168.219.103:2379 exceeded header timeout
            ```
            - 테스트
                - yum install ca-certificates -y 
                - reset 후 진행
                ```
                ansible-playbook -i inventory.ini -v --become --become-user=root ../../reset.yml
                ```


    


1. `마스터노드1`에서 나머지 노드에 SSH 연결 할 수 있도록 설정
    - `마스터노드1` 에서 ssh rsa방식 암호화키 생성
    ```
    $ssh-keygen -t rsa 
    - 생성위치
    - 최초 비밀번호 : 입력x 엔터
    ```
    - .ssh안에 비밀키 is_rsa 와 공개키 is_rsa.pub 생성
    - 접속할 서버 설정
        - GCP : GCP 메타데이터-SSH키에 is_rsa.pub 편집기로 열어서 내부 내용 복사하여 붙여넣기.
        - 자체서버 : `$ssh-copy-id 계정이름@서버IP`로 설정
        - ssh-copy-id으로 원격접근이 어려운경우 대상서버 직접적속하여 .ssh/authorized_keys명령 등으로 authorized_keys 파일안에 공개키를 복사
    


Kubespray 
- binary build -> containerizing(image) -> push image -> service define -> test deploy (canary test) -> prod deploy
- 위 과정을 자동화 하는 배포툴
- kubespray는 Ansible 기반 
    ```
    * Ansible

    구성관리 tool로, 인프라 관리과정을 코드로 기술한 IaC (Infra as Code)를 효율적이고 자동으로 관리할수있는 인프라 도구.

    Python 기반의 개발 + YAML로 정의 + JSON으로 통신

    초기설정이나 모니터링, 변경사항 추적이 불가능하다는 단점이 있지만, shell command를 제외하고는 모두 Idempotency(멱등성) 을 제공한다.
    ```

- template
    - 내부 group_vars ,inventory.ini 있음
        - group_vars : 클러스터 설치에 필요한 설정 내용
        - inventory.ini : 설치 대상 서버 정보 
    - tree 설치 : yum install -y tree
    ```
    -group_vars/
    ├── all : 설치 환경 및 방법
    │   ├── all.yml : Kubespray 설치 및 설정
    │   ├── aws.yml : aws 환경에서 설치할 때 적용할 설정
    │   ├── azure.yml : 애저 환경에서 설치할 때 적용할 설정
    │   ├── containerd.yml : ?
    │   ├── coreos.yml : CoreOS 환경에서 설치할 때 적용할 설정
    │   ├── docker.yml : Docker를 설치할 때 적용할 설정
    │   ├── gcp.yml : GCP 환경에서 설치할 때 적용할 설정
    │   ├── oci.yml : Oracle Cloud 환경에서 설치할 때 적용할 설정
    │   └── openstack.yml : OpenStack 환경에서 설치할 때 적용할 설정
    ├── etcd.yml : etcd 설치에 필요한 상세 설정
    └── k8s-cluster : 쿠버네티스 관련 설정
        ├── addons.yml : 쿠버네티스 클러스터를 설치한 후 추가로 설치할 구성 요소 관련 설정
        ├── k8s-cluster.yml : 쿠버네티스 클러스터를 설치할 때 설정
        ├── k8s-net-calico.yml : k8s-net-* 쿠버네티스 네트워크 플러그인별 상세 설정 
        네트워크 플러그인은 k8s-cluster.yml에서 kube_network_plugin 변수에 설정한 내용을 적용 하고 ,상세설정은 k8s-net-*  여기서 적용
        ├── k8s-net-canal.yml
        ├── k8s-net-cilium.yml
        ├── k8s-net-contiv.yml
        ├── k8s-net-flannel.yml
        ├── k8s-net-kube-router.yml
        ├── k8s-net-macvlan.yml
        └── k8s-net-weave.yml
    ```

    - 온프레미스(On-premise) 필수적으로 all.yml , k8s-cluster.yml 
        - all.yml
            - http_proxy, https_proxy, no_proxy 필드 확인
            - 인터넷에 직접 접근할 수 없는 환경이랑 프록시 설정 필수

        - k8s-cluster.yml : 온 프레미스 환경에서 아래대역이 사용 중 이면 변경 해야함
            - kube_service_addresses : 쿠버네티스 서비스 IP 할당 대역 설정
            - Kube_pods_subnet : 클러스터에 생성되는 파드들이 할당 받을 IP 대역
            - 기본값 
            ```
            # Kubernetes internal network for services, unused block of space.
            kube_service_addresses: 10.233.0.0/18

            # internal network. When used, it will assign IP
            # addresses from this range to individual pods.
            # This network must be unused in your network infrastructure!
            kube_pods_subnet: 10.233.64.0/18

            ```
    - inventory.ini : 파일 설치 대상 서버 정보
        - all : 클러스터 구성 호스트 설정
        - kube-master : 마스터노드
        - etcd : 쿠버네티스 클러스터 데이터 저장소
        - kube-node : 워커노드
        - k8s-cluster:children : 쿠버네티스 설치할 노드 

        ```
        # ## Configure 'ip' variable to bind kubernetes services on a
        # ## different ip than the default iface
        # ## We should set etcd_member_name for etcd cluster. The node that is not a etcd member do not need to set the value, or can set the empty string value.
        [all]
        <VM인스턴스명1> ansible_ssh_host=<사용중인 IP> ip=<사용중인 IP> etcd_member_name=<ctcd 명1>
        <VM인스턴스명2> ansible_ssh_host=<사용중인 IP> ip=<사용중인 IP> etcd_member_name=<ctcd 명2>
        <VM인스턴스명3> ansible_ssh_host=<사용중인 IP> ip=<사용중인 IP> etcd_member_name=<ctcd 명3>
        <VM인스턴스명4> ansible_ssh_host=<사용중인 IP> ip=<사용중인 IP>
        <VM인스턴스명5> ansible_ssh_host=<사용중인 IP> ip=<사용중인 IP>

        # ## configure a bastion host if your nodes are not directly reachable
        # bastion ansible_host=x.x.x.x ansible_user=some_user

        [kube-master]
        <VM인스턴스명1>
        <VM인스턴스명2>
        <VM인스턴스명3>

        [etcd]
        <VM인스턴스명1>
        <VM인스턴스명2>
        <VM인스턴스명3>

        [kube-node]
        <VM인스턴스명4>
        <VM인스턴스명5>

        [calico-rr]

        [k8s-cluster:children]
        kube-master
        kube-node
        calico-rr
        ## 저장 후 inventory.ini 종료
        :wq


        ## 쉘에서 아래 명령어 입력 및 GCP를 사용할 경우 반드시 사용자 계정으로 진행 (root X)
        # 몇십분가량 소요 됩니다.
        ```


--- 
# 쿠버네티스로 컨테이너 실행하기

에코서버
1. $ kubectl run echoserver --generator=run-pod/v1 --image="k8s.gcr.io/echoserver:1.10" --port=8080
2. $ kubectl expose po echoserver --type=NodePort
3. $ kubectl port-forward svc/echoserver 8080:8080

