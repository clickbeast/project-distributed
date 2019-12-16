import java.util.ArrayList;
import java.util.List;

public class Master {
    List<Slave> slaves;
    private void addSlave(Slave slave){
        if(slaves==null){
            slaves=new ArrayList<>();
        }
        slaves.add(slave);
    }



}
