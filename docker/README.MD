Docker 

## 1. 도커란

- 도커는 리눅스 컨테이너에 여러기능을 추가함으로써 애플리케이션을 컨테이너로서 좀더 쉽게 사용할 수 있게 만들어진 오픈소스 프로젝트
- 도커와 관련된 프로젝트는 도커 컴포즈, 레지스트리,도커머신,키네마틱 등 여러가지가 있지만 일반적으로 도커라고 하면 도커엔진 혹은 도커에 관련된 모든 프로젝트를 의미

### 1.1 가상머신과 도커 컨테이너

- 기존 가상화 기술은 하이퍼바이저 위에 게스트OS 올리고 각 게스트 OS 는 독립된 공간을 할당받아 사용
  - 이작업은 하이퍼바이저를 거치기 때문에 호스트에 비해 성능 하락이 생김 
  - 이 가상머신은 운영체제를 사용하기위한 라이브러리, 커널 등을 모두 포함하기때문에 완벽한 운영체제를 만들수 있지만 성능 손실과 용량이 큰 이미지로 배포가 힘들음
- 도커는 리눅스의 자체 기능인 chroot, 네임스페이스, cgroup 을 사용함으로써 프로세스 단위의 격리 환경을 만들기 때문에 성능 손실이 거의 없음
  - 컨테이너에 필요한 커널은 호스트의 커널을 공유해 사용
  - 컨테이너안에는 애플리케이션을 구동하는 데 필요한 라이브러리 및 실행 파일만 존재하기 때문에 이미지 용량이 기존보다 많이 줄어듦

### Docker 제품

#### `Community Edition` 

- 개발자 및 소규모

#### `Enterprise Edition`

- 유료

![](/image/1.2.jpg)

### 1.2 도커 엔진 설치

다양한 운영체제가능한데 대부분 리눅스에서 사용

- CoreOs,CentOS,우분투 등

윈도우나 맥은 가상화 공간을 별도로 생성



#### 1.2.1 리눅스 도커 엔진 설치

##### 최신버전 커널 확인

```
# uname -r
3.10.0-862.e17.x86_64
```

##### 지원 기간 내에 있는 배포판인지 확인

일부 오래된 리눅스 배포판은 업데이트 등의 지원을 못받을수 있음

- CentOS7, 우분투 16.04 등

##### 64비트 리눅스인지 확인

##### sudo명령어로 설치 및 root권한으로 설치



##### CentOS7

yum-utils :yum 패키지매니저 관련 유틸리티 모음

```
# yum install -y yum-utils
# yumc-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
# yum install -y docker-ce
# systemctl start docker
# docker info

Client:
 Debug Mode: false

Server:
 Containers: 0
  Running: 0
  Paused: 0
  Stopped: 0
 Images: 0
.....
```



## 2. 도커 엔진

### 2.1 도커 이미지와 컨테이너

#### 2.1.1 도커 이미지

- 이미지는 컨테이너 생성할 때 필요한 요소
- 가상머신사용할때 iso 개념과 비슷
- 이름은   [저장소이름]/[이미지이름]:[태그] 로 구성
  - alicek106/ubuntu:14.04 
  - 태그가 없을시 latest 로 인식
  - 저장소 : 이미지가 저장된 저장소, 생략하면 도커 허브의 공식 이미지를 뜻함 , 많이 생략
  - 이미지 이름은 해당이미지가 어떤 역할인지 나타냄,생략 못하고 반드시설정
  - 태그는 버전관리 리비전관리에 사용, 일반적으로 14.04처럼 버전으로 명시하지만 태그가 없을시 latest로 인식
    - ubuntu:latest

#### 2.1.2 도커 컨테이너

- 도커이미지는 우분투, CentOS 등 기본적인 운영체제부터 아파치 웹서버 MySQL 데이터 베이스 등 각종 애플리케이션, 하둡이나 스파크 스톰 등 빅데이터 분석 도구까지 갖가지 종류가 있음
- 이미지로 컨테이너를 생성하면 이미지 안에 있는 파일 시스템과, 격리된 시스템 자원, 네트워크를 사용 할 수 있음
- 이미지는 읽기전용으로 사용하고 변경된 사항만 컨테이너 계층에 저장 , 원래이미지영향 없음
- 독립된 파일시스템을 제공받아 어떤어플리케이션을 설치하거나 삭제해도 다른 컨테이너와 호스트는 변화없음

### 2.2 도커 컨테이너 다루기 

도커 컨테이너는 run 과 creat로 만들 수 있다.

옵션은 나중에 배우는데 우선 -i 는상호 입출력 , -t옵션은 tty 를 활성화 해서 배시셸을 사용

```
# docker run -i -t ubuntu
Unable to find image 'ubuntu:latest' locally
latest: Pulling from library/ubuntu
7413c47ba209: Pull complete 
0fe7e7cbb2e8: Pull complete 
1d425c982345: Pull complete 
344da5c95cec: Pull complete 
Digest: sha256:c303f19cfe9ee92badbbbd7567bc1ca47789f79303ddcef56f77687d4744cd7a
Status: Downloaded newer image for ubuntu:latest
root@e7ccb92ea5d6:/# 
```

- 한줄로 컨테이너 생성 및 실행하여우분투 환경에 들어옴, 

- Ctrl + P,Q 는 컨테이너 정지안하고 나오기
- Ctrl +D, exit 입력은 컨테이너에서 나오는방법 



##### 이미지 받기

```
# docker pull centos:7
```

##### 이미지확인

```
# docker images
```

##### Create로 컨테이너 생성

```
# docker create -i -t --name mycentos centos:7
```

- 생성만 했기때문에 run과달리 컨테이너 내부로 들어오지 않음
- 무작위 16진수 해시값이 나옴 , 보통 12자리만 사용

##### 컨테이너 ID 보기

```
# docker inspect 
```

##### 컨테이너 실행과 내부로 들어가기

```
# docker start mycentos
mycentos

# docker attach mycentos
[root@~~~#]
```

