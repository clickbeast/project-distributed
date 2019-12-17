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

    public List<Slave> getSlaves() {
        return slaves;
    }

    public void setSlaves(List<Slave> slaves) {
        this.slaves = slaves;
    }

    @Override
    public String toString() {
        return "Master{" +
                "slaves=" + slaves +
                '}';
    }
}
