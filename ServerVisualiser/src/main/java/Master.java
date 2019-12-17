import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class Master {
    ObservableList<Slave> slaves;

    public Master() {
        slaves = FXCollections.observableArrayList();
        ;
    }

    public void addSlave(Slave slave) {
        if (slaves == null) {
            slaves = FXCollections.observableArrayList();
        }
        if (!slaves.stream().anyMatch(x -> x.getName().equals(slave.getName()))) {

            slaves.add(slave);
        }
    }

    public List<Slave> getSlaves() {
        return slaves;
    }

    public void setSlaves(ObservableList<Slave> slaves) {
        this.slaves = slaves;
    }

    @Override
    public String toString() {
        return "Master{" +
                "slaves=" + slaves +
                '}';
    }
}
