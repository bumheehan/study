import java.time.LocalDate;

public interface DateCalculator {

  public void calculateInternationalAge(LocalDate ldt);

  public void calculateKoreanAge(LocalDate ldt);

  public void calculateDDay(LocalDate ldt);

}