##### 컨테이너 이름대신 아이디 사용가능

- 자리수 제한은 없음, 다른 컨테이너와 겹치지않는선 3~4개 정도만 입력하면됨 

```
# docker start dd50c5cb6bf4~~~
# docker start dd5
```

##### Run 명령어

이미지가 없을때 pull -> docker create -> docker start -> -i -t 옵션시 docker attach까지 들어감

#### 2.2.2 컨테이너 목록 확인

- 정지되지 않는 컨테이너목록

```
# docker ps
```

- 모든 목록

  ```
  #docker ps -a
  ```

  | CONTAINER ID | IMAGE    | COMMAND     | CREATED        | STATUS                    | PORTS | NAMES                 |
  | :----------: | -------- | ----------- | -------------- | ------------------------- | ----- | --------------------- |
  | ff1b51cc4471 | centos:7 | "/bin/bash" | 5 minutes ago  | Up 5 minutes              |       | mycentos              |
  | e7ccb92ea5d6 | ubuntu   | "/bin/bash" | 48 minutes ago | Exited (0) 45 minutes ago |       | affectionate_chatelet |

  - CONTAINER ID : 컨터이너에게 자동으로 할당되는 고유한 ID, 일부분만나오는데 Inspect 로 전체확인가능

    - ```
      # docker inspect mycentos | grep Id
      ```

  - IMAGE : 컨테이너 생성할때 사용한 이미지

  - COMMAND : 컨테이너가 실행될 명령어, 커맨드는 대부분 이미지에 미리 내장되어 있다. CentOS 나 우분투는 /bin/bash 명령어가 내장되어 있기 때문에 컨테이너를 생성할때 따로 지정안함

    - 컨테이너를 생성할때 내장 커맨드는 변경할수 있음

    - ```
      # docker run -i -t ubuntu echo hello world!
      CONTAINER ID        IMAGE               COMMAND              
      3e0627d9f00b        ubuntu              "echo hello world!" 
      ff1b51cc4471        centos:7            "/bin/bash"                           
      e7ccb92ea5d6        ubuntu              "/bin/bash"
      ```

  - CREATED : 컨테이너가 생성되고 흐른 시간

  - STATUS : 컨테이너 상태를 나타냄, 실행중이면 UP, 종료되면 Exited 일시중지 Pause

  - PORT : 컨테이너가 개방한 포트와 호스트에 연결한 포트를 나열, 

  - NAMES : 컨테이너 고유이름 지정안하면 자동으로 설정됨, 이름바꿔줘야함

    - ```
      # docker rename affectionate_chatelet my_container
      ```

#### 2.2.3 컨테이너 삭제

- 컨테이너 삭제 

  - 실행중일때 정지해준뒤 삭제

  ```
  # docker stop 컨테이너이름
  # docker rm 컨테이너이름
  ```

  - 실행중인거 삭제할때는 -f 로 한번에 삭제가능

  ```
  # docker rm -f 컨테이너이름
  ```

  - 한번에 모든 컨테이너 삭제

  ```
  # docker container prune
  ```

#### 2.2.4 `컨테이너를 외부에 노출 ★`

- 컨테이너는 가상 머신과 마찬가지로 가상 IP 주소를 할당 받음
  - 172.17.0.x 로 순차적으로 할당
    - 172.17.0.1은 메인 도커 IP이고 2부터 순차적 할당

```
root@ad367f8b0095:/# hostname -i
172.17.0.2
```

- 일반적으로 설정을 하지 않을 경우 호스트 PC에서만 도커에 접속 가능!!!!

- 포트 열어주기

  - -p 입력하여 포트 입력 , 입력 규칙은 [호스트 포트] : [컨테이너 포트]

  - ```
    # docker run -i -t --name mywebserver -p 80:80 ubuntu
    ```

  - 호스트 7000 번 포트를 도커 80번 포트와 연결하려면 7000:80 이렇게 입력

  - ```
    # docker run -i -t --name mywebserver -p 80:80 ubuntu
    ```

  - -p 여러번 써서 여러개의 포트 입력가능

  - 호스트가 여러아이피가 있을 경우 특정 아이피의 포트를 사용하고싶을때 아이피까지 입력

  - ```
    # docker run -i -t --name mywebserver -p 3306:3306 -p 172.24.0.141:7777:80  ubuntu
    ```

- 간단한 웹서버만들기

  - ```
    # docker run -i -t --name mywebserver -p 3306:3306 -p 172.24.0.141:7777:80  ubuntu
    
    root@16ffa7e8c800:/# apt-get update 
    root@16ffa7e8c800:/# apt-get install apache2 -y
    root@16ffa7e8c800:/# service apache2 start
    ```

    



#### 2.2.5 컨테이너 애플리케이션 구축

- 한 컨테이너에 프로세스 하나만 실행

- 과거 All in one 컨테이너는 하나의 컨테이너에 데이터베이스부터 웹서버까지 모두 포함하여 배포

- 지금은 데이터베이스따로 웹서버 따로 설정

  - ##### 데이터베이스

  - ```
    # docker run -d --name wordpressdb -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=wordpress mysql:5.7
    ```

  - ##### 워드프레스 

  - ```
    # docker run -d -e WORDPRESS_DB_PASSWORD=password --name wordpress --link wordpressdb:mysql -p 80 wordpress 
    ```

    

##### Run -i -t 옵션과 -d 옵션의 차이

- -i 는 표준입출력 , -t 는 tty가능 즉 터미널 형태이지만 입출력 불가능 , -i -t를 해야 터미널에서 입출력가능

  즉 -i -t 옵션은 표준 입출력이 활성화된 셀환경을 사용할 수 있다.

- -d 는 데몬 모드라고하며 컨테이너가 백그라운드로 실행됨, 실행프로그램이 있어야함 없을 경우 그대로 종료

  - ubuntu를 -d 로 실행하면 바로종료됨, 
  - mysql -d 없이 실행해보면 로그들 다뜸 , -d넣고 실행하고 로그로 보는게 좋음 2.2.8에서 추가 설명

