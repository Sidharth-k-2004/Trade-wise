package Learning_1.First;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Dev {
    @Autowired  // field injection : usually not preffered.. try setter or constructor injection
    public Laptop laptop;
    public void builds()  {
        laptop.compile();
        System.out.println("Heyy we build objects virtually by coding....");
    }
}
