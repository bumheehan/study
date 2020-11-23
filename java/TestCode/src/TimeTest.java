import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeTest {

  public static void main(String[] args) {


    LocalDateTime now = LocalDateTime.now();
    System.out.println(now);

    ZonedDateTime atZone = now.atZone(ZoneId.systemDefault());
    System.out.println(atZone);

    ZonedDateTime withZoneSameInstant = atZone.withZoneSameInstant(ZoneId.of("UTC"));
    ZonedDateTime withZoneSameLocal = atZone.withZoneSameLocal(ZoneId.of("UTC"));
    System.out.println(withZoneSameInstant);
    System.out.println(withZoneSameLocal);

    ZonedDateTime UTC = now.atZone(ZoneId.of("UTC"));
    System.out.println(UTC);
    ZonedDateTime utc = ZonedDateTime.now(ZoneId.of("UTC"));
    System.out.println(utc);

    ZonedDateTime now2 = ZonedDateTime.now();
    System.out.println(now2);

  }
}