- -e 옵션으 컨테이너 내부의 환경변수설정, 자주사용

  - 리눅스에서 echo를 사용하여 환경변수를 볼수 있음

  - ```
    # echo ${환경변수} 	
    ```

  - wordpressdb에서 #echo ${} 을 사용하려면 배시 쉘을 사용해야하는데 -d 모드로 만든 프로그램은 attach를 사용하더라도 배시 쉘을 사용할 수 없다. 이럴때는 exec 명령어를 사용하면 쉘을 사용할 수 있다.

  - ```
    # docker exec -i -t wordpressdb /bin/bash
    ```

- --link 는 A컨테이너에서 B컨테이너로 접근하는 방법 중  가장 간단한 것은 NAT으로 할당받은 내부 IP를 사용한것 입니다. B의 컨테이너의 IP가 172.17.0.3 이라면 A컨테이너는 이IP를 사용하여 B컨테이너에 접근할 수 있습니다. 하지만 매번 시작할때마다 시작순서에 맞게 재할당 하므로 매번 IP로 접근하기는 어려움

  --link 옵션은 컨테이너 별명으로 접근하도록 설정  --wordpressdb:mysql 은 wordpressdb컨테이너를 mysql로 접속 가능하게 함

  - 책에는 다음으로 확인하는데 나는 wordpress 컨테이너에 ping 안깔려있어서 실행안됨

  ```
  # docker exec wordpress ping -c 2 mysql
  ```

  ```
  OCI runtime exec failed: exec failed: container_linux.go:345: starting container process caused "exec: \"ping\": executable file not found in $PATH": unknown
  ```

  

##### 종료 

```
#docker stop wordpress wordpressdb
```

- 컨테이너 끼리도 의존성이있음 wordpressdb없는 상태에서 wordpress 실행안됨

  ```
  [root@localhost han]# docker start wordpress
  Error response from daemon: Cannot link to a non running container: /wordpressdb AS /wordpress/mysql
  Error: failed to start containers: wordpress
  ```

  

#### 2.2.6 도커 불륨 

- 도커 이미지는 읽기전용

- 도커 컨테이너는 쓰기 가능이라 각종 정보 저장

- 문제점 : 컨테이너에 모든 값이 저장되어있어서 쉽게 추가 삭제가능한 컨테이너는 위험함

- 해결 : 데이터를 영속적(Persistent) 로 활용 

- 영속성 활용방법

  불륨 활용

  1. 호스트와 불륨 공유

     - 호스트 디렉터리를 컨테이너 디렉토리에 마운트 한다는 개념 , 호스트 디렉토리에 파일이있을 경우 덮어쓰기가 된다.

     - 데이터베이스

     ```
     # docker run -d --name wordpressdb_hostvolume -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=wordpress -v /home/han/wordpress_db:/var/lib/mysql mysql:5.7
     ```

     - 워드프레스

     ```
     # docker run -d -e WORDPRESS_DB_PASSWORD=password --name wordpress_hostvolume --link wordpressdb_hostvolume:mysql -p 80 wordpress 
     ```

     - -v 옵션이 추가되었는데 호스트의 /home/han/wordpress_db에 컨테이너 /var/lib/mysql을 공유한다는 의미
     - /home/han 에보면 wordpress_db 디렉토리가 있고 안에 파일이 생성됨

     ```
     -rw-r-----. 1 polkitd input       56  8월  1 15:53 auto.cnf
     -rw-------. 1 polkitd input     1679  8월  1 15:53 ca-key.pem
     -rw-r--r--. 1 polkitd input     1107  8월  1 15:53 ca.pem
     -rw-r--r--. 1 polkitd input     1107  8월  1 15:53 client-cert.pem
     -rw-------. 1 polkitd input     1679  8월  1 15:53 client-key.pem
     -rw-r-----. 1 polkitd input     1346  8월  1 15:53 ib_buffer_pool
     -rw-r-----. 1 polkitd input 50331648  8월  1 15:53 ib_logfile0
     -rw-r-----. 1 polkitd input 50331648  8월  1 15:53 ib_logfile1
     -rw-r-----. 1 polkitd input 79691776  8월  1 15:53 ibdata1
     -rw-r-----. 1 polkitd input 12582912  8월  1 15:53 ibtmp1
     drwxr-x---. 2 polkitd input     4096  8월  1 15:53 mysql
     drwxr-x---. 2 polkitd input     8192  8월  1 15:53 performance_schema
     -rw-------. 1 polkitd input     1675  8월  1 15:53 private_key.pem
     -rw-r--r--. 1 polkitd input      451  8월  1 15:53 public_key.pem
     -rw-r--r--. 1 polkitd input     1107  8월  1 15:53 server-cert.pem
     -rw-------. 1 polkitd input     1679  8월  1 15:53 server-key.pem
     drwxr-x---. 2 polkitd input     8192  8월  1 15:53 sys
     drwxr-x---. 2 polkitd input       20  8월  1 15:53 wordpress
     ```

     - 삭제

     ```
     # docker rm -f wordpress_hostvolume wordpressdb_hostvolume
     wordpress_hostvolume
     wordpressdb_hostvolume
     ```

     

  2. 불륨 컨테이너 활용

     - 1번 -v 를 이용하여 호스트와 불륨 공유를 한 컨테이너, 즉 불륨 컨테이너를  다른 컨테이너가 또 공유함 

     - --volume-from [불륨컨테이너 이름]
     - 컨테이너 1은 -v /home/han/db:/home/test/db로 생성
     - 컨테이너 2는 --volume-from 컨테이너1 으로 생성
     - 컨테이너 2가보면 /home/test/db가 있음
     - 즉 컨테이너2 /home/test/db=컨테이너1/home/test/db=호스트/home/han/db 이다.
     - 검증필요 확인안해봄

  3. 도커가 관리하는 불륨 생성

     - docker volume을 활용한 방법

     ```
     # docker volume create --name myvolume
     # docker volume ls
     
     DRIVER              VOLUME NAME
     local               07466fdbec7e20aad9f754df162190f8f7a213b60c8449d51ec53a0fa030df51
     local               19060e662dc6ece0a03f4ff8f951ce456db76a08592ca434e1e6b4d09e9ecd8a
     local               fd93372011d5bb5a2ea9057ea57b8bb571bf687e639bd6a1b6725063f9b27f4c
     local               myvolume
     ```

     - 위에 3개는 다른 컨테이너인가봄
     - -v [불륨의 이름]:[컨테이너 공유 디렉토리] 로 사용

     ```
     # docker run -i -t --name myvolume_1 -v myvolume:/root/ ubuntu
     # docker run -i -t --name myvolume_2 -v myvolume:/root/ ubuntu
     ```

     

