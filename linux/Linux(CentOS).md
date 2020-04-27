# Linux(CentOS)

## 1. 명령어(CLI)

```shell
사용자 보기
cat /etc/passwd 
사용자 보기 아이디만
cut -f1 -d: /etc/passwd
이더넷 장치 보기
# nmcli d
이더넷 설정
# nmtui
네트워크 재부팅
# systemctl restart network

```

## 2. 네트워크 설정

### ip설정

ip 설정은 3가지 방법이 있다.

```shell
이더넷 장치 보기
# nmcli d 
```

1.  설정파일 변경

   ```shell
   설정 폴더
   # vi /etc/sysconfig/network-scripts/ifcfg-eth0
   ```

   다음과 같이 나옴

   ```properties
   TYPE=Ethernet
   BOOTPROTO=dhcp
   DEFROUTE=yes
   PEERDNS=yes
   PEERROUTES=yes
   IPV4_FAILURE_FATAL=no
   IPV6INIT=yes
   IPV6_AUTOCONF=yes
   IPV6_DEFROUTE=yes
   IPV6_PEERDNS=yes
   IPV6_PEERROUTES=yes
   IPV6_FAILURE_FATAL=no
   IPV6_ADDR_GEN_MODE=stable-privacy
   NAME=eth0
   UUID=996b6722-4260-44bc-9311-7f9dedc333ea
   DEVICE=eth0
   ONBOOT=no
   ```

   유동 IP(DHCP)를 사용하는 경우

   ```properties
   BOOTPROTO=dhcp
   ONBOOT=yes
   ```

   고정 IP(Static)을 사용하는 경우

   ```properties
   #static이나 none 둘중 하나
   BOOTPROTO=none
   #BOOTPROTO=static
   
   ONBOOT=yes
   IPADDR=10.20.30.41
   #prefix나 netmask 둘중 하나 입력
   PREFIX=24
   #NETMASK=255.255.255.0
   GATEWAY=10.20.30.254
   DNS1=168.126.63.1
   DNS2=168.126.63.2
   ```

2. IP 설정 UI

   ```shell
   # nmtui
   ```

   1. Edit a connection
   2. eth() 선택
   3. 유동 IP -> Automatically  connect 체크후 OK
   4. 고정 IP -> IPv4 CONFIGURATION을 <Automatic>에서 <Manual>로 변경
      우측 Show를 눌러 수동설정
      Automatically  connect 체크 후 OK
   5. systemctl restart network

## 3. 사용자 설정

### 사용자 추가 및 삭제

```
# useradd [옵션] 계정이름
```

-g : 그룹

```
# groupadd group1
# useradd -g group1 test2 
```

그룹1에 테스트2 계정만듦, 그룹1은 이미 만들어놓음

```
# userdel [옵션] 계정이름
```

-r : 홈 디렉토리 삭제

```
# userdel -r test1
```



### 사용자 확인

```
# cat/etc/passwd
```



### 사용자 비밀번호 설정

```
# passwd 계정이름
```



### 그룹 추가 삭제

```
# groupadd group1
# groupdel group1
```



### 유저 정보 조회

```
# id [옵션] 계정이름
```

-g : 기본그룹의 GID 출력

-G : 사용자가 속한 모든그룹의 GID 출력

-u : 사용자의 UID 출력

-n : 위의 옵션과 사용하며 이름도 함께 출력

### 사용자 정보수정

```
# usermod
```

-c : 사용자의 설명을 수정
-d : 홈디렉토리를 변경
-m : 홈디렉토리 변경시 파일을 옮긴다.
-e : 계정종료일 변경
-s : 기본 쉘 변경
-u : UID변경
-g : 기본 그룹 변경
-G : 추가 그룹 변경
-l : 사용자명 변경
-L : 사용자 패스워드 LOCK (로그인 불가)
-U : 패스워드  LOCK을 푼다.



## 4. Yum

RPM 기반 시스템용 패키지 설치/삭제/업데이트 도구

|                      기능                      |         명령어         |
| :--------------------------------------------: | :--------------------: |
|                  패키지 설치                   | `yum install 패키지명` |
|                  패키지 삭제                   | `yum remove 패키지명`  |
|                패키지 업데이트                 | `yum update 패키지명`  |
|                패키지 정보 확인                |  `yum info 패키지명`   |
|                  패키지 검색                   |  `yum search 검색어`   |
| 패키지 목록 보기 (설치된 항목 + 설치가능 항목) |       `yum list`       |
|            설치된 패키지 목록 보기             |  `yum list installed`  |

- yum 패키지매니저 관련 유틸리티 모음

```
# yum install -y yum-utils
```



- Yum을 이용한 package 다운로드

  1. yum을 이용한 rpm download

     - yum을 통해 rpm 패키지를 설치하면, rpm 패키지를 자동으로 다운받아
       자동으로 설치까지 진행된다. 

     - yum을 통해 패키지를 설치하지 않고, 해당 패키지를 download만 수행하는 방법이 있다.

     - 먼저 yum-downloadonly 패키지를 설치한다.

       ```
       # yum install yum-downloadonly
       ```

     - 그런 후 yum 옵션에 --downloadonly 를 추가해 주면 된다. 만일, 패키지가 저장되는 디렉토리를 지정해야 한다면옵션을 추가해주면 된다. 

       ```
       ex) # yum install libxml2 -y --downloadonly --downloaddir=/usr/local/src
       ```

  2. yumdownloader 를 이용한 rpm download

     - yumdownloader 명령을 사용하려면 우선 설치를 해야한다.

       ```
       # yum install yum-utils
       ```

     - 사용방법: 

       ```
       ex) # yumdownloader --resolve --destdir=/home/user/downdir qt-* 
       --resolve 옵션: 의존되는 모든 패키지들도 다운받도록 해준다.
       --destdir 옵션: 저장되는 디렉토리를 지정할 수 있다.
       ```

       

