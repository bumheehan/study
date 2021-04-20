# NPM 학습

## 모듈화
기존 자바스크립트는 `<script>` 태그를 통해 외부 스크립트 파일을 전역 객체에 바인딩 했는데 이는 전역 변수가 겹치는 등 문제가 발생함 

-> 자바스크립트 모듈화 방법으로 CommonJS, AMD(Asynchronous Module Definition) 진영이 나옴

-> CommonJS는 AMD에 비해 문법이 간단하고, 동기 방법 

-> NPM이 CommonJS 모듈화 방법을 사용

-> 대부분 브라우저가 ES6 Module을 지원하지 않기 때문에 Browserify, Webpack같은 모듈 번들러 사용

## NPM 설치 및 설정

### 프로젝트 설치
특정폴더에서 프로젝트를 시작하려면 다음 명령어 사용
```
#기본 설치 , 프로젝트명 버전 등 기본 정보 물어봄
$npm init 

# 프로젝트 정보 기본값으로 생성
$npm init -y
```

실행되면 Package.json이 생성됨


#### Package.json
프로젝트 정보와 의존성 관리 -> Maven의 Pom.xml과 비슷
- name & version 은 프로젝트 고유값으로 필수
- dependencies 는 의존되는 패키지 정보
    - `"node-emoji": "^1.5.0"` 다음과 같이 ^이 사용될 경우 마이너 버전내 업데이트 허용
    - 
        ```
        version	    명시된 version과 일치
        >version	명시된 version보다 높은 버전
        >=version	명시된 version과 같거나 높은 버전
        <version	명시된 version보다 낮은 버전
        <=version	명시된 version과 같거나 낮은 버전
        ~version	명시된 version과 근사한 버전
        ^version	명시된 version과 호환되는 버전
        ```

- devDependencies 는 개발 환경에서만 사용되는 의존 패키지 정보


### 패키지 설치


#### Local 설치 & Global 설치
- 패키지는 Local , Global 위치에 맞게 설치 
- Local은 해당 폴더 내에 설치하여 해당 프로젝트에만 사용
- Global은 전체 프로젝트에서 사용
    - macOS의 경우
        - /usr/local/lib/node_modules
    - 윈도우의 경우
        - c:\Users\%USERNAME%\AppData\Roaming\npm\node_modules

```
$npm install <package>
$npm install -g <package>
# 제거 
$npm uninstall <package>
$npm uninstall -g <package>
# 업데이트
$npm update
#전역 설치 패키지 확인
$ npm ls -g --depth=0
```

`<package>`에 버전을 명시하지 않을 시 최신버전으로 설치
- 버전 명시 `<packageName>@<version>`


#### Dependency 설정
Package.json에 의존성 추가, npm5 부터 --save가 기본값이라고함

--save는 dependencies 에
--save-dev(-D)는 devDependencies에 추가

```
$npm install --save <package>
$npm install --save-dev <package>
```

package.json에 있는 모든 의존 패키지 설치

```
$npm install
```


## NPM 명령어 모음
```
$npm install <package>          로컬 패키지 설치
$npm install -g <package>       글로벌 패키지 설치
$npm install --save <package>   dependency 저장
$npm install --save-dev <package>  devDependency 저장
$npm init                       프로젝트 시작
$npm uninstall <package>        패키지 제거
$npm uninstall -g <package>     글로벌 패키지 제거
$npm update                     업데이트
$npm ls -g --depth=0           전역 설치 패키지 확인
$npm root -g                   전역 설치 경로
$npm start                      package.json start script 실행
$npm run                         package.json start script 실행
$npm view <package>             패키지 정보
$npm view <package> peerDependencies     함께 설치해야하는 패키지 정보
$npm root -g
```



### 질문

1. npm run build VS npm install
   - run build는 package.json에 정의된 스크립트 실행
   - npm install은 의존 패키지 설치
   - 즉 package.json의 dependency가 바뀌면 빌드전에 npm install 해줘야함, 안 바뀌면 어플리케이션 코드수정 후 npm run build만 해줘도됨