- 외부에 데이터 저장하는구조 `Stateless` ,상태를 나타내는 것은 외부에 컨테이너자체는 상태가 없다.

- 반대로 컨테이너가 상태를 가지고 있는경우 `Stateful` 이라 한다.

#### 2.2.7 도커 네트워크

##### 도커 네트워크 구조

- 도커  컨테이너는 172.17.0.x 로 증가함

- 호스트에서 ifconfig 를 보면 실행중인 컨테이너만큼 veth 가 있는 것을 볼 수 있다

```
# ifconfig 
....
...
veth277baf5: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500
        inet6 fe80::ec6d:77ff:fe66:25aa  prefixlen 64  scopeid 0x20<link>
        ether ee:6d:77:66:25:aa  txqueuelen 0  (Ethernet)
        RX packets 19492  bytes 1584239 (1.5 MiB)
        RX errors 0  dropped 0  overruns 0  frame 0
        TX packets 26705  bytes 40273380 (38.4 MiB)
        TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0
```

- v는 virtual 을 뜻함 즉 Virtual eth 의미

- veth 로 생성된 인터페이스는 컨테이너 시작할때 생성되어 컨테이너의 eth0 와 연결됨

- docker0 는 브릿지로 veth 와 바인딩 되어 호스트 eth0과 연결해준다.

- 정리하면 컨테이너의 eth0는 호스트의 veth...과 연결되었있으면 veth는 docker0 브릿지와 바인딩 되어 외부와 통신할 수 있다.

- ```
  # yum install bridge-utils
  # brctl show docker0
  ```

##### 도커 네트워크 기능

- 컨테이너를 생성하면 기본적으로 docker0 브리지를 통해 외부와 통신한다.
- 사용자의 선택에 따라서 브리지, 호스트,논,컨테이너,오버레이 드라이버로 사용할 수도 있다.

```
# docker network ls
NETWORK ID          NAME                DRIVER              SCOPE
7bebcb65b643        bridge              bridge              local
b1994dff46eb        host                host                local
b885d02f2ed2        none                null                local
```

- 기능만 설명하고 예제넘김
- 브릿지
  - 새로운 브릿지만들어서 컨테이너 ip대역 바꿔서 할당가능, 172.17.0.2 -> 172.18.0.2  

- 호스트
  - 어플리케이션을 그대로 호스트에 인식 , 포트 포워딩 안함
- 논
  - 인터넷 단절
- 컨테이너 네트워크
  - 다른 컨테이너 네트워크를 공유받음
- 브릿지 네트워크와 --net-alias
  - ip하나에 여러개의 컨테이너 연결 ? 

#### 2.2.8 컨테이너 로깅

- Json-file 로그 사용

  도커는 컨테이너의 표준 출력(Std Out)과 에러(StdErr) 로그를 별도의 메타데이터 파일로 저장하며 docker logs로 확인

  - mysql 설치

    ```
    #docker run -d --name mysql -e MYSQL_ROOT_PASSWORD=1234 mysql:5.7
    8fd5a0dc8cabcadb1b66ca775c7412f8eb9907825c4673a3973f37a859e04ee9
    ```

    ##### -d 없이 실행할경우 로그들 다뜸

    ```
    .....
    2019-08-01T08:26:15.874582Z 0 [Note] Shutting down plugin 'MEMORY'
    2019-08-01T08:26:15.874588Z 0 [Note] Shutting down plugin 'CSV'
    2019-08-01T08:26:15.874593Z 0 [Note] Shutting down plugin 'sha256_password'
    2019-08-01T08:26:15.874596Z 0 [Note] Shutting down plugin 'mysql_native_password'
    2019-08-01T08:26:15.874746Z 0 [Note] Shutting down plugin 'binlog'
    2019-08-01T08:26:15.875340Z 0 [Note] mysqld: Shutdown complete
    
    
    MySQL init process done. Ready for start up.
    
    .....
    2019-08-01T08:26:16.405631Z 0 [Note] IPv6 is available.
    2019-08-01T08:26:16.405649Z 0 [Note]   - '::' resolves to '::';
    2019-08-01T08:26:16.405676Z 0 [Note] Server socket created on IP: '::'.
    2019-08-01T08:26:16.407144Z 0 [Warning] Insecure configuration for --pid-file: Location '/var/run/mysqld' in the path is accessible to all OS users. Consider choosing a different directory.
    2019-08-01T08:26:16.417866Z 0 [Note] Event Scheduler: Loaded 0 events
    2019-08-01T08:26:16.418357Z 0 [Note] mysqld: ready for connections.
    Version: '5.7.27'  socket: '/var/run/mysqld/mysqld.sock'  port: 3306  MySQL Community Server (GPL)
    ```

  - -d 넣고 실행하고 logs 보면 위와 똑같이나옴

    - --tail x 넣으면 마지막 x줄 표현

    ```
    # docker logs mysql
    # docker logs --tail 2 mysql
    ```

  - 환경변수 없이 생성

    ```
    # docker run -d  --name mysql mysql:5.7 
    # docker ps 
    ```

    - mysql 안켜짐

    ```
    # docker logs mysql
    
    error: database is uninitialized and password option is not specified 
      You need to specify one of MYSQL_ROOT_PASSWORD, MYSQL_ALLOW_EMPTY_PASSWORD and MYSQL_RANDOM_ROOT_PASSWORD
    ```

  - -t 는 시간, -f 는 로그를 스트림으로 볼 수 있다.

    ```
    # docker logs -f -t mysql
    ```

  - 우분투 bash입력 -> log에 입력됨

    ```
    # docker run -i -t --name ubuntu ubuntu
    
    root@92dd3da4ed48:/# echo ddd
    ddd
    root@92dd3da4ed48:/# exit
    
    # docker logs ubuntu
    
    root@92dd3da4ed48:/# echo ddd
    ddd
    root@92dd3da4ed48:/# exit
    ```

  - 컨테이너 로그는 Json 형식으로 저장됨

    ```
    # cat/var/lib/docker/containers/${CONTAINER ID}/${CONTAINER ID}-json.log
    ```

    

