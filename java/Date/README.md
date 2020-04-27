# Date

Java 8에서부터 datetime에 관한 클래스가 새로나옴

![](/images/datetime.png)

### 사용도

UTC시간 - > Instant

지역+UTC시간 -> ZonedDateTime

날짜 + 시간 -> LocalDateTime

날짜 -> LocalDate

시간 -> LocalTime



### 포맷 

```
DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX");
```

```
// UTC 기준 1970-01-01
		Instant a = Instant.EPOCH;
		System.out.println(a);

		// UTC 현재시간
		a = Instant.now();
		System.out.println(a); // 2019-09-11T02:26:50.602Z
		System.out.println(a.getEpochSecond()); // 1568168894

		// UTC
		a = Instant.ofEpochMilli(1L);
		System.out.println(a); // 1970-01-01T00:00:00.001Z
		a = Instant.ofEpochSecond(1);
```