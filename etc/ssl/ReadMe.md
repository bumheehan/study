
# SSL 공부

## Ref

[openssl 설명](https://www.lesstif.com/system-admin/openssl-root-ca-ssl-6979614.html)

[쿠버네티스 SSL 설명](https://coffeewhale.com/kubernetes/authentication/x509/2020/05/02/auth01/)

[SSL 설명](https://wiki.kldp.org/HOWTO/html/SSL-Certificates-HOWTO/x70.html)

[CA 흐름 설명](https://blog.naver.com/alice_k106/221468341565)

## 암호화 방식 
- 추가 

### 공개키/개인키, PKI

- 개인키로 공개키 생성
- 개인키 암호화 -> 공개키 복호화 : 인증, 전자서명
- 공개키 암호화 -> 개인키 복호화 : 전송
- 대칭키보다 느리기 때문에 웹에서는 PKI로 인증하고 대칭키를 암호화 시켜서 전달하고 대칭키로 통신(세션마다 다름)


### 브라우저 간단한 통신방법, 이미지 추가

![](images/browser_handshake.png)

1. [웹브라우저] SSL로 암호화된 페이지를 요청하게 된다. (일반적으로 https://가 사용된다)
2. [웹서버] Public Key를 인증서와 함께 전송한다.
3. [웹브라우저] 인증서가 자신이 신용있다고 판단한 CA(일반적으로 trusted root CA라고 불림)로부터 서명된 것인지 확인한다. (역주:Internet Explorer나 Netscape와 같은 웹브라우저에는 이미 Verisign, Thawte와 같은 널리 알려진 root CA의 인증서가 설치되어 있다) 또한 날짜가 유효한지, 그리고 인증서가 접속하려는 사이트와 관련되어 있는지 확인한다.
4. [웹브라우저] Public Key를 사용해서 랜덤 대칭 암호화키(Random symmetric encryption key)를 비릇한 URL, http 데이터들을 암호화해서 전송한다.
5. [웹서버] Private Key를 이용해서 랜덤 대칭 암호화키와 URL, http 데이터를 복호화한다.
6. [웹서버] 요청받은 URL에 대한 응답을 웹브라우저로부터 받은 랜덤 대칭 암호화키를 이용하여 암호화해서 브라우저로 전송한다.
7. [웹브라우저] 대칭 키를 이용해서 http 데이터와 html문서를 복호화하고, 화면에 정보를 뿌려준다.

### 인증서

인증서 구성
1. 인증서 소유자의 e-mail 주소
2. 소유자의 이름
3. 인증서의 용도
4. 인증서 유효기간
5. 발행 장소
6. Distinguished Name (DN)
  - Common Name (CN)
  - 인증서 정보에 대해 서명한 사람의 디지털 ID
7. Public Key
8. 해쉬(Hash) : 원본 메세지 확인

#### 인증서 예제

```
$ openssl x509 -noout -text -in [crt] 
Certificate:
    Data:
        Version: 3 (0x2)
        Serial Number: 1 (0x1)
    Signature Algorithm: sha256WithRSAEncryption
        Issuer: C=KR, O=Openbase, CN=Openbases Self Signed CA
        Validity
            Not Before: Oct 22 00:32:19 2020 GMT
            Not After : Oct 20 00:32:19 2030 GMT
        Subject: C=KR, O=Openbase, CN=Openbases Self Signed CA
        Subject Public Key Info:
            Public Key Algorithm: rsaEncryption
                Public-Key: (2048 bit)
                Modulus:
                    00:9f:88:10:ff:01:1d:e3:3d:2a:e2:ba:b0:27:8c:
                    d9:db:86:31:9b:57:ab:d8:f5:45:e1:2c:e9:9c:8e:
                    7d:75:41:97:85:37:67:70:0e:8a:74:a6:fe:1e:1c:
                    57:45:bb:a6:08:ec:a4:fb:5b:b0:ef:b0:74:c6:09:
                    bb:07:87:6f:a7:29:c3:47:2e:5b:4d:30:c8:4d:7d:
                    35:bd:8c:83:c5:e4:8f:36:92:d6:6f:f2:06:af:99:
                    ce:9d:6a:30:3d:f7:31:54:9c:0f:e7:b6:1f:75:73:
                    4a:7c:42:a8:01:53:fa:85:59:bd:80:b8:35:84:01:
                    97:b3:0e:6f:ab:37:de:06:87:9f:d9:ae:24:ad:ab:
                    b2:96:6d:eb:8d:08:64:d3:68:7e:28:93:4d:c3:67:
                    d6:b4:22:40:40:c7:56:89:81:ac:df:48:64:48:01:
                    66:fb:27:b7:8b:2c:9a:b5:9a:b6:e4:a5:d5:dc:88:
                    fa:77:19:3e:b0:a4:88:5d:9e:b8:fb:ac:73:1e:9c:
                    83:91:c6:20:d7:b3:8b:ea:8f:33:04:e1:ea:b5:9b:
                    a8:10:3b:fd:7a:a0:4c:2d:ab:78:bc:e9:25:aa:c8:
                    07:e3:e3:a4:35:b9:49:de:9a:cd:9b:92:da:5f:fd:
                    35:7e:d4:d7:a4:e8:83:3c:48:51:c5:d7:a3:06:fa:
                    e4:7f
                Exponent: 65537 (0x10001)
        X509v3 extensions:
            X509v3 Basic Constraints: critical
                CA:TRUE, pathlen:0
            X509v3 Subject Key Identifier: 
                44:8D:5E:A7:A9:B8:68:A2:6A:CF:3B:9F:F9:C5:0B:D0:35:9B:5B:24
            X509v3 Key Usage: 
                Certificate Sign, CRL Sign
            Netscape Cert Type: 
                SSL Client, SSL Server
    Signature Algorithm: sha256WithRSAEncryption
         39:1f:6b:2b:69:5f:2b:5f:8f:d4:0c:79:cf:10:b8:a8:6a:31:
         c1:87:b2:2b:33:8a:b6:2f:7c:09:0f:f7:ee:2a:73:f8:d5:cc:
         a5:77:d2:48:14:e8:f0:21:14:96:ea:d6:d9:b2:2e:9a:d2:b6:
         91:c3:91:e7:48:71:1d:b2:59:40:0d:2a:ca:57:5c:62:94:fd:
         72:6b:bf:8f:75:aa:57:cd:bf:a8:9e:47:aa:59:46:e3:df:30:
         74:c8:0a:b8:2f:83:b9:39:0f:f9:ed:a0:ee:06:2f:ad:3c:24:
         29:7c:37:22:48:b9:20:5b:a3:36:b4:11:ad:c8:2b:e5:6b:ea:
         75:44:84:7f:c9:41:6d:ed:5b:31:17:5c:7c:35:f5:42:0f:24:
         15:2c:39:d6:4b:6c:99:36:49:6f:fb:92:6e:f7:a8:8b:2f:af:
         dc:f8:8c:06:a8:8a:20:e0:3a:8f:3d:2e:a8:01:ed:3d:6c:b5:
         29:89:64:d1:2c:70:99:20:f5:9a:79:86:91:14:9d:9e:c2:ca:
         34:ef:a8:ab:5e:86:dd:93:e2:cd:0e:2d:67:e1:75:7a:2c:da:
         27:f1:1f:5a:5b:f9:92:dd:e8:9f:8f:17:35:e2:00:f8:ea:0f:
         77:b7:e3:a8:b0:c2:a5:46:ad:24:19:c9:cf:73:2a:46:c9:c0:
         35:ba:a9:7e
```

### 서명

생성 - 해쉬 생성
생성 - Private Key로 해쉬 암호화
생성 - 암호화된 해쉬와 서명된 인증서를 메시지에 추가

확인 - 받는 사람은 따로 해쉬를 생성
확인 - 받은 메시지에 포함된 해쉬를 Public Key를 이용해서 복호화
확인 - 두 개의 해쉬 비교


### ★★★ Chain of Trust, CA 흐름


ROOT CA 
- rootCA.crt (Self Signed Certificate)
    - 내용 + RootCA PublicKey + RootCA.key로 암호화된 Hash
- RootCA.key (private key)

CA (Root CA Signed Certificate)
- CA.crt
    - 내용 + Ca PublicKey + RootCA.key로 암호화된 Hash
- CA.key (private key)

HAN, 개인 (CA Sigend Certificate)
- Han.crt
    - 내용 + han PublicKey + CA.key로 암호화된 Hash
- han.key (private key)

#### Root CA
모든 인증서는 CA(발급기관)이 있어야하나 Root CA는 서명해줄 상위기관이 없으므로 Root Ca의 개인키로 스스로의 인증서에 서명하여 최상위 인증기관 인증서를 만든다, 이렇게 스스로 서명한 ROOT CA인증서를 Self Signed Certificate라고 부른다.
- 신뢰 가능하다고 모두가 믿음
- 생성한 KeyPair(private,public key)로 스스로 인증서 생성 

#### CA
- 해시체크 : CA.crt의 Hash를 RootCA public key 로 복호화 == CA.crt 내용물을 가지고 해시 생성
    - CA.crt hash부분을 RootCA의 public Key로 복호화 성공 = 인증서가 RootCA.key로 암호화 됨 => RootCA의 인증을 받음 => **RootCA는 신뢰할 수 있으므로 CA도 신뢰할 수 있다.**
    - 복호화된 해시와 인증서 내용물을 가지고 해시생성된 값을 비교하여 같으면 내용물이 변경되지 않았다라고 판단가능 => 정상 인증서

#### Han
- 해시체크 : han.crt의 Hash를 CA public key 로 복호화 == han.crt 내용물을 가지고 해시 생성
    - CA는 RootCA에 의해 신뢰할 수 있는 기관으로 인증되었고 CA에 의해 생성된 han.crt도 CA에 인증을 받았기 때문에 han.crt 인증서도 신뢰할 수 있다.


##  openssl Root CA 생성 
```
# CA가 사용할 RSA key pair(public, private key) 생성
# 개인키 분실에 대비해 AES 256bit 로 암호화한다. AES 이므로 암호(pass phrase)를 분실하면 개인키를 얻을수 없으니 꼭 기억해야 한다.
openssl genrsa -aes256 -out openbase-rootca.key 2048

# 개인키의 유출 방지를 위해 group 과 other의 permission 을 모두 제거한다.
chmod 600 openbase-rootca.key

---------------- openssl 부가 정보 -------------------------------------------
# public key 생성
openssl rsa -in openbase-rootca.key -pubout -out public.key
# public key를 이용한 암호화
openssl rsautl -encrypt -pubin -inkey [public.key] -in [file] -out [output]
# private key를 이용한 복호화
openssl rsautl -decrypt -inkey [private key] -in [encoded file] -out [output]
------------------------------------------------------------------------------

# CSR(Certificate Signing Request) 생성을 위한 설정
vi rootca_openssl.conf 

#############################rootca_openssl.conf ##########################
[ req ]
default_bits            = 2048
default_md              = sha1
default_keyfile         = openbase.key
distinguished_name      = req_distinguished_name
extensions             = v3_ca
req_extensions = v3_ca

[ v3_ca ]
basicConstraints       = critical, CA:TRUE, pathlen:0
subjectKeyIdentifier   = hash
##authorityKeyIdentifier = keyid:always, issuer:always
keyUsage               = keyCertSign, cRLSign
nsCertType             = client, server
[req_distinguished_name ]
countryName                     = Country Name (2 letter code)
countryName_default             = KR
countryName_min                 = 2
countryName_max                 = 2

# 회사명 입력
organizationName              = Organization Name (eg, company)
organizationName_default      = Openbase

# 부서 입력
#organizationalUnitName          = Organizational Unit Name (eg, section)
#organizationalUnitName_default  = Lab

# SSL 서비스할 domain 명 입력
commonName                      = Common Name (eg, your name or your server's hostname)
commonName_default              = Openbase's Self Signed CA
commonName_max                  = 64
###########################################################################

#CSR 생성
openssl req -new -key openbase-rootca.key -out openbase-rootca.csr -config rootca_openssl.conf

# passphrase 제거 -> 서버 부팅시 비밀번호 묻는것을 방지
openssl rsa -in openbase-rootca.key -out openbase-rootca.key.nopasswd

# 10년짜리 Self-Signed Certificate 생성
# 서명에 사용할 해시 알고리즘을 변경하려면 -sha256, -sha384, -sha512 처럼 해시를 지정하는 옵션을 전달해 준다. 기본값은 -sha256 이며 openssl 1.0.2 이상이 필요
openssl x509 -req \
-days 3650 \
-extensions v3_ca \
-set_serial 1 \
-in openbase-rootca.csr \
-signkey openbase-rootca.key.nopasswd \
-out openbase-rootca.crt \
-extfile rootca_openssl.conf

#인증서 확인
openssl x509 -text -in openbase-rootca.crt
```

## SSL 인증서 생성
위 과정과 거의 비슷

```
# CA가 사용할 RSA key pair(public, private key) 생성
# 개인키 분실에 대비해 AES 256bit 로 암호화한다. AES 이므로 암호(pass phrase)를 분실하면 개인키를 얻을수 없으니 꼭 기억해야 한다.
openssl genrsa -aes256 -out hbh.key 2048

# 개인키의 유출 방지를 위해 group 과 other의 permission 을 모두 제거한다.
chmod 600 hbh.key

# CSR(Certificate Signing Request) 생성을 위한 설정
vi hbh_openssl.conf 
#############################hbh_openssl.conf ##########################

[ req ]
default_bits            = 2048
default_md              = sha1
default_keyfile         = openbase.key
distinguished_name      = req_distinguished_name
extensions             = v3_user
## 인증서 요청시에도 extension 이 들어가면 authorityKeyIdentifier 를 찾지 못해 에러가 나므로 막아둔다.
## req_extensions = v3_user

[ v3_user ]
# Extensions to add to a certificate request
basicConstraints = CA:FALSE
authorityKeyIdentifier = keyid,issuer
subjectKeyIdentifier = hash
keyUsage               = nonRepudiation, digitalSignature, keyEncipherment
## SSL 용 확장키 필드
extendedKeyUsage = serverAuth,clientAuth
subjectAltName          = @alt_names

[ alt_names]
## Subject AltName의 DNSName field에 SSL Host 의 도메인 이름을 적어준다.
## 멀티 도메인일 경우 *.hbh.com 처럼 쓸 수 있다.
DNS.1   = www.hbh.com
DNS.2   = hbh.com
DNS.3   = *.hbh.com

[req_distinguished_name ]
countryName                     = Country Name (2 letter code)
countryName_default             = KR
countryName_min                 = 2
countryName_max                 = 2

# 회사명 입력
organizationName              = Organization Name (eg, company)
organizationName_default      = Openbase

# 부서 입력
#organizationalUnitName          = Organizational Unit Name (eg, section)
#organizationalUnitName_default  = Lab

# SSL 서비스할 domain 명 입력
commonName                      = Common Name (eg, your name or your server's hostname)
commonName_default              = hbh
commonName_max                  = 64
############################################################################

#CSR 생성
openssl req -new -key hbh.key -out hbh.csr -config hbh_openssl.conf

# passphrase 제거 -> 서버 부팅시 비밀번호 묻는것을 방지
openssl rsa -in hbh.key -out hbh.key.nopasswd

# 10년짜리 Root CA에 의한 인증서 발급

openssl x509 -req -days 3650 -extensions v3_user -in hbh.csr \
-CA rootca.crt -CAcreateserial \
-CAkey  rootca.key \
-out hbh.crt  -extfile hbh_openssl.conf

#인증서 확인
openssl x509 -text -in hbh.crt

SSL 
인증서 
