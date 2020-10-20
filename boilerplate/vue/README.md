```
npm init -y
npm install --save-dev webpack webpack-cli webpack-manifest-plugin html-webpack-plugin
npm install --save-dev @babel/core babel-loader @babel/preset-env
npm install --save-dev webpack-dev-server
npm install vue --save
npm install vue-loader vue-template-compiler vue-style-loader css-loader -D

// eslint/prettier
npm install eslint prettier eslint-config-prettier eslint-plugin-prettier eslint-plugin-vue babel-eslint --save-dev
```

https://velog.io/@kyusung/Vue-app-sfc-without-vue-cli

vue: JavaScript 프레임 워크
vue-loader 및 vue-template-compiler: Vue 파일을 JavaScript로 변환하는 데 사용됩니다.
webpack: 일부 변형을 통해 코드를 전달하고 하나의 파일로 묶을 수있는 도구입니다.
webpack-cli: Webpack 명령을 실행하는 데 필요합니다.
webpack-dev-server: 로컬에서 개발 서버를 실행하는데 필요합니다.
babel-loader: ES6 코드를 ES5로 변환합니다. (다음 두 가지 종속성에 도움이 필요합니다.)
@babel/core 및 @babel/preset-env: babel 자체는 코드에 아무런 영향을 미치지 않습니다. 이 두 가지기능을 사용하면 ES6 코드를 ES5 코드로 변환 할 수 있습니다.
css-loader: .vue 파일에 작성한 CSS 또는 JavaScript 파일로 가져올 CSS를 가져 와서 해당 파일의 경로를 확인합니다.
vue-style-loader: css-loader로 가져온 CSS을 HTML 파일에 삽입합니다. 이렇게 하면 HTML 문서의 header 부분에 스타일 태그가 만들어져 삽입됩니다.
html-webpack-plugin: index.html을 가져 와서 번들 된 JavaScript 파일을 header에 삽입합니다. 그 다음 dist 폴더에 복사합니다.
rimraf: 명령 줄에서 파일 삭제를 허용합니다. 여러번 프로젝트를 빌드할 때 유용하며, 이것을 사용하여 오래된 빌드를 삭제합니다.