## 5.Tar

압축하기 

```
#> tar [-옵션] [압축파일 이름] [압축할 파일 이름]
```

특정 경로로 파일 압축 

```
#> tar [-옵션] [압축파일이 생성될 곳과 이름] [압축할 파일 이름]
```

압축풀기

```
#> tar [-옵션] [압축파일 이름]  
```

특정 경로에 압축 풀기

```
#> tar [-옵션-] [압축파일 이름] -C [압축파일이 풀어질 경로]
```

주요 옵션값

-c : 새로운 아카이브 생성
-x : 압축 해제
-v : 압축시에 진행률을 보여줌
-z : gzip 압축 및 압축해제
-f : 파일 이름 지정
-C : 압축 해제시 경로 지정



**1. 압축하기 예제**
**test** 라는 파일을 압축한다고 가정해봅시다. 
(압축할 파일이 있는 경로로 이동합니다)

[root@server]# tar -cvzf test.tar.gz test

이렇게 명령어를 입력하면 현재 경로에 test.tar.gz 파일이 생성되었습니다.

다른 곳에 압축파일을 남기고 싶다면 경로를 입력해줍니다.
test 파일의 압축본을 /var/log 에 만든다고 가정합시다.
(실수할수 있으므로 절대경로를 입력해줍시다)

[root@server]# tar -cvzf **/var/log/**test.tar.gz test

이렇게 하면 /var/log 경로 밑에 test.tar.gz 파일이 생성됨을 볼수 있습니다.



**2. 압축풀기 예제**
**test..tar.gz** 라는 파일을 압축해제 해봅시다. 
(압축을 풀 파일이 있는 경로로 이동합니다)

[root@server]# tar -xvzf test.tar.gz

압축파일 이름만 입력하면 해당 경로에 압축이 풀립니다.
다른 경로에 압축을 풀려면 -C (대문자 C) 로 경로를 지정해줍니다.
이번에는 홈(/home) 경로에다가 풀어보겠습니다.

[root@server]# tar -xvzf test.tar.gz -C /home

이때 압축 되어 있던 파일의 이름은 지정해줄 필요가 없으므로
-C 옵션 뒤에 경로만 지정해주면 /home 경로 밑에 압축파일이 풀립니다.



**3. 심화**

tar 명령어 자체는 파일을 압축한다기 보다는 파일을 하나로 묶어주는 개념입니다.
압축시에 -z 옵션이 붙어서 파일을 압축하는 것이죠.

test 파일을 압축한다고 가정했을때

**-z 옵션을 넣으면 `test.tar.gz` 라고 파일 이름을 명명해주어야 하고**
**옵션을 넣지 않았으면 `test.tar` 이라고 압축파일 이름을 설정해줘야 합니다.**

예를들어 test.tar 이란 파일을 압축해제 하기 위해선
-xvzf 가 아닌 -xvf 라는 명령어로 해 주어야 합니다.

.zip 파일은 tar이 아닌 uzip 명령어로 해제해줍니다.





## 6. 스크립트

### 파라메터

```
#!/bin/bash
echo "파라미터 개수 : $#"
echo "첫 번째 파라미터: $1"
echo "모든 파라미터 내용 : $@"

실행 결과: 
[test@localhost ~]# test.sh bread star
파라미터 개수 : 2
첫 번째 파라미터: bread
모든 파라미터 내용 : bread star
```



```
#! 커맨드
$# 파라미터 개수
$1 ,2,3 파라미터
$@ 모든 파라미터
```



### if 문

```
if [ 값1 조건식 값2 ];then

   수행문

 fi 
```

-   if 한칸 띄고 [ 넣고 값과 조건식을 넣어야 하는데 띄어쓰기도 틀리면 스크립트 에러가 나므로 유의하셔서 작성 하시기 바랍니다.

#### 조건식

```
[ -z ] : 문자열의 길이가 0이면 참
[ -n ] : 문자열의 길이가 0이 아니면 참

[ -eq ] : 값이 같으면 참
[ -ne ] : 값이 다르면 참
[ -gt ] :  값1 > 값2
[ -ge ] : 값1  >= 값2
[ -lt ] : 값1 < 값2
[ -le ] : 값1 <= 값2

[ -a ] : &&연산과 동일 and 연산
[ -o ] : ||연산과 동일 xor 연산

[ -d ] : 파일이 디렉토리면 참
[ -e ] : 파일이 있으면 참
[ -L ] : 파일이 심볼릭 링크면 참
[ -r ] : 파일이 읽기 가능하면 참
[ -s ] : 파일의 크기가 0 보다 크면 참
[ -w ] : 파일이 쓰기 가능하면 참
[ -x ] : 파일이 실행 가능하면 참
[ 파일1 -nt 파일2 ]  : 파일1이 파일2보다 최신파일이면 참
[ 파일1 -ot 파일2 ]  : 파일1이 파일2보다 이전파일이면 참
[ 파일1 -ef 파일2 ] : 파일1이 파일2랑 같은 파일이면 참
```