- syslog

  - 컨테이너의 로그는 json 뿐만아니라 syslog로 보내 저장하도록 설정할 수 있다.
  - syslog는 유닉스 계열 운영체제에서 로그를 수집하는 호래된 표준 중 하나로서, 커널, 보안 등 시스템과 관련된 로그, 애플리케이션의 로그 등 다양한 종류의 로그를 수집
  - 대부분 유닉스계열은 syslog에 사용하는 인터페이스가 동일

- 로그 넘김

#### 2.2.9  컨테이너 자원 할당 제한

- run과 create 에서 컨테이너 자원할당할수 있고 아무런 옵션이 없으면 컨테이너는 호스트자원을 제한없이 사용
- 가능하면 할당해주는게 좋음 => 다른 컨테이너나 호스트자체가 다운될수 있음
- 할당 정보는 inspect 가면 볼수있음

```
# docker inspect ubuntu
"HostConfig": {
....
"KernelMemory": 0,
"KernelMemoryTCP": 0,
"MemoryReservation": 0,
"MemorySwap": 0,
"MemorySwappiness": null,
"OomKillDisable": false,
"PidsLimit": null,
"Ulimits": null,
"CpuCount": 0,
"CpuPercent": 0,
"IOMaximumIOps": 0,
"IOMaximumBandwidth": 0
....
```

- update로 변경가능

```
# docker update [변경할 자원] [컨테이너 이름]
# docker update --cpuset-cpus=1 ubuntu
```

```
# docker inspect ubuntu

...
"CpusetCpus": "1"
...
```



##### 2.2.9.1 메모리 제한

- run 명령에 --memory 옵션으로 메모리를 제한할 수 있음
- 제한할수 있는 최소 메모리는 4MB

```
# docker run -d --memory="1g" --name memory_1g nginx

# docker inspect memory_1g | grep \"Memory\"
"Memory": 1073741824,
```



##### 2.2.9.2 CPU 제한

###### cpu-shares(cpu할당량 제한)

- 일반 VM은 특정 CPU개수를 할당 받는데 모든 컨테이너 작업은 CPU 스케쥴링에서 같은 비율로 처리됨 따라서 CPU 스케쥴링에서 해당 컨테이너가 CPU를 얼마나 차지할 것인가 설정해야함
- default 1024가 1  ,  상대적임 1번 컨테이너에서 1024 2번컨테이너에서 512 면 cpu 처리시 1번 컨테이너가  2번컨테이너보다 2배 정도 할당받음

```
# docker run -i -t --name cpu_share --cpu-shares 2048 ubuntu
```

###### cpuset-cpus(특정 cpu 제한)

- 호스트에 여러개의 cpu가 있을때 --cpuset-cpus 옵션으로 특정 cpu만 사용하도록 할 수 있다.

```
# docker run -d --name cpuset --cpuset-cpus=2 alicek106:stress stress --cpu1
```

- alicek106:stress는 stress가 설치되어있는 이미지, 백그라운드라 확인하고 꺼야함

###### cpu-period, cpu-quota

- 컨테이너 CFS(Completely Fair Scheduler) 주기는 기본적으로 100ms이지만 cpu-period, cpu-quota로 변경가능
-  cpu-period 기본값은 100000이며 100ms 를 뜻함, cpu-quota은 cpu-period의 시간에서 얼마나 할당받을지를 뜻함 25000일경우 총 100에서 25를 사용하는것임 ,즉 CPU성능이 1/4 정도로 감소

```
# docker run -d --name quota_1_4 --cpu-period=100000 --cpu-quota=25000 alicek106:stress stress --cpu1
```

```
# docker run -d --name quota_1_1 --cpu-period=100000 --cpu-quota=100000 alicek106:stress stress --cpu1
```

- 위에꺼는 할당량 25 아래꺼는 100으로잡힘

###### cpus(제일 편함)

- cpus를 0.5 로 설정하면 cpushare=512, 또는 cpu-period=100000 cpu-quota=50000과 동일하게 컨테이너 CPU를 제한할 수 있다.

##### 2.2.9.3 Block I/O 제한

- 컨테이너 입출력제한 
- device-write-bps,device-read-bps  : 절대값으로 정의

```
# docker run -it --device-write-bps [디바이스 이름]:[값] ubuntu:14.04
```

-  device-write-iops, device-read-iops : 상대값으로 정의

```
# docker run -it --device-write-iops [디바이스 이름]:[값] ubuntu:14.04
```

- iops로 두개의 도커를 실행시킬때 iops값을 하나는 5 하나는 10줄 경우 속도가 두배차이남

##### 2.2.9.4 컨테이너 저장 공간 설정

- DeviceMapper사용해야만 가능
- 17.03.0-ce기준 컨테이너 저장 공간 크기를 개별적으로 설정하는 기능은 지원안함
- 넘어감



###  2.3 도커 이미지 다루기

- 모든 컨테이너는이미지 기반으로 생성
- 일반적으로 pull할경우 Docker Hub에서 이미지 내려받음
- docker search 로 허브사이트 검색, stars 가 즐겨찾기 같은 용도

