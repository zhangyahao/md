1. 主要是Period类方法getYears（），getMonths（）和getDays（）来计算.
    ```$xslt
        import java.time.LocalDate;
        import java.time.Month;
        import java.time.Period;
        
        public class Test {
        
            public static void main(String[] args) {
                LocalDate today = LocalDate.now();
                System.out.println("Today : " + today);
                LocalDate birthDate = LocalDate.of(1993, Month.OCTOBER, 19);
                System.out.println("BirthDate : " + birthDate);
        
                Period p = Period.between(birthDate, today);
                System.out.printf("年龄 : %d 年 %d 月 %d 日", p.getYears(), p.getMonths(), p.getDays());
            }
        }
    ```