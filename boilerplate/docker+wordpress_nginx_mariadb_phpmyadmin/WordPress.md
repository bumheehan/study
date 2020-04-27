# WordPress



플러그인 설치오류

권한문제

폴더권한 777 







```
vi /etc/php-fpm.d/www.conf
사용자이름 ,phpfpm설정 뿐만아니라 자식개수도 정해야함 
max children
```

PHP FPM : 자식 개수정함

```
upload_max_filesize
파일에대한 최대 업로드 용량을 결정합니다.
post_max_size
POST 요청(HTTP 리퀘스트)를 통한 업로드 용량 제한치를 결정합니다.
memory_limit
PHP 스크립트가 얼마나 많은 메모리를 할당 받을 것인가를 결정하는데 upload_max_filesize의 값과 같거나 커야합니다. 
참고: 워드프레스의 경우 설정파일(wp-config.php)에 define('WP_MEMORY_LIMIT', '64M');  처럼 문구를 추가해 변경 할 수도  있습니다.
max_execution_time
실행 시간 제한입니다. 초단위 값을 넣습니다. 웹서버에서 fatal error: maximum execution time exceeded 같은 에러 메시지가 나온다면 이 값을 올려주면됩니다.
file_uploads  
On 으로 설정해야 파일을 업로드 할 수 있습니다. 이미 On으로 되어있을 겁니다.
 자신이 업로드하려는 파일이 20MB라고 한다면 위에서 각 3개의 세팅을 각각 25MB, 27MB, 30MB 정도로 설정해주면 된다고 하는데 대충 큰숫자로 통일하면 됩니다. max_execution_time 은 600으로 설정하겠습니다.
```

.htaccess 아파치 설정





```
HTTPS는 
cloudflare 설정

always https use 이거 안해도됨

그냥 dns설정만해주고 워드프레스에서 really simple ssl 만설치해주고 설정하면끝

https://www.wpdigest.kr/2018/06/free-ssl-service-by-cloudflare/#.XThWYugzaUk
----------------------------------------------------------------------------------------
cloudflare.com 가입하기
CloudFlare_SignUP
https://www.cloudflare.com 사이트를 열고 ‘이메일 주소’, ‘비밀번호’ 를 입력한 후 ‘Sign Up’ 클릭하면 등록이 완료된 후에

cloudflare_addsite
웹사이트 등록화면으로 넘어간다.

2. 도메인 등록하기

자신의 사이트 도메인(‘http://’는 제외한 순수한 도메인 이름만 넣는다. 혹자는 서브도메인도 등록 가능하다는 설이있으나 불가는한 것으로 확인되었다. )을 입력하고  ‘scan’버튼 클릭하면   DNS recods상태를 한참 스캔 한 후에 결과를 보여준다.

3. DNS records 확인하기

Cloudflare_DNS_records_scan_results
그림 같이 A record 인  ‘example.com’, 와  CNAME ‘www.example.com’ 이 검색되고 ‘cloud 그림’이 연결되어 있으면 ok  ‘continue’ 버튼 누르면 서비스 등급 선택화면으로 넘어간다.

4. cloudflare plan — free website로 선택하기

cloudflare_select_free_website
무료서비스를 선택하고 ‘continue’ 하면 네임서버 변경안내 화면으로 넘어간다.

5. 네임서버변경하기

cloudflare_change_nameserver
여기서 주의할 점은 두 가지

      자신의 서버 가 위치한 지역에 따라서 cloudflare가 제공하는 네임서버의 서브네임이 달라진다. 따라서 반드시 자신에게  제공된 서버네임을 카피해서 넣는다. 1), 2)
      예전에 사용하던 네임서버 두 개 이상 이더라도 절대  나머지 네임서버는 사용하지 말고 지워야한다.  3) ,4)
6.    ‘Flexible SSL’ 설정 — cloudflare  dashboard  ‘Crypto’ tab 선택

cloudflare  dashboard에서   ‘Crypto’ tab 선택하고

‘Flexible’을 선택한다. ( ‘Flexible’에 관해서는 에서 설명하기로 하겠다. )

cloudflare_recheck_nameservers
‘overview’ tab으로 가서 ‘recheck_nameservers’ 버튼을 누른다.

네임서버의 성능에 따라서 다르지만  ‘Pending’–>’Active’ 으로 바뀌는 데   최대 24시간이 소요된다. 그때까지 기다려야 하지만 플러그인은  미리 설치해 놓을수는 있다.  필자의 경우에는  실수로 ‘일차네임서버’ 이름을 2차네임서버와 동일하게 기재하는바람에 시간이 약간 더 지체 되었다.   네임서버이름이 올바르게 수정된 후 곧 바로 cloudflare 로부터 아래와 같이 세 개의 메일이 발송되었다.

 

7.  WordPress 플러그인 설치- Really simple SSL

플러그인 사이트에서 ‘Really simple SSL’를 검색하여 설치한다.  이후의 과정은 네임서버변경이 완성된 이후의 상황이다.

Really_simple_ssl_go_ahead_active_SSL
Go ahead 버튼을 누른다음 ‘설정’탭에서 ‘ssl’을 선택한다.

admin_dashboard_설정_SSL
Really-simple_ssl-Mixed-content-fixer-error
‘Mixed-content-fixer’기능에 이상이 있는 경우에는’ Page Rule’ 설정이 중요해진다. ‘Page Rule’ 탭으로 이동한다.

8. ‘Page Rule’  설정

cloudflare_page_rules
‘Create Page Rules’ 버튼을 누르고

cloudflare_page_rules
그림같이 입력한 후에 저장버튼누른다.
Really simple SSL  에 돌아가 보면 아래같이 정상으로  된것을 확인할수있다.

Mixed_content_fixer_successfully_detected
일단 마친다.
```



워드프레스 db설정

```
HTTPS db변경

```





고유주소

```
아파치
.htaccess 여기서 설정일듯


nginx

conf에서 location 밑에 try_files 추가

location / {
                try_files $uri $uri/ /index.php?$args;
        }
```





```
nginx 캐시

```

