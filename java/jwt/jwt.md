```java
package xyz.phkproject.webcore.jwt;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

public class JWT_TEST {

    //
    public static String encode(String key, String data) throws Exception {
	Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
	SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
	sha256_HMAC.init(secret_key);

	return Base64.getUrlEncoder().encodeToString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
    }

    public static void main(String[] args) {

	// jwt 알고리즘 라이브러리 없이 사용
	// JWT 구조 : HMACSHA256(base64UrlEncode(header) + "." + base64UrlEncode(payload),secretKey)

	try {
	    // json에서 개행문자 띄어쓰기 모두제거
	    String header = "{\"alg\":\"HS256\"}";
	    String claims = "{\"sub\":\"Joe\"}";

	    System.out.println("header : " + header);
	    System.out.println("claims : " + claims);

	    // base64로 인코딩 및 인코딩 후 '=' 패딩문자 제거
	    String headerEncode = Base64.getUrlEncoder().encodeToString(header.getBytes("UTF-8")).replace("=", "");
	    String claimsEncode = Base64.getUrlEncoder().encodeToString(claims.getBytes("UTF-8")).replace("=", "");

	    System.out.println("Encoded Header :" + headerEncode);
	    System.out.println("Encoded Claims :" + claimsEncode);

	    // headerEncode.claimsEncode 구조로 합침
	    String concatenated = headerEncode + '.' + claimsEncode;

	    // SecretKey
	    String secretKey = "JWT연습용시크릿키입니다.";

	    // Base64 인코딩된 SecretKey
	    String secretKey2 = Base64.getUrlEncoder().encodeToString(secretKey.getBytes("UTF-8")).replace("=", "");

	    System.out.println("SecretKey : " + secretKey);
	    System.out.println("EncodedSecretKey : " + secretKey2);

	    String signature = encode(secretKey, concatenated);
	    byte[] decode = Base64.getUrlDecoder().decode(secretKey2);
	    String signature2 = encode(new String(decode), concatenated);

	    String lastData = (concatenated + "." + signature).replace("=", "");
	    String lastData2 = (concatenated + "." + signature2).replace("=", "");

	    System.out.println("JWT TOKEN : " + lastData);
	    System.out.println("JWT TOKEN(Encoded SecretKey) : " + lastData2);
	} catch (UnsupportedEncodingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	System.out.println("라이브러리사용 ");
	// 라이브러리 사용해서 사용

	try {

	    //라이브러리에서는 시크릿키 256비트 이상만사용가능
	    String secretKey = "JWT연습용시크릿키입니다.";

	    // Base64 인코딩된 SecretKey
	    String secretKey2 = Encoders.BASE64URL.encode(secretKey.getBytes("UTF-8"));

	    System.out.println("SecretKey : " + secretKey);
	    System.out.println("EncodedSecretKey : " + secretKey2);

	    /*
	     * 랜덤으로 알고리즘에 맞게 최소길이의 키를 생성  => 어플리케이션 실행할때 생성해놓고 static으로 사용해도 좋을듯
	     * HS256 256 bits (32 bytes) 
	     * HS384 384 bits (48 bytes) 
	     * HS512 512 bits (64 bytes) 
	     *
	     * */
	    Key randkey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

	    //테스트는 랜덤키 사용안함
	    //hmacShaKeyFor => SHA256 은 256비트(8바이트) 보다 큰 키를 사용해야함 아니면 여기서 오류발생
	    Key secretKeyLib = Keys.hmacShaKeyFor(secretKey.getBytes("UTF-8"));

	    //base64로 인코딩된 키는 다시 디코딩하여 원래키로 변환 시켜야함
	    //jwt.io에서 secret base64 encoded 누를경우 이경우에 해당됨 , 
	    //`JWT연습용시크릿키입니다.` 키 대신 `SldU7Jew7Iq17Jqp7Iuc7YGs66a_7YKk7J6F64uI64ukLg`로 넣은상황=>다시 `JWT연습용시크릿키입니다.`로 변환 후 실행
	    byte[] decodedSecret = Base64.getUrlDecoder().decode(secretKey2);
	    Key secretKeyLib2 = Keys.hmacShaKeyFor(decodedSecret);

	    String compact = Jwts.builder().setSubject("Joe").signWith(secretKeyLib).compact();
	    String compact2 = Jwts.builder().setSubject("Joe").signWith(secretKeyLib2).compact();

	    System.out.println("JWT TOKEN : " + compact);
	    System.out.println("JWT TOKEN(Encoded SecretKey) : " + compact2);

	    //파싱
	    Jws<Claims> parse = Jwts.parserBuilder().setSigningKey(secretKeyLib).build().parseClaimsJws(compact);
	    Jws<Claims> parse2 = Jwts.parserBuilder().setSigningKey(secretKeyLib2).build().parseClaimsJws(compact2);

	    System.out.println(parse.getBody().getSubject());
	    System.out.println(parse2.getBody().getSubject());

	} catch (UnsupportedEncodingException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}

    }
}

```



```
라이브러리 jjwt
<!-- JWT 라이브러리 -->
<dependency>
	<groupId>io.jsonwebtoken</groupId>
	<artifactId>jjwt-api</artifactId>
	<version>0.11.1</version>
</dependency>
<dependency>
	<groupId>io.jsonwebtoken</groupId>
	<artifactId>jjwt-impl</artifactId>
	<version>0.11.1</version>
	<scope>runtime</scope>
</dependency>
<dependency>
	<groupId>io.jsonwebtoken</groupId>
	<artifactId>jjwt-jackson</artifactId> 
	<version>0.11.1</version>
	<scope>runtime</scope>
</dependency>
```

