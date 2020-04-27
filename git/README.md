# Git 시작하기 

## 1.버전관리란 : 파일변화를 시간에 따라 기록하여 과거 특정 시점의 버전을 다시 불러올수 있는 시스템

### 버전관리시스템(Version Control System, VCS) 종류 
1. 로컬에서 폴더별로 관리
2. 로컬버전관리 (Local VCS) : 파일변화에대한 날짜를 데이터베이스에 저장 
3. 중앙집중식 버전 관리 시스템(Centralized Version Control System; CVCS) : 외부 사용자 개발자 작업 문제를 해결
4. 분산 버전 관리 시스템(Distributed Version Control System; DVCS) : CVCS에서 서버 고장나면 복구하기 전까지 모든게 불가능의 문제를 가진다.
	DVCS는 마지막 스냅샷을 가져오는대신 저장소를 통째로 복제 -> 서버문제 발생해도 복제된 저장소를 다시서버로 복사하면 서버가 복구


## 2.Git 기초

### 델타가 아니라 스냅샷

![Alt Text](https://git-scm.com/figures/18333fig0104-tn.png)

각 파일에 대한 변화를 저장하는 시스템


![Alt Text](https://git-scm.com/figures/18333fig0105-tn.png)

Git은 파일에 변화가 없으면 따로 저장안하고 링크만 저장

### 거의 모든 명령을 로컬에서 실행
모든 히스토리가 로컬에있음 -> 오프라인에서 커밋하여 기록을 남김

### Git의 무결성
git은 모든 데이터를 저장하기 전에 체크섬(또는 해시)을 구하고 그 체크섬으로 테이터를 관리 

### 세가지 상태

![Alt Text](https://git-scm.com/figures/18333fig0106-tn.png)

Git은 파일을 Committed, Modified, Staged 이렇게 세 가지 상태로 관리한다. Committed란 데이터가 로컬 데이터베이스에 안전하게 저장됐다는 것을 의미한다. Modified는 수정한 파일을 아직 로컬 데이터베이스에 커밋하지 않은 것을 말한다. Staged란 현재 수정한 파일을 곧 커밋할 것이라고 표시한 상태를 의미한다.

Git으로 하는 일은 기본적으로 아래와 같다:
* 워킹 디렉토리에서 파일을 수정한다.
* Staging Area에 파일을 Stage해서 커밋할 스냅샷을 만든다.
* Staging Area에 있는 파일들을 커밋해서 Git 디렉토리에 영구적인 스냅샷으로 저장한다.

## 3. Git 설치

두가지 방법이있는데 소스코드로 설치하거나 OS 패키지로 설치한다.

### 리눅스
<pre><code> yum install git-core </code></pre>
    우분투
<pre><code> apt-get install git</code></pre>
### 윈도우
 <http://msysgit.github.com/>

## 4. Git 최초 설정(한번만)

* /etc/gitconfig 파일: 시스템의 모든 사용자와 모든 저장소에 적용되는 설정이다. git config --system 옵션으로 이 파일을 읽고 쓸 수 있다.
* ~/.gitconfig 파일: 특정 사용자에게만 적용되는 설정이다. git config --global 옵션으로 이 파일을 읽고 쓸 수 있다.
* .git/config: 이 파일은 Git 디렉토리에 있고 특정 저장소(혹은 현재 작업 중인 프로젝트)에만 적용된다. 각 설정은 역순으로 우선시 된다. 그래서 .git/config가 /etc/gitconfig보다 우선한다.

* 윈도용 Git은 $HOME 디렉토리(%USERPROFILE% 환경변수)에 있는 .gitconfig 파일을 찾는다. 보통 C:\Documents and Settings\$USER 또는 C:\Users\$USER 이다(윈도우에서는 $USER 대신 %USERNAME%를 사용한다). 그리고 msysGit도 /etc/gitconfig를 가지고 있다. 경로는 MSys 루트에 따른 상대 경로다. 인스톨러로 msysGit을 설치할 때 설치 경로를 선택할 수 있다.

### 사용자 정보

<pre><code> 
$ git config --global user.name "John Doe"
$ git config --global user.email johndoe@example.com
</code></pre>

* --global은 전체 옵션이라 한번 설정해놓으면 매번 커밋때마다 같은 옵션으로 작동 , 매번 커밋때마다 이메일,이름을 다르게하고싶으면 global 빼고 입력

### 편집기
* 기본 Vi나 Vim 인데 Emacs같은 다른거 사용하고싶으면 다음과 같이 실행
<pre><code> 
$ git config --global core.editor emacs
</code></pre>

### Diff 도구
* Merge 충돌을 해결하기 위해 사용하는 Diff 도구를 설정 할 수 있음, Vimdiff 사용하고싶으면 아래와같이 실행
<pre><code> 
$ git config --global merge.tool vimdiff
</code></pre>
* kdiff3, tkdiff, meld, xxdif, emerge, vimdiff, gvimdiff, ecmerge, opendiff 이렇게있음

### 설정확인 
<pre><code> 
$ git config --list
</code></pre>


<hr/>
# 2.Git의 기초

## 2.1 Git 저장소 만들기

### 기존 디렉토리를 git 저장소로 만들기

<pre><code> 
$ git init
</code></pre>
이 명령은 .git 이라는 하위 디렉토리를 만듦 .git에는 저장소 필요한 뼈대 구성

<pre><code> 
$ git add *.c
$ git add README
$ git commit -m 'initial project version'
</code></pre>


### 기존 저장소 Clone

git clone [url] 명령으로 저장소를 clone 

* 예제
<pre><code> 
$ git clone git://github.com/schacon/grit.git
</code></pre>

위의 명령은 grit 이라는 디렉토리만들고 그 안에 .git 디렉토리를 만든다.
그리고 저장소의 모든데이터를 가져와서  가장 최신버전을 Checkout해 놓는다.

디렉토리 이름 바꾸고싶으면 다음과 같이 실행
<pre><code> 
$ git clone git://github.com/schacon/grit.git mygrit
</code></pre>

* Git은 다양한 프로토콜을 지원한다. 이제까지는 git:// 프로토콜을 사용했지만 http(s)://를 사용할 수도 있고 user@server:/path.git처럼 SSH 프로토콜을 사용할 수도 있다. 


## 2.2 수정하고 저장소에 저장하기

워킹디렉토리는 Tracked(관리대상)과 Untracked(비관리대상) 으로 나뉨
Tracked파일은 Unmodified(수정하지않음) Modified(수정함) Staged(저장소에 기록)상태중 하나
처음 Clone 하면(Tracked) 모두 Unmodified 상태가 됨

![alt ](https://git-scm.com/figures/18333fig0201-tn.png)

### 파일 상태 확인 
 git status로 파일 상태 확인, clone한 후 이 명령 실행하면 다음과 같은 메세지를 볼 수 있음
<pre><code> 
$ git status
On branch master
nothing to commit, working directory clean
</code></pre>
기본 브런치가 master이기 때문에 master로 나오고, 커밋할 파일은 없음

### ReadMe 파일 만들기
<pre><code> 
$ vim README
$ git status
On branch master
Untracked files:
  (use "git add <file>..." to include in what will be committed)

    README

nothing added to commit but untracked files present (use "git add" to track)

</code></pre>

인덱스 추가(Staging)
<pre><code> 
$ git add README
$ git status
On branch master
Changes to be committed:
  (use "git reset HEAD <file>..." to unstage)
        new file:   README
</code></pre>

* 'Changes to be committed' 에 들어 있는 파일은 Staged 상태라는 것을 의미


### Modified 상태의 파일을 Staged 하기

git add는 파일을 새로 추적할 때도 사용하고 수정한 파일을 Staged 상태로 만들 때도 사용한다

변경 1 후 git add로 추가하면 Staged 상태 
변경 2 후 상태보면 Staged 와 Unstaged 상태 두개 다 나옴 => 여기서 커밋 하면 변경1의 시점으로 커밋됨(즉 최신버전으로 계속 git add해줌)

### 파일무시 
gitignore 파일을 만들고 그 안에 무시할 파일 패턴을 적는다. 아래는 .gitignore 파일의 예이다

</code></pre>

인덱스 추가(Staging)
<pre><code> 
$ cat .gitignore
*.[oa]
*~
</code></pre>

* a comment - 이 줄은 무시한다.
* 확장자가 .a인 파일 무시
*.a
* 윗 줄에서 확장자가 .a인 파일은 무시하게 했지만 lib.a는 무시하지 않는다.
!lib.a
* 루트 디렉토리에 있는 TODO파일은 무시하고 subdir/TODO처럼 하위디렉토리에 있는 파일은 무시하지 않는다.
/TODO
* build/ 디렉토리에 있는 모든 파일은 무시한다.
build/
* `doc/notes.txt`같은 파일은 무시하고 doc/server/arch.txt같은 파일은 무시하지 않는다.
doc/*.txt
* `doc` 디렉토리 아래의 모든 .txt 파일을 무시한다.
doc/**/*.txt


### 커밋 
git commit -m "message" 
* -m은 message

영역내의 모든파일을 자동으로 Staging 하는 옵션
git commit -a

### 파일 삭제 
git rm 명령으로 Tracked 상태의 파일을 삭제한 후에(Staging Area에서 삭제) 커밋

부가적인 것은 나중에 사용하면서 정리

### 파일이름변경

<pre><code> 
$ git mv file_from file_to
</code></pre>

## 커밋 히스토리 조회하기 

클론후 커밋
</code></pre>

$ git clone git://github.com/schacon/simplegit-progit.git

$ git log
commit ca82a6dff817ec66f44342007202690a93763949
Author: Scott Chacon <schacon@gee-mail.com>
Date:   Mon Mar 17 21:52:11 2008 -0700

    changed the version number

commit 085bb3bcb608e1e8451d4b2432f8ecbe6306e7e7
Author: Scott Chacon <schacon@gee-mail.com>
Date:   Sat Mar 15 16:40:33 2008 -0700

    removed unnecessary test code

commit a11bef06a3f659402fe7563abf99ad00de2209e6
Author: Scott Chacon <schacon@gee-mail.com>
Date:   Sat Mar 15 10:31:28 2008 -0700

    first commit

</code></pre>

git log 실행하면 SHA-1체크섬, 저자이름,저자이메일,커밋날짜,커밋메세지 나옴
-p 옵션을 사용하면 diff결과를 보여준다
-2 는 최근 두개만 보여줌

<pre><code> 
$ git log -p -2
commit ca82a6dff817ec66f44342007202690a93763949
Author: Scott Chacon <schacon@gee-mail.com>
Date:   Mon Mar 17 21:52:11 2008 -0700

    changed the version number

diff --git a/Rakefile b/Rakefile
index a874b73..8f94139 100644
--- a/Rakefile
+++ b/Rakefile
@@ -5,7 +5,5 @@ require 'rake/gempackagetask'
 spec = Gem::Specification.new do |s|
     s.name      =   "simplegit"
-    s.version   =   "0.1.0"
+    s.version   =   "0.1.1"
     s.author    =   "Scott Chacon"
     s.email     =   "schacon@gee-mail.com"

commit 085bb3bcb608e1e8451d4b2432f8ecbe6306e7e7
Author: Scott Chacon <schacon@gee-mail.com>
Date:   Sat Mar 15 16:40:33 2008 -0700

    removed unnecessary test code

diff --git a/lib/simplegit.rb b/lib/simplegit.rb
index a0a60ae..47c6340 100644
--- a/lib/simplegit.rb
+++ b/lib/simplegit.rb
@@ -18,8 +18,3 @@ class SimpleGit
     end

 end
-
-if $0 == __FILE__
-  git = SimpleGit.new
-  puts git.show
-end
\ No newline at end of file
</code></pre>

GitLab이나 GitHub 같이 웹에서 확인하는 것이 더 보기 좋아서 넘어감

## 2.4 Rollback 
* 한번 Rollback 되면 복구 불가 !


### 커밋 수정하기

커밋하고 메세지바꾸거나 빼먹은 파일있을때 --amend사용 
<code><pre>
$ git commit -m 'initial commit'
$ git add forgotten_file
$ git commit --amend
</code></pre>

### 파일상태 Unstage로 변경

git reset HEAD <file> 명령어 사용

### Modified 되돌리기 

git checkout -- <file>
사용법은 이런데 위험해서 잘사용안함 
branch같은걸로 빼서 사용

## 2.5 리모트 저장소

### 리모트 저장소 확인
git remote로 알수 있다.

<code><pre>
$ git clone git://github.com/schacon/ticgit.git
Cloning into 'ticgit'...
remote: Reusing existing pack: 1857, done.
remote: Total 1857 (delta 0), reused 0 (delta 0)
Receiving objects: 100% (1857/1857), 374.35 KiB | 193.00 KiB/s, done.
Resolving deltas: 100% (772/772), done.
Checking connectivity... done.
$ cd ticgit
$ git remote
origin
</code></pre>

-v 주면 URL도 볼수있다.
<code><pre>
$ git remote -v
origin  git://github.com/schacon/ticgit.git (fetch)
origin  git://github.com/schacon/ticgit.git (push)
</code></pre>

### 리모트 저장소 추가
 git remote add [단축이름] [url] 

 <code><pre>
$ git remote

origin

$ git remote add pb git://github.com/paulboone/ticgit.git
$ git remote -v

origin  git://github.com/schacon/ticgit.git
pb  git://github.com/paulboone/ticgit.git
 </code></pre>

이제 URL 대신에 스트링 pb를 사용할 수 있다. 예를 들어 로컬 저장소에는 없지만 Paul의 저장소에 있는 것을 가져오려면 아래과 같이 실행한다:

<code><pre>
$ git fetch pb

remote: Counting objects: 58, done.
remote: Compressing objects: 100% (41/41), done.
remote: Total 44 (delta 24), reused 1 (delta 0)
Unpacking objects: 100% (44/44), done.
From git://github.com/paulboone/ticgit
 * [new branch]      master     -> pb/master
 * [new branch]      ticgit     -> pb/ticgit

 </code></pre>


 ### 리모트 저장소 Pull or Fetch

 $ git fetch [remote-name]
 자동 Merge 안됨

 git pull 은 자동 mergy됨


 ### 리모트 저장소에 Push
  git push [리모트 저장소 이름] [브랜치 이름]

 ### 리모트 저장소 살펴보기 및 삭제 
[link](https://git-scm.com/book/ko/v1/Git%EC%9D%98-%EA%B8%B0%EC%B4%88-%EB%A6%AC%EB%AA%A8%ED%8A%B8-%EC%A0%80%EC%9E%A5%EC%86%8C)

## 2.6 태그

### 태그 조회하기
git tag 로 이미 만들어지 태그가 있는지 확인할수 있다.

git tag -l 'v1.4.2.*' 이런식으로 가능

## 2.7 팁 
Alias와 자동완성 기능

# 3.Git 브랜치

## 3.1 브랜치란 무엇인가

브랜치를 알려면 Git이 데이터를 어떻게 저장하는지에 대해서 알아야함
* Git은 데이터를 스냅샷으로 기록 
* 용어정리
    * 스냅샷 : 그상태 파일을 모두 저장
    * 델타 : 변경사항 (스냅샷끼리의 차)

[참고 자료](http://dogfeet.github.io/articles/2012/git-delta.html)
* 깃은 스냅샷+델타로 저장됨 
    *ex : 100mb 커밋하면 Woking Directory에 100Mb 올라감 => 5번하면 500Mb =>용량 문제 발생 
        => 가비지 컬렉터가 스냅샷끼리의 델타를 만들고 최종 스냅샷에 1,2,3,4,5 간의 델타로 변경 => 용량이 크게줄어듦
    
### 커밋 데이터 

* 저장소 : 커밋데이터(워킹디렉토리 포인터,저자,이메일,메세지 ...)  + 워킹디렉토리 트리구조 + 각 파일 

예제 

<pre><code>
$ git add README test.rb LICENSE
$ git commit -m 'initial commit of my project'
</code></pre>

![Alt ](https://git-scm.com/figures/18333fig0301-tn.png)


* 여러번 커밋 

![Alt ](https://git-scm.com/figures/18333fig0302-tn.png)

* 기본적으로 master브랜치를 만든다. 최초 커밋때 master라는 이름의 브랜치를 만듦 

![Alt ](https://git-scm.com/figures/18333fig0303-tn.png)

* 테스트 브랜치 생성
<pre><code>
$ git branch testing
</code></pre>

![Alt ](https://git-scm.com/figures/18333fig0304-tn.png)

* 현재 작업중인 브랜치 HEAD 

![Alt ](https://git-scm.com/figures/18333fig0305-tn.png)

* Head이동 
<pre><code>
$ git checkout testing
</code></pre>

![Alt ](https://git-scm.com/figures/18333fig0306-tn.png)

* 새로 커밋 
<pre><code>
$ vim test.rb
$ git commit -a -m 'made a change'
</code></pre>

![Alt ](https://git-scm.com/figures/18333fig0307-tn.png)

* HEAD master로 이동
<pre><code>
$ git checkout master
</code></pre>
![Alt ](https://git-scm.com/figures/18333fig0308-tn.png)

* Master 커밋
<pre><code>
$ vim test.rb
$ git commit -a -m 'made other changes'
</code></pre>

![Alt ](https://git-scm.com/figures/18333fig0309-tn.png)

## 3.2 브랜치와 Merge 기초

실제 개발과정에서 겪을만한 예제 
1. 작업중인 웹사이트
2. 새로운 이슈1를 처리한 새 Branch 하나 생성
3. 새로 만든 Branch에서 작업
4. 더 급한 이슈2 발생
5. 이슈1 처리전 브랜치로 이동
6. 이슈2 브랜치 새로하나 생성
7. 이슈2 해결하고 이슈1 처리중인 브랜치로 Merge
8. 하던일 진행

글로는 이렇고 그림으로 설명

- 마스터로 커밋 3번

![image](https://git-scm.com/figures/18333fig0310-tn.png)

- 이슈 53생겨서 브랜치 만듦 + Checkout으로 HEAD는 iss53브런치 가리킴

![image](https://git-scm.com/figures/18333fig0311-tn.png)

- 어느정도 일하고 커밋하면 iss53이 앞으로감 

![image](https://git-scm.com/figures/18333fig0312-tn.png)

- 다른이슈가생겨서 Hotfix 만들어야함 => hotfix브랜치 만들고 해결 

![image](https://git-scm.com/figures/18333fig0313-tn.png)

- 해결되었으면 마스터와 Merge
- Fast forward : 현재 커밋보다 앞선 곳을 커밋할 경우 
    master가 가리키는 커밋 포인터를 핫픽스가 가리키는 커밋 포인터로 변경, 다시말해서 단순히 포인터만 변경

![image](https://git-scm.com/figures/18333fig0314-tn.png)

- hotfix 브랜치 삭제하고 iss 53 해결 

![image](https://git-scm.com/figures/18333fig0315-tn.png)

- 앞에서 master 와 hotfix merge와는 다르게 현재(master)브랜치가 merge할 브랜치의 조상이 아니므로 Fast-forward로 merge하지 않는다.
- 이러면 각 브랜치가 가리키는 공통 조상하나를 사용하여 3-way Merge를 한다.

![image](https://git-scm.com/figures/18333fig0316-tn.png)

- 마무리 사진

![image](https://git-scm.com/figures/18333fig0317-tn.png)

### 충돌
각자다른 브랜치에서 코드 수정시 충돌남 충돌나는 곳을 해결하고 merge

## 3.4 Branch Workflow

## 3.5 Remote Branch

리모트 브랜치 이름은 (remote)/(branch) 로 구성됨
clone 할경우 origin/master 로 설정 => 서버의 마스터는 여기를 포인터로 두고있다 의미 // Local의 Master와는 다른의미 

![alt](https://git-scm.com/figures/18333fig0322-tn.png)

Local은  893cf 포인터까지 커밋(master)상태
누군가 서버로 커밋한다음 Push 하면 origin/master는 190a3 포인터를 가리킴  
내컴퓨터는 서버랑 동기화 하지않아 origin/master 포인터는 f42c5이고 master는 893cf이다.

![alt](https://git-scm.com/figures/18333fig0323-tn.png)

리모트 서버와 동기화 하려면 git fetch origin 명령을 사용
새로운 정보가 있으면 모두 내려받고 origin/master 포인터를 최신 커밋(190a3)으로 이동

![alt](https://git-scm.com/figures/18333fig0324-tn.png)

여러개의 리모트 서버를 가질때 
origin과 teamone 서버 두개 이지만 서로 마스터 포인터가 다름
![alt](https://git-scm.com/figures/18333fig0325-tn.png)

git getch teamone 으로 teamone 데이터를 받을 경우 모두 있는 데이터라 데이터는 받지않지만
teamone/matster는 동기화됨 


### Push , 리모트브런치 추적,삭제 

## 3.6 Rebase 우선 넘김


## 충돌 

서로다른 브런치가 같은 파일을 변경했을 경우 충돌발생
보통 하나의서버에 여러명이 코옵할 경우 생김 (local master와 origin/master가 같은 파일을 다르게 변경해서 커밋했을 경우)

충돌나면 Push 안됨
Pull 할 경우 충돌난거 해결 하라는 메세지 나옴
1. 충돌 파일 확인 및 변경
2. 변경된 파일 재커밋하여 병합