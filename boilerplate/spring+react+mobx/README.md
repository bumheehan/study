# React & Spring boot 

1. Spring Boot + Security
2. CRA
3. React Router, Mobx, Babel, Webpack, React, EsLint, HRM?

# 문제점
- 스프링 서버 재부팅 안하고 뷰만 변경
- 해시가 달라질 경우 스프링 서버 재부팅 해야함  => 스프링 devtools 사용
- 리액트 라우터 사용

# Spring Boot + Security

Pom.xml

```xml

```

Security-config.java

```java

```

# Create-React-App
CRA 패키지 

## CRA 설치
```
npm init react-app spring-react
```

## CRA 분석
package.json
```json
{
  "name": "spring-react",
  "version": "0.1.0",
  "private": true,
  "dependencies": {
    "@testing-library/jest-dom": "^4.2.4",
    "@testing-library/react": "^9.5.0",
    "@testing-library/user-event": "^7.2.1",
    "customize-cra": "^0.9.1",
    "mobx": "^5.15.4",
    "mobx-react": "^6.1.8",
    "react": "^16.13.0",
    "react-app-rewired": "^2.1.5",
    "react-dom": "^16.13.0",
    "react-router-dom": "^5.1.2",
    "react-scripts": "3.4.0"
  },
  "scripts": {
    "start": "react-app-rewired start",
    "build": "react-app-rewired build ",
    "postbuild": "@powershell rm -r -Force ../resources/static/* && @powershell mv -Force build/* ../resources/static ",
    "test": "react-app-rewired test",
    "eject": "react-scripts eject"
  },
  "eslintConfig": {
    "extends": "react-app"
  },
  "browserslist": {
    "production": [
      ">0.2%",
      "not dead",
      "not op_mini all"
    ],
    "development": [
      "last 1 chrome version",
      "last 1 firefox version",
      "last 1 safari version"
    ]
  },
  "devDependencies": {
    "@babel/plugin-proposal-decorators": "^7.8.3",
    "eslint": "^6.8.0",
    "eslint-config-airbnb": "^18.1.0",
    "eslint-config-prettier": "^6.10.0",
    "eslint-plugin-import": "^2.20.1",
    "eslint-plugin-jsx-a11y": "^6.2.3",
    "eslint-plugin-prettier": "^3.1.2",
    "eslint-plugin-react": "^7.19.0",
    "eslint-plugin-react-hooks": "^2.5.0",
    "prettier": "1.19.1"
  }
}


```


## React Router 설치

```
npm install react-router-dom --save
```
## Sass 설치 (필요하면)

```
npm install node-sass --save
```

## ESLint/ Prettier
- ESLint : 문법 오류 체크
- Prettier : 포맷에 맞게 코드변경 (Eslint plugin 은 추가적인 Rule을 포함)
- eslint-plugin-prettier : Prettier에서 인식하는 코드상의 포맷 오류를 ESLint 오류로 출력해준다
- eslint-config-prettier : ESLint의 formatting관련 설정 중 Prettier와 충돌하는 부분을 비활성화 한다.
(Eslint에서도 formatting 처리함 충돌나는 부분 비활성화)

```
npm install --save-dev prettier eslint-plugin-prettier eslint-config-prettier 
npx install-peerdeps --dev eslint-config-airbnb
```

- CRA는 ESLint가 설치되어있음
- ESLint만 설치하면 CLI에서만 확인 가능 => ESLint Vscode확장팩설치
- VSCode 에서 포맷팅이 가능하도록 하거나 파일 저장 시에 포맷팅이 가능하도록 하기 위해서 확장 기능을 설치한다.
- settings.json에 다음과 같이 설정
```
"editor.formatOnSave": false, 
"[javascript]": {
    //default formatter
    "editor.defaultFormatter": "esbenp.prettier-vscode",
    "editor.formatOnSave": true
},
```

## Mobx 설치

```
npm i --save mobx --save mobx-react
```

### 데코레이터
customize-cra와 react-app-rewired는 eject 없이 CRA의 설정을 커스터마이징을 할 수 있도록 해주는 라이브러리 입니다. (eject보다 자유롭지는 않다)

만약 CRA(v1)을 쓰고 있다면 react-app-rewired 만 설치하면 됩니다.

저는 현재 최신 버전인 CRA v2 를 기준으로 하겠습니다.
```
npm install --save -d customize-cra
npm install --save -d react-app-rewired
```
두 가지를 설치하면 다음과 같이 package.json에 있는 scripts를 다음과 같이 수정합니다.

```
"scripts": {
	"start": "react-app-rewired start",
    "build": "react-app-rewired build",
    "test": "react-app-rewired test --env=jsdom",
    "eject": "react-scripts eject",
}
```


```
npm install --save-dev @babel/plugin-proposal-decorators
```

?
root에 jsconfig.json
```
{
    "compilerOptions": {
        "experimentalDecorators": true
    }
}
```


manifest

웹앱 매니페스트란 앱에 대한 정보를 담고 JSON 파일입니다. 배경색은 어떠한 색인지, 앱의 이름은 무엇인지, 홈스크린 화면에 추가할 때 아이콘은 어떤 것인지 등의 정보를 담고 있죠. 웹앱 매니페스트는 manifest.json 파일명을 대부분 사용합니다.

```
short_name: 사용자 홈 화면에서 아이콘 이름으로 사용
name: 웹앱 설치 배너에 사용
icons: 홈 화면에 추가할때 사용할 이미지
start_url: 웹앱 실행시 시작되는 URL 주소
display: 디스플레이 유형(fullscreen, standalone, browser 중 설정)
theme_color: 상단 툴바의 색상
background_color: 스플래시 화면 배경 색상
orientation: 특정 방향을 강제로 지정(landscape, portrait 중 설정)

```

서비스워커(Service Worker)

서비스워커는 브라우저의 백그라운드에서 실행되는 자바스크립트 워커입니다. PWA는 네이티브 앱처럼 오프라인 상태에서도 사용가능하고, 푸시 알림(Notification) 기능도 사용할 수 있는데요. 이런 기능을 할 수 있도록 도와주는 것이 바로 서비스워커입니다. 향후에는 서비스워커에 지오펜싱(Geofencing) 기능도 추가될 예정이라고 하네요.


이 부분 잘모름 