```
# docker search ubuntu

NAME                                                      DESCRIPTION                                     STARS               OFFICIAL            AUTOMATED
ubuntu                                                    Ubuntu is a Debian-based Linux operating sys…   9805                [OK]                
dorowu/ubuntu-desktop-lxde-vnc                            Docker image to provide HTML5 VNC interface …   330                                     [OK]
rastasheep/ubuntu-sshd                                    Dockerized SSH service, built on top of offi…   226                                     [OK]
consol/ubuntu-xfce-vnc                                    Ubuntu container with "headless" VNC session…   185                                     [OK]
ubuntu-upstart                                            Upstart is an event-based replacement for th…   99                  [OK]                
ansible/ubuntu14.04-ansible                               Ubuntu 14.04 LTS with ansible                   97                                      [OK]
neurodebian                                               NeuroDebian provides neuroscience research s…   58                  [OK]                
1and1internet/ubuntu-16-nginx-php-phpmyadmin-mysql-5      ubuntu-16-nginx-php-phpmyadmin-mysql-5          50                                      [OK]
ubuntu-debootstrap                                        debootstrap --variant=minbase --components=m…   40                  [OK]                
i386/ubuntu                                               Ubuntu is a Debian-based Linux operating sys…   18                                      
1and1internet/ubuntu-16-apache-php-5.6                    ubuntu-16-apache-php-5.6                        14                                      [OK]

.....

```



#### 2.3.1 도커 이미지생성

##### docker commit [Option] CONTAINER [REPOSITORY[:TAG]]

컨테이너의 변경사항을 이미지로 저장할때 사용

```
# docker commit -a "openbase" -m "first commit" ubuntu myubuntu:1.0
```



#### 2.3.2 이미지 구조 이해

- 도커 이미지는 레이어 형태로 존재

```
# docker inspect openjdk:8-jdk-alpine

"Layers": [                "sha256:f1b5933fe4b5f49bbe8258745cf396afe07e625bdab3168e364daf7c956b6b81",
"sha256:9b9b7f3d56a01e3d9076874990c62e7a516cc4032f784f421574d06b18ef9aa4",
"sha256:ceaf9e1ebef5f9eaa707a838848a3c13800fcf32d7757be10d4b08fb85f1bc8a"]
```

- 위와 같이 보통 이미지는 Layer를 가지고 있다. 
- Layer는 Git에서 Commit 주소라고 생각하면 편하다. 
- 변경사항들의 주소를 나타냄

```
기본이미지(layer0) : 100 Mb
변경사항1(layer1) : 1 Mb
변경사항2(layer2) : 1 Mb
총 용량 : 102Mb
```



#### 2.3.3 이미지삭제 

```
# docker rmi [이미지]
```

- 사용중인 이미지 삭제하면 이름만지워지고 이미지는 none 으로 바뀜
- 이를 댕글링 이미지라하며  아래와같이 확인가능

```
# docker images -f dangling=true

REPOSITORY               TAG                 IMAGE ID            CREATED             SIZE
<none>                  <none>              a91fe6079ea7        2 days ago          120MB
```

- 댕글링 이미지 삭제는 아래와 같이함

```
# docker image prune 
```

- 추천 삭제방법

```
# docker stop [컨테이너] && docker rm [커네이너]
# docker rmi [이미지]
```

- 이미지 삭제시 Untagged:는 이미지에 부여된 이름을 삭제한다는 뜻, 실제 레이어 삭제는 Deleted:..으로 표시

#### 2.3.4 이미지 저장 및 로드

- 이미지를 단일 바이너리 파일로 저장할때 save기능사용
- docker save -o [파일이름] [이미지]

```
# docker save -o ubuntu_14_04.tar ubuntu:14.04
```

- 추출된 이미지는 docker load 로 도커에 로드할 수 있다.

```
# docker load -i ubuntu_14_04.tar
```



- 비슷한 용도 export,import 가있음

#### 2.3.5 이미지 배포

- 도커 허브를 이용하는 방법과 사설 저장소를 이용하는 방법이있다.

##### 도커허브

- 기본기능으로 push, pull 쉽게 할 수 있다.
- docker hub에 가입하면, private 1개 를 무료로 준다.
- 순서는 로그인, 이미지 태그이름바꾸기, 올리기

```
# docker login 
... 아이디 비번....
Login Succeeded
```

- 이미지 이름변경 , 허브아이디/이미지:버젼 형식으로 변경

```
# docker tag [변경전 태그] [변경후 태그]

ex)
# docker tag myimage:1.0 kjhhjk1234/myimage:1.0
```

- 업로드 및 다운로드

```
//업
# docker push kjhhjk1234/myimage:1.0

//다운
# docker pull kjhhjk1234/myimage:1.0
```

- 조직 & 팀 저장소 도 있음.



##### 사설 저장소

- 사설 저장소를 이용하게하는 공식 도커 이미지가 있다.

```
# docker run -d --name myregistry -p 5000:5000 --restart=always registry:2.6
```

- restart 는 컨테이너가 정지될때마다 재시작에 대한 정책
- `always ` : 컨테이너가 종료된 상태면 계속 재시작
- `on-failure :[횟수] ` :  컨테이너 종료코드가 0이 아닐때 횟수만큼 재시작
- `unless-stopped `:  stop으로 종료되기전 까지 계속 재시작



- 업로드

```
# docker tag myimage:1.0 ${DOCKER_HOST_IP}:5000/myimage:1.0
```

- DOCKER _HOST_IP 에는 사설 저장소 ip 입력, 도메인도 가능
- registry 는 자동으로 Volume 을 생성함 , 따라서 레지스트리 삭제시 --volumes 가 들어가야함

```
# docker rm --volumes myregistry
```



- PUSH

```
# docker push 192.168.0.1:5000/myimage:1.0
```

- HTTPS 사용안 할경우 에러발생, 기본적으로 도커 데몬은 HTTPS 사용하지 않는 레지스트리 컨테이너에 접근 못하게함
- 도커 데몬 시작 옵션에 다음을 입력하고 실행

