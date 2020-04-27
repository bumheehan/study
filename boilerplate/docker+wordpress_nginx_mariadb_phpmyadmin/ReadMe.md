# Wordpress,nginx,php-fpm,phpmyadmin

CentOS 7 에서 설정

## 1. Install Docker

#### Docker

```
# yum update
# yum install -y yum-utils device-mapper-persistent-data lvm2
# yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
# yum install docker-ce docker-ce-cli containerd.io
```

- 재대로 설치안해서 client 만 설치되고 문제 많았음, 공홈에 나와 있는대로 설치
- https://docs.docker.com/install/linux/docker-ce/centos/

#### Docker-compose

```
# curl -L "https://github.com/docker/compose/releases/download/1.24.1/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose

# chmod +x /usr/local/bin/docker-compose
# ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose
# docker-compose --version

```



## 2.  Copy File 

설치 경로(.)에 파일 넣기

1. `./docker-compose.yml`
2. `./.env `
3. `./nginx/default.conf` 

##### docker-compose.yml

```
version: '3.6'
services:

  wordpress:
    image: wordpress:${WORDPRESS_VERSION:-php7.3-fpm}
    container_name: wordpress
    volumes:
      - ${WORDPRESS_DATA_DIR:-./wordpress}:/var/www/html
      - ${WORDPRESS_UPLOAD_DIR:-./conf/uploads.ini}:/usr/local/etc/php/conf.d/uploads.ini
      - ${WORDPRESS_PHPCONF_DIR:-./conf/www.conf}:/usr/local/etc/php-fpm.d/www.conf
    environment:
      - WORDPRESS_DB_NAME=${WORDPRESS_DB_NAME:-wordpress}
      - WORDPRESS_TABLE_PREFIX=${WORDPRESS_TABLE_PREFIX:-wp_}
      - WORDPRESS_DB_HOST=${WORDPRESS_DB_HOST:-mysql}
      - WORDPRESS_DB_USER=${WORDPRESS_DB_USER:-root}
      - WORDPRESS_DB_PASSWORD=${WORDPRESS_DB_PASSWORD:-password}
    depends_on:
      - mysql
    restart: always

  mysql:
    image: mariadb:${MARIADB_VERSION:-latest}
    container_name: mysql
    volumes:
      - ${MYSQL_DATA_DIR:-./mysql}:/var/lib/mysql
    environment:
      - MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD:-password}
      - MYSQL_USER=${MYSQL_USER:-root}
      - MYSQL_PASSWORD=${MYSQL_PASSWORD:-password}
      - MYSQL_DATABASE=${MYSQL_DATABASE:-wordpress}
    restart: always

  nginx:
    image: nginx:${NGINX_VERSION:-latest}
    container_name: nginx
    ports:
      - '80:80'
      - '443:443'
    volumes:
      - ${NGINX_CONF_DIR:-./nginx}:/etc/nginx/conf.d
      - ${NGINX_LOG_DIR:-./logs/nginx}:/var/log/nginx
      - ${WORDPRESS_DATA_DIR:-./wordpress}:/var/www/html
      - ${SSL_CERTS_DIR:-./certs}:/etc/letsencrypt
      - ${SSL_CERTS_DATA_DIR:-./certs-data}:/data/letsencrypt
    depends_on:
      - wordpress
    restart: always
    
  phpmyadmin:
    image: phpmyadmin/phpmyadmin:${PHPMYADMIN_VERSION:-latest}
    container_name: phpmyadmin
    ports:
      - '8080:80'
    environment:
      - MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD:-password} 
      - MYSQL_USER=${MYSQL_USER:-root}
      - MYSQL_PASSWORD=${MYSQL_PASSWORD:-password}
      - PMA_HOST=mysql
      - PMA_ARBITRARY=1 
    depends_on:
      - mysql
    restart: always
    
networks:
  default:
    driver: bridge
    driver_opts:
      com.docker.network.driver.mtu: 1450
```

- docker-compose 에서 컨테이너 이름 그대로 쓰면 그 컨테이너의 host 값이 들어감, 포트를 열지않으면 컨테이너들은 docker 0를 통해서만 통신이 가능한데 그때 내부 ip를 사용할때 위의 방법으로 사용함 
- wordpress나 phpmyadmin host를 입력할때 mysql 은 외부 포트를 열지 않았으므로 docker0에서  할당한 mysql 컨테이너 ip를 넣어야함, docker-compose.yml에서  컨테이너 이름으로 사용 
- 나같은경우 신복위 kafka:29092 할때 사용

##### .env (상수값)

