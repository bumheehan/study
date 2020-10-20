# Webpack 
여러가지 자바스크립트 모듈 디펜던시를 관리하는 도구

https://velog.io/@decody/Webpack-%EC%84%A4%EC%A0%95%ED%95%98%EA%B8%B0-

https://cresumerjang.github.io/2019/02/09/webpack-config/

## 설치
```
$npm install webpack
```

## Webpack.config.js

### 용어

- mode: 해당값에 따라 내부 최적화를 따릅니다.
    - String
    - production(기본값), development, none
- entry: 라이브러리 및 모듈을 로딩을 시작할 포인트 설정
    - object, string, array모두가능
    - object 예제
        ```javascript
        {
            entry : {
                app: './src/app.js', // 잦은배포
                vendors: './src/vendors.js' // 버전업 잘 없는 경우
            }
        }
        ```
- output: 산출물 파일명 및 파일 path 설정
    - object
    - 속성
        - name: 엔트리 명에 따른 output
        - hash: webpack build에 따른 output
        - chunkhash: chunk에 따른 output
    - 예제
    ``` javascript
    {
        output: {
            path: '/home/cdn/assets/[hash]', // 빌드된 번들 파일이 위치할 파일의 절대 경로
            publicPath: 'http://script.auction.co.kr/assets/[hash]/', // 브라우저가 참고할 번들링 결과 파일의 URL주소(CDN 호스트) 반드시 앞뒤 / 추가해 줘야합니다. ?
            filename: '[name].js' // entry의 key name => name 으로 참조 됩니다.       
        }
    }
    ```
- loader: 로더는 웹팩 번들링 시점에 중간에 개입
    - object
    - 정규식, css(scss), babel 등 비 자바스크립트 로더
    - 웹팩은 자바스크립트밖에 모름 -> 이미지,폰트,스타일시트 등 비 자바스크립트 파일은 웹팩이 이해하게끔 변경하는부분이 로더
        - test : 로딩할 파일
        - use : 적용할 로더

- plugins: 번들링 완료 후 마지막 output 시점에 개입
  
    - array


    - 플러그인 종류 체크 
    - 
    ```javascript 
    module.exports = {
    plugins: [
        new webpack.ProvidePlugin({
            $: 'jquery' 
        }), 
        // 모든 모듈에서 사용할 수 있도록 해당 모듈을 변수로 변환한다.
        // 즉 각 모듈(파일)에서 매번 import할 필요 없이 해당 변수를 통하여 참조가능
        new webpack.DefinePlugin({
            PRODUCTION: JSON.stringify(true),
            VERSION: JSON.stringify('5fa3b9'),
            BROWSER_SUPPORTS_HTML5: true,
            TWO: '1+1',
            'typeof window': JSON.stringify('object')
        }),
        // 웹팩 번들링 시작하는 시점에 사용 가능한 상수값 정의 가능
        // 개발계, 테스트계에 따라 다른 설정 적용시 유용하다.
        new ManifestPlugin({
            fileName: 'manifest.json',
            basePath: './dist/'
        }),
        // 번들링시 생성되는 코드에 대한 정보들을 json 파일 안에 담아줘 라이브러리들간 의존성 파악 용의
    ]
}
    ```
    
    ```

- resolve: 모듈로딩 관련 옵션 설정, 모듈 해석방식 정의(alias등)
    - object
    - alias,modules 많이 사용
    - alias : 실제 모듈 Path가 아닌 alias에서 선언한 모듈 Path로 로딩이 가능합니다.
    ```javascript
    module.exports = {
        resolve:{
            alias: {
                Utilities: path.resolve(__dirname, 'src/path/utilities/')
            }
        }
    }
    // 기존 모듈 로딩
    import Utility from '../../src/path/utilities/utility';

    // resolve alias option 설정 시 모듈 로딩
    import Utility from 'Utilities/utility';
    ```
    - modules : require, import 등 모듈 로딩시에 기준이되는 폴더를 설정할 수 있습니다. 
    ```javascript
    module.exports = {
        resolve:{
            modules: [path.resolve(__dirname, 'src'), 'node_modules'] // src/node_modules
        }
    }
    ```

- devtool: 디버깅을 위한 소스맵 제공
- devServer: 빌드를 위한 개발 서버
    - object
    - 설치
    ```javascript
    //설치
    $ npm i webpack-dev-server -D
    //설정
    module.exports = {
    //...
        devServer: {
            publicPath: '/assets', // 절대경로로 지정하고 항상 /를 앞뒤에 붙여야 한다. 
            contentBase: path.join(__dirname, 'dist'), // 서버가 로딩할 static 파일 경로
            // contentBase: false, // 비활성화
            compress: true, // gzip 압축 방식을 이용하여 웹 자원의 사이즈를 줄인다.
            // 파일을 줄이는게 아니라 서버와 클라이언트간 압축방식 정의
            port: 9000
        }
    };

    //package.json
    "scripts": { "start": "webpack-dev-server"} 
    //실행
    $npm start
    ```


    ```
  - optimizer 
    - minimize는 TerserPlugin이나 minimizer에 따로 정의된 플러그인 사용하여 minimize
    - splitChunks : 청크파일에서 중복되는 모듈을 모으는 역할
  - webpack-bundle-analyzer

## watch 모드
js 소스코드가 변경될 때마다 자동으로 bundle.js파일을 만들어줌

```
$ webpack -w
```

## 통합 config


```javascript
const path = require("path");
const TerserPlugin = require("terser-webpack-plugin");
const { CleanWebpackPlugin } = require("clean-webpack-plugin");
const ManifestPlugin = require("webpack-manifest-plugin");
module.exports = {
  entry: {
    'index': './src/index.js'
  },
  output: {
    filename: '[name].bundle.js',
    path: path.resolve(__dirname, './dist'),
    publicPath: ''
  },
  mode: 'development',//나중에 production으로 변경
  devServer: {
    contentBase: path.resolve(__dirname, './dist'),
    index: 'index.html',
    port: 9000
  },
  module: {
    rules: [
        //FileLoader
        {
            test: /\.(png|jpg)$/,
            use: [
                'file-loader'
            ]
        },
        //CSS 설정
        {
            test: /\.css$/i,
            use: ['style-loader', 'css-loader'],
        },

        /*
        //SCSS 설정
        {
            test: /\.scss$/,
            use: [
                'style-loader', 'css-loader', 'sass-loader'
            ]
        },
        */

        //바벨
        //npm install -D babel-loader @babel/core @babel/preset-env webpack

        {
            test: /\.(js|ts)$/,
            exclude: /node_modules/,
            use: {
                loader: 'babel-loader',
                options: {
                    presets: [ 
                        '@babel/preset-env' ,//기본
                        '@babel/preset-typescript',//타입
                        '@babel/preset-react'//jsx
                    ],
                    plugins: [
                        '@babel/proposal-class-properties',
                        '@babel/proposal-object-rest-spread'
                    ]
                }
            }
        }
    ]
  },

  const TerserPlugin = require('terser-webpack-plugin');

  plugins: [
    new CleanWebpackPlugin('dist'), // 초기화
    new ManifestPlugin({
        fileName: 'manifest.json',
        basePath: './dist/'
    }),
    // 번들링시 생성되는 코드에 대한 정보들을 json 파일 안에 담아줘 라이브러리들간 의존성 파악 용의
  ],
  optimization: {
    minimize: true,
    minimizer: [new TerserPlugin()],
  },
};
```