```
DOCKER_OPTS="--insecure-registry=192.168.0.1:5000"
```



###### nginx 를 사용한 HTTPS 인증



- 인증서가 없을 경우 스스로 인증한(Self-Signed) 인증서와 키를 발급함으로써 TLS를 적용하는 방법

```
# mkdir certs

# openssl genrsa -out ./certs/ca.key 2048
Generating RSA private key, 2048 bit long modulus
...

# openssl req -x509 -new -key ./certs/ca.key -days 10000 -out ./certs/ca.crt
Country Name (2 letter code) [XX]:
State or Province Name (full name) []:
Locality Name (eg, city) [Default City]:
Organization Name (eg, company) [Default Company Ltd]:
Organizational Unit Name (eg, section) []:
Common Name (eg, your name or your server's hostname) []:
Email Address []:

```

- 입력정보는 모두 빈칸으로해도 된다.

```
# openssl genrsa -out ./certs/domain.key 2048
Generating RSA private key, 2048 bit long modulus
...
# openssl req -new -key ./certs/domain.key -subj /CN=${DOCKER_HOST_IP} -out ./certs/domain.csr

# echo subjectAltName = IP:${DOCKER_HOST_IP} > extfile.cnf

# openssl x509 -req -in ./certs/domain.csr -CA ./certs/ca.crt  -CAkey ./certs/ca.key -CAcreateserial -out ./certs/domain.csr -days 10000 -extfile extfile.cnf 
Signature ok
subject=/CN=..
Getting CA Private Key
```

- 계정 설정(htpasswd 없을시 `yum install httpd-tools` ,우분투 `apt-get install apache2-utils`)

```
# htpasswd -c htpasswd openbase
New password: 
Re-type new password: 
Adding password for user openbase
# mv htpasswd certs/
```





---- nginx  부분은 나중에추가





### 2.4  Dockerfile

- java application을 실행한다고할때 openjdk docker이미지 를 받아서  app.jar을 추가하고 java -jar app.jar로 실행 하는 이미지를 만든다고 할때 이 복잡한 과정을 한번에 해결해주는 빌드 방법
- jdk 자체 도커도 OS  Layer를 불러옴

```
FROM openjdk:8-jdk-alpine
ADD myapplication.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT ["java","-jar","/app.jar"]
```

- 위와 같이 도커파일을 만들고 빌드하면 나의 어플리케이션이 실행되는 이미지를 만들 수 있다.
- build 명령어로 실행

#### 2.4.1 Dockerfile 명령어

- 기본기능 설명

  - ###### ARG

    - 상수 설정으로 밑단에서 ${상수명} 으로 사용가능

    ```
ARG  CODE_VERSION=latest
    FROM base:${CODE_VERSION}
    ```
```
    
- ###### FROM 
  
  - BASE 이미지로 Dockerfile 작성시 반드시 한 번 이상 입력하며 포맷은 docker run 이미지명 사용할 때랑 같음, 이미지없을시 자동으로 pull함
  
- ###### MAINTAINER(deprecated)
  
  - 이미지를 생성한 개발자의 정보를 나타냄, 작성한 사람과 연락처(메일) 등을 입력
  
- ###### LABEL
  
  - 이미지에 메타데이터를 추가 , 키:값으로 저장, 추가된 메타데이터는 inspect로 확인가능
  
- ###### RUN
  
  - **빌드시** 명령어 실행시키고 커밋시켜서 새로운 Layer 를 만들음
    - 이미지를 만들기 위해 컨테이너 내부에서 명령어를 실행, apt-get update, apt-get install 등 ,,
  - Run을 여러개 써도되고, [] 형태로 써도됨
  
```
    RUN <command>
    RUN ["명령어", "param1", "param2"]
    ```
      
    - &&을 사용하여 하나의 Run으로 해결하는것이 많은레이어 생성안하고 좋다
      
    ```
    RUN mkdir /test &&fallocate -l 100m /test/dummy && rm /test/dummy
    ```


​    

  - ###### CMD
  
    - **The main purpose of a CMD is to provide defaults for an executing container.**
    - 컨테이너 run할때 명령어 실행, 기본값 설정이 주, 실행은 Entrypoint로 분리시키는것이 좋음
    - Dockerfile에서 한번만 가능
    - RUN은 빌드 될때 실행, CMD는 빌드시 어떠한 작용안함
    
    ```
    CMD ["명령어","param1","param2"] (exec form, this is the preferred form)
    CMD ["param1","param2"] (as default parameters to ENTRYPOINT)
    CMD command param1 param2 (shell form)
    ```
  
  - ###### ENTRYPOINT
  
    - 컨테이너 run할때 명령어 실행
    - CMD는 기본값으로 ENTRYPOINT가 겹치면 모두 오버라이드함 
    
    ```
    ENTRYPOINT ["executable", "param1", "param2"] (exec form, preferred)
    ENTRYPOINT command param1 param2 (shell form)
    ```
  
  - ###### ADD
  
    -  파일을 이미지에 추가
    - Json 형태로 {"추가파일이름","컨테이너 추가될 위치"}
  
  - ###### COPY
  
    - 복사
    
    ```
    COPY <src>... <dest>
    COPY ["<src>",... "<dest>"] (this form is required for paths containing whitespace)
    ```
  
    ```
    COPY test relativeDir/   # adds "test" to `WORKDIR`/relativeDir/
    COPY test /absoluteDir/  # adds "test" to /absoluteDir/
    ```
  
    
  
  - ###### WORKDIR
  
    - 빌드시 cd 와 같다고 생각하면 됨
    
    ```
    WORKDIR /a
    WORKDIR b
    WORKDIR c
    RUN pwd
    
    The output of the final pwd command in this Dockerfile would be /a/b/c.
    ```
  
    
  
  - ###### EXPOSE
  
    - 도커에게 컨테이너 포트 알리는 용도
    
    ```
    EXPOSE <port> [<port>/<protocol>...]
    ```
  
  - ###### ENV
  
    - 도커 환경설정
    
    ```
    ENV <key> <value>
    ENV <key>=<value> ...
    ```
  
  - Onbuild,Stopsignal Healthcheck,Shell ...

#### 2.4.2 Dockerfile 빌드

##### 2.4.2.1 이미지생성

```
# docker build -t mybuild:0.0 ./
```

- -t 는 태그, -t 안쓰면 16진수형태로 저장되니까 가급적이면 -t옵션 사용
- ./ 은 빌드 컨텍스트 위치
- -f 로 dockerfile 위치 지정할 수도 있음

```
# docker run -d -P mybuild:0.0
```

- -P는 expose로 입력해놨던 모든 포트 여는 옵션

###### 캐시

- 전에 빌드시 만들었던 레이어를 캐시로 사용하여 속도를 증가 하지만 git clone같은 명령어를 사용할때 캐시를 사용하면 깃 서버에서 변경이 일어나도 변경되지않음, git clone를 사용한 레이어가 캐시로 저장되어있기때문

### 2.3 Docker Deamon

- 위치 확인

```
# which docker
/usr/bin/docker
```

- 실행중인 프로세스목록

```
# ps aux | grep docker
```



- 명령어를 처리하는 docker랑 프로세스 docker 랑 다름 , 명령어를 처리하는 docker 는 클라이언트 docker, 프로세스는 서버 docker임

- 명령어 처리과정

  1. docker ps 입력
  2. /usr/bin/docker(클라이언트)가 /var/run/docker.sock 유닉스 소켓을 사용해 도커데몬에게 명령어 전달
  3. 도커 데몬은 명령어 파싱 후 작업 수행
  4. 수행결과  클라이언트에게 반환

- 도커 데몬실행

  ```
  # service docker start
  # service docker stop
  
  //시작시자동실행
  # systemctl enable docker 
  ```

  

###### 

- 모니터링
  - 많은 수의 도커서버를효율적으로 관리, 컨테이너 개발하다 문제생겼을때  등 모니터링해야함
  - 

```
실시간 상태
# docker events
시스템상태
# docker stats
볼륨상태
# docker df
```

- 도커 모니터링도구 CAdvisor 



###### ...  중요한거 많은데 시스템적인거라 도커데몬 넘어감 

## 3. Docker Swarm

- 병렬처리, 여러대 물리서버 연결 방법
- 지금 사용안하기때문에 나중에봄

## 4. Docker Machine

- 이것도 필요없음

## 5. Docker Compose

- 도커 컴포즈는 여러개의 컨테이너의 옵션과 환경을 정의한 파일을 읽어 컨테이너를 순차적으로 생성하는 방식

- docker run으로 여러가지 컨테이너를 실행하는 것보다  도커 컴포즈로 실행 하는 것이 좋음

### 5.1 Docker Compose 설치

```
$ sudo curl -L "https://github.com/docker/compose/releases/download/1.24.1/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose

$ sudo chmod +x /usr/local/bin/docker-compose
```

### 5.2  간단한 연습

#### 5.2.1 docker-compose.yml

```
version: '3.6'
services:
  nginx:
    image: nginx
    container_name: nginx
    ports:
      - '80:80'
```

- http://localhost 접속
- yml 은 tab 인식못하고 space 두칸으로 하위 항목 구성

#### 5.2.2 옵션

- -f :  docker-compose.yml 파일 직접 입력, 없을 시 현재폴더 docker-compose.yml 참조
- -d : background

#### 5.2.3 실행

```
$ docker-compose -f ./docker-compose.yml up -d 
```

#### 5.2.4 멈춤

```
$ docker-compose down
```



- container_name 없을 시 [프로젝트 이름]\_[서비스 이름]_[서비스 내에서 컨테이너의 번호 ]로 저장
- 프로젝트 이름은 docker-compose.yml 이 저장되어 있는 디렉토리 이름



### 5.3 Docker compose 사용

#### 5.3.1 docker-compose.yml 구성

1.  version 
   
   - yml 포맷 버젼 도커 스웜 모드와 호환되는 버전은 3 ,  가능하면 최신버젼 사용
   
2. services
   
   - 서비스는 도커 컴포즈로 생성할 컨테이너 옵션을 정의
   
3. image
   
   - 컨테이너 이미지 이름 , 이름포맷은 docker run 과 같다. 
   
4. links
   
   - 다른 서비스에 서비스명으로 접근할 수 있도록 설정
   
5. environment 
   
   - 컨테이너 내부에서 사용할 환경변수를 지정, 딕셔너리나 배열 형태로 사용
   
6. command
   
   - 컨테이너가 수행될 때 수행할 명령어 설정
   
7. depends_on
   
   - 특정 컨테이너에 대한 의존관계를 나타내며 이항목에 명시된 컨테이너가 먼저 생성되고 실행됨
   
8.  ports
    
    - 서비스의 컨테이너를 개방할 포트를 설정
    
9. build

   - Dockerfile로 이미지를 빌드하여 컨테이너 생성

10. extends

    - 다른 yml 파일을 상속

    - ```
      #docker-compose.yml
      version: '3.0'
       services:
       	web:
       		extends:
       			file: extend_compose.yml
       			service : extend_web
       		
      #extend_compose.yml
      version: '3.0'
       services:
       	extend_web:
       	image:ubuntu:14.04
          port:
           - "80:80"
      ```

      

#### 5.3.2 네트워크 정의 

- driver : 
  - docker-compose 로 생성된 컨테이너는 브릿지 타입의 네트워크를 생성
  - driver 항목을 정의해 브릿지아닌 네트워크를 사용하도록 설정할 수 있다.
    - driver: bridge # 네트워크에서 사용할 드라이버 종류 (overlay, host, none 등…
    - internal : true # 내부망 설정 옵션
    - ipam:
          driver: default # ipam에서 사용할 드라이버 옵션 기본 설정은 default
          config: # ipam config
              – subnet : 10.0.0.0/24 # subnet 설정 ps. gateway설정은 version 2에서만 지원된다.