```

#wordpress - wordpress:php7.3-fpm
WORDPRESS_VERSION=php7.3-fpm
WORDPRESS_DB_NAME=wordpress
WORDPRESS_TABLE_PREFIX=wp_
WORDPRESS_DB_HOST=mysql					
WORDPRESS_DB_USER=esthercoy				
WORDPRESS_DB_PASSWORD=1q2w3e~!			

# mariadb - mariadb:latest
MARIADB_VERSION=latest
MYSQL_ROOT_PASSWORD=1q2w3e~!			
MYSQL_USER=esthercoy				
MYSQL_PASSWORD=1q2w3e~!					
MYSQL_DATABASE=wordpress				

# nginx - nginx:latest
NGINX_VERSION=latest

# PHPMyAdmin
PHPMYADMIN_VERSION=latest

# volumes on host
NGINX_CONF_DIR=./nginx
NGINX_LOG_DIR=./logs/nginx
WORDPRESS_DATA_DIR=./wordpress
WORDPRESS_UPLOAD_DIR=./conf/upload.ini
WORDPRESS_PHPCONF_DIR=./conf/www.conf
MYSQL_DATA_DIR=./mysql
PHPMYADMIN_CONF_DIR=./phpmyadmin
SSL_CERTS_DIR=./certs
SSL_CERTS_DATA_DIR=./certs-data

```

##### upload.ini
```
file_uploads = On
memory_limit = 500M
upload_max_filesize = 500M
post_max_size = 500M
max_execution_time = 600

```
##### nginx 업로드부분 변경
```

server {
    listen 80;
    server_name 127.0.0.1;
    client_max_body_size 500M;
    
```
##### HTTP default.conf

```
server {
    listen 80;
    server_name 127.0.0.1;

    root /var/www/html;
    index index.php;

    access_log /var/log/nginx/access.log;
    error_log /var/log/nginx/error.log;

    location / {
        try_files $uri $uri/ /index.php?$args;
    }

    location ~ \.php$ {
        try_files $uri =404;
        fastcgi_split_path_info ^(.+\.php)(/.+)$;
        fastcgi_pass wordpress:9000;
        fastcgi_index index.php;
        include fastcgi_params;
        fastcgi_param SCRIPT_FILENAME $document_root$fastcgi_script_name;
        fastcgi_param PATH_INFO $fastcgi_path_info;
    }
}

```

##### HTTPS default.conf

```
server {
    listen      80;
    listen [::]:80;
    server_name FQDN_OR_IP;

    location / {
        rewrite ^ https://$host$request_uri? permanent;
    }

    location ^~ /.well-known {
        allow all;
        root  /data/letsencrypt/;
    }
}

server {
    listen      443           ssl http2;
    listen [::]:443           ssl http2;
    server_name               FQDN_OR_IP www.FQDN_OR_IP;

    add_header                Strict-Transport-Security "max-age=31536000" always;

    ssl_session_cache         shared:SSL:20m;
    ssl_session_timeout       10m;

    ssl_protocols             TLSv1 TLSv1.1 TLSv1.2;
    ssl_prefer_server_ciphers on;
    ssl_ciphers               "ECDH+AESGCM:ECDH+AES256:ECDH+AES128:!ADH:!AECDH:!MD5;";

    ssl_stapling              on;
    ssl_stapling_verify       on;
    resolver                  8.8.8.8 8.8.4.4;

    root /var/www/html;
    index index.php;

    access_log /var/log/nginx/access.log;
    error_log /var/log/nginx/error.log;

    ssl_certificate           /etc/letsencrypt/live/FQDN_OR_IP/fullchain.pem;
    ssl_certificate_key       /etc/letsencrypt/live/FQDN_OR_IP/privkey.pem;
    ssl_trusted_certificate   /etc/letsencrypt/live/FQDN_OR_IP/chain.pem;

    location / {
        try_files $uri $uri/ /index.php?$args;
    }

    location ~ \.php$ {
        try_files $uri =404;
        fastcgi_split_path_info ^(.+\.php)(/.+)$;
        fastcgi_pass wordpress:9000;
        fastcgi_index index.php;
        include fastcgi_params;
        fastcgi_param SCRIPT_FILENAME $document_root$fastcgi_script_name;
        fastcgi_param PATH_INFO $fastcgi_path_info;
    }
}
```

## 3. Docker Compose

#### Volume

```
./nginx 		=	nginx 설정경로
./logs/nginx	= 	nginx 로그경로
./wordpress		= 	wordpress 경로
./mysql			= 	mysql 경로
./phpmyadmin	= 	phpmyadmin 경로
./certs			= 	ssl 인증서 경로
./certs-data	= 	ssl 인증 데이터경로
```

- 경로에 파일이 있으면 그대로 읽어서 사용하고 없으면 복사하기 때문에 꼭 처음시작하거나 문제가 생겨 초기화 할때 폴더 삭제 하고 시작

```
# rm -rf certs/ certs-data/ logs/nginx/ mysql/ wordpress/
```

#### Docker-compose

```
# docker-compose -f docker-compose.yml up -d
```

- 포어 그라운드로 할 경우 -d 제거

## 4. SSL

## Test

- 설정 변경이 있을시 꼭 기존 파일 삭제 후 새로설치, 기존데이터 남아있어서 변경안되는 줄 알고 고생했슴

```
# docker rm -f phpmyadmin wordpress nginx mysql  
# rm -rf certs/ certs-data/ logs/nginx/ mysql/ wordpress/
```

- Docker-compose로도 멈추고 삭제할 수 있음

```
$ cd wordpress-nginx-docker
$ docker-compose stop
Stopping nginx     ... done
Stopping wordpress ... done
Stopping mysql     ... done
$ docker-compose rm -f
Going to remove nginx, wordpress, mysql
Removing nginx     ... done
Removing wordpress ... done
Removing mysql     ... done
```

