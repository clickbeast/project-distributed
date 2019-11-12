package UserInterface;

import VirtueelGeheugen.DataProcessing.Processing.XMLProcessor;
import VirtueelGeheugen.Simulation.SimulationManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private SimulationManager simulationManager;


    //keep a reference to the main  window controller
    public MainWindowViewController mainwindow;


    @Override
    public void start(Stage primaryStage) throws Exception{

        this.simulationManager = new SimulationManager();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));

        //provide the controller with a reference of the simulationManager
        loader.setControllerFactory( c -> {
            if(c == MainWindowViewController.class) {
                MainWindowViewController mc = new MainWindowViewController();
                mc.setSimulationManager(this.simulationManager);
                mainwindow = mc;
                return mc;
            }else{
                try {
                    return c.newInstance();
                }catch (Exception exc){
                    throw new RuntimeException(exc);
                }
            }
        });


        Parent flowPane = loader.load();

        primaryStage.setTitle("Memory Simulator");
        //primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(flowPane, 1200, 860));
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(750);
        primaryStage.show();
        this.simulationManager.setMainWindowController(mainwindow);
        mainwindow.scene = primaryStage.getScene();

        System.out.println("SETTING XML DATA");
        this.simulationManager.setInstructionList(new XMLProcessor().generateProcessListBasedOnXML("Instructions_30_3.xml"));
        this.mainwindow.startFinished();



        /**
         *
         *  RUN YOUR TESTS BELOW THIS , COMMENT  ABOVE CODE WHEN NO NEED FOR History (temp)
         */




    }


    public static void main(String[] args) {
        launch(args);
    }
}