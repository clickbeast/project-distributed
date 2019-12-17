
public class testMain {
    public static void main(String[] args) {
        ServerInfoManager serverInfoManage = new ServerInfoManager();
        Master master = new Master();
        ThreadListener lister = new ThreadListener() {
            @Override
            public void onUpdate(Master m) {
                System.out.println(master);
            }
        };
        serverInfoManage.checkBoxes(master, lister);
    }
}
