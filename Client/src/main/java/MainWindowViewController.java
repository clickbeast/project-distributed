package UserInterface;

import VirtueelGeheugen.DataProcessing.Processing.XMLProcessor;
import VirtueelGeheugen.Simulation.SimulationManager;
import VirtueelGeheugen.Simulation.SimulationState;
import VirtueelGeheugen.UserInterface.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainWindowViewController implements Initializable {

    public Scene scene;

    //handles the main simulation of the RAM etc.
    private SimulationManager simulationManager;
    private HistoryManager historyManager;

    //manages color highlights inside the application
    private HighlightManager highlightManager;


    /**
        VARIABLES -  ELEMENTS
     */



    //Top Control Bar
    public Label alertInfoLabel;
    public ChoiceBox choiceBox;
    public Button executeAllInstructionsButton;
    public Button executeNextInstructionButton;
    public Button backwardHistoryButton;
    public Button forwardHistoryButton;
    public Button playHistoryButton;


    //Summary Windows
    public Label timer;
    public Label totalAmountOfWritesToRamLabel;
    public Label totalAmountsOfWritesToPercistentLabel;

    //STATE WINDOW
    public TitledPane currentStatePane;
    public TitledPane currentPageTablePane;

    //Current instruction
    public Label currentInstructionOperationLabel;
    public Label currentInstructionVirtualAdressLabel;
    public Label currentInstructionPhysicalAdressLabel;
    public Label currentInstructionProcessIdLabel;
    public Label currentInstructionProcessWritesToRamLabel;
    public Label currentInstructionProcessWritesToPercistentLabel;


    //Previous Instrcution 
    public Label previousInstructionOperationLabel;
    public Label previousInstructionVirtualAdressLabel;
    public Label previousInstructionPhysicalAdressLabel;
    public Label previousInstructionProcessIdLabel;
    public Label previousInstructionProcessWritesToRamLabel;
    public Label previousInstructionProcessWritesToPercistentLabel;

    //contains the view for pagetable // ram
    public PageTableView pageTableView;
    public RamView ramView;


    public AnchorPane pageTableAnchorPane;
    public AnchorPane ramTableAnchorPane;



    //Last removed Process
    public Label lastRemovedProcessIdLabel;
    public Label lastRemovedProcessAmountOfWritesToRam;
    public Label lastRemovedProcessAmountOfWritesToPercistent;


    //View Option Bar
    public ToggleButton highlightPagesButton;
    public ToggleButton highlightProcessesButton;

    //toolbar itself
    public ToolBar topToolBar;

    public ChoiceBox pagingMethodChoiceBox;


    private int currentSelectedXMLIndex = 0;
    private String document;

    public ToolBar bottomToolBar;

    public ToggleButton highlightPages;
    public ToggleButton highlightProcesses;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initializing...");
        this.configureUIElements();

        //Confiure Hisotry Manager
        this.historyManager = new HistoryManager();
        document = "Instructions_30_3.xml";

    }


    //Configures all Elements when starting the application
    public void configureUIElements() {
        System.out.println("Configuring UI Elements");

        //configure xml select button
        choiceBox.setItems(FXCollections.observableArrayList("30 Instructies","20000_4 Instructies","20000_20 Instructies"));
        choiceBox.getSelectionModel().selectFirst();
        choiceBox.getSelectionModel().getSelectedIndex();
        this.currentSelectedXMLIndex = choiceBox.getSelectionModel().getSelectedIndex();

        //keep the xml xhoice box in het oog wanneer er iets veranderd
        choiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                System.out.println("Preparing to load in new fxml -> generate warning");

                if(currentSelectedXMLIndex != newValue.intValue()) {
                    switch ((int) newValue) {
                        case 0:
                            System.out.println("0");
                            System.out.println("Preparing to load in new fxml -> generate warning");
                            changeXMLWarning();
                            break;
                        case 1:
                            System.out.println("1");
                            changeXMLWarning();
                            break;
                        default:
                            System.out.println("2");
                            changeXMLWarning();
                            break;
                    }
                }
            }

        });

        this.ramView = new RamView();
        this.pageTableView = new PageTableView();

        this.highlightManager = new HighlightManager(this.pageTableView,this.ramView);


        //configure bounds
        AnchorPane.setBottomAnchor(this.pageTableView, -10.0);
        AnchorPane.setTopAnchor(this.pageTableView,-10.0);
        AnchorPane.setRightAnchor(this.pageTableView,-10.0);
        AnchorPane.setLeftAnchor(this.pageTableView,-10.0);

        AnchorPane.setBottomAnchor(this.ramView, -10.0);
        AnchorPane.setTopAnchor(this.ramView,-10.0);
        AnchorPane.setRightAnchor(this.ramView,-10.0);
        AnchorPane.setLeftAnchor(this.ramView,-10.0);

        //configure list views
        this.pageTableAnchorPane.getChildren().add(pageTableView);
        this.ramTableAnchorPane.getChildren().add(ramView);

        pagingMethodChoiceBox.setItems(FXCollections.observableArrayList("PrePaging","DemandPaging"));
        pagingMethodChoiceBox.getSelectionModel().selectFirst();
        pagingMethodChoiceBox.getSelectionModel().getSelectedIndex();
        this.currentSelectedXMLIndex = pagingMethodChoiceBox.getSelectionModel().getSelectedIndex();


        //configure paging method choice box
        pagingMethodChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {


                    switch ((int) newValue) {
                        case 0:
                            System.out.println("Changing paging method -> Prepaging");

                            simulationManager.runPrePaging();
                            break;
                        case 1:
                            System.out.println("Changing paging method -> Demand Paging");
                            simulationManager.runDemandPaging();
                            break;
                        default:
                            break;
                    }
            }
        });


        this.configureViewOptionToggleButton();

    }

    public void configureViewOptionToggleButton() {

        final ToggleGroup viewToggleGroup = new ToggleGroup();
        viewToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov, Toggle toggle, Toggle new_toggle) {
                if (new_toggle == null) {
                    //nothing selected
                    highlightManager.removeHighlights();
                }else{
                    if(viewToggleGroup.getSelectedToggle().getUserData().equals("highlightPage")) {
                        highlightManager.setDefault("highlightPage");
                        highlightManager.removeHighlights();
                        highlightManager.highlightSameFramesAndPages();
                    }else{
                        highlightManager.setDefault("highlightProcess");
                        highlightManager.removeHighlights();
                        highlightManager.highlightSameProcesses();
                    }
                }

            }
        });

        highlightPagesButton.setUserData("highlightPage");
        highlightPagesButton.setToggleGroup(viewToggleGroup);
        highlightPagesButton.setSelected(true);

        highlightProcessesButton.setUserData("highlightProcess");
        highlightProcessesButton.setToggleGroup(viewToggleGroup);

    }

    //Genertates a warning when the user is about to change xml file
    public void changeXMLWarning() {


        Optional<ButtonType> result = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to change XML? Current Simulation Will be removed. And History will be lost",
                ButtonType.CANCEL,ButtonType.OK
        ).showAndWait();


        if(result.get() == ButtonType.OK) {
            System.out.println("Resetting and loading new xml");
            this.currentSelectedXMLIndex = this.choiceBox.getSelectionModel().getSelectedIndex();
            this.reset();
        }else{
            //cancel
            System.out.println("CANCEL");
            this.choiceBox.getSelectionModel().select(this.currentSelectedXMLIndex);
        }
    }





    //get called when Main is finshed with doing its things
    public void startFinished() {
        System.out.println("Finished setup of program");

        //check how the buttons of the htsory have to be configured
        this.disableCheck();
    }


    public void setSimulationManager(SimulationManager simulationManager) {
        this.simulationManager = simulationManager;
    }





    /**
     * UI ACTIONS
     *
     */



    public void runNextInstruction() {

        System.out.println("Running next instruction");

        //DISABLE UI
        this.freezeUI();

        //get the new simulatoon state after instruction
        SimulationState simulationState = simulationManager.runStep();

        //If simulation state is zero then we have done the complete simulation , give message to user and reset
        if(simulationState == null) {
            this.runningDoneAlert();
        }else {
            //generate UI Appropiate data based on current simulation state
            UIState newUIState = new UIState(simulationState);
            this.historyManager.addNewState(newUIState);
            this.updateUIBasedOnNewState(newUIState);

            //Always add first to history  manager
            //this.historyManager.addNewState(newUIState);


        }

        this.unFreezeUI();
        this.disableCheck();


    }

    //Runs each instruction
    public void runAllInstructions() {

        System.out.println("Running All Instructions after eachother");

        Optional<ButtonType> result = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to run all Instructions? Current running simulation will be removed and history will be lost.",
                ButtonType.CANCEL,ButtonType.OK
        ).showAndWait();


        if(result.get() == ButtonType.OK) {
            System.out.println("Resetting everything" +
                    "");

              this.reset();


            //EXECUTION

            SimulationState simulationState = this.simulationManager.runStep();

            if(simulationState != null) {
                //Convert
                UIState uiState = new UIState(simulationState);
                this.historyManager.addNewState(uiState);


                while (simulationState != null) {
                    simulationState = this.simulationManager.runStep();
                    if(simulationState != null) {
                        this.historyManager.addNewState(new UIState(simulationState));
                    }
                }

                //SHOW UI
                this.updateUIBasedOnNewState(this.historyManager.latestState());
                this.disableCheck();

                System.out.println("DONE WITH RUNNING ALL");
            }
        }else{
            //cancel
            System.out.println("Canceled");
            this.disableCheck();
        }

    }


    public void backHistoryAction(ActionEvent actionEvent) {
        this.updateUIBasedOnNewState(this.historyManager.goToPreviousState());
        this.disableCheck();
    }

    public void forwardHistoryAction(ActionEvent actionEvent) {
        this.updateUIBasedOnNewState(this.historyManager.nextState());
        this.disableCheck();
    }



    /*
        Gets called when the system wants to start again
     */
    public void reset() {
        System.out.println("RESETTING");
        this.historyManager.reset();


        boolean prepaging = this.simulationManager.getPrepaging();
        //reset manager
        simulationManager = new SimulationManager();
        if(prepaging) {
            simulationManager.runPrePaging();
        }else{
            simulationManager.runDemandPaging();
        }


        //Load in XML DOC
        System.out.println("Loading XML Document");


        //TODO aan te passen
        switch (this.choiceBox.getSelectionModel().getSelectedIndex()) {

            case 0:
                 System.out.println("USE 30 xml file");
                simulationManager.setInstructionList(new XMLProcessor().generateProcessListBasedOnXML("Instructions_30_3.xml"));
                break;

            case 1:
                simulationManager.setInstructionList(new XMLProcessor().generateProcessListBasedOnXML("Instructions_20000_4.xml"));
                break;
            case 2:
                simulationManager.setInstructionList(new XMLProcessor().generateProcessListBasedOnXML("Instructions_20000_20.xml"));
                break;

                default:
                    System.out.println("deg");
        }



        //reset UI
        this.updateUIBasedOnNewState(new UIState(null));


        //enable evetyhing again ready for use
        this.unFreezeUI();
        this.disableCheck();

    }




    //gets called when all instructions have been done
    public void runningDoneAlert () {
        Optional<ButtonType> result = new Alert(
                Alert.AlertType.INFORMATION,
                "No more instuctions Left. Do you wish to restart?",
                ButtonType.NO, ButtonType.YES
        ).showAndWait();

        if(result.get() == ButtonType.YES) {
            System.out.println("Resetting everything" + "");
            this.reset();
        }else{
            //do nothing
            System.out.println("NO CALLED");
        }
    }





    /**
     *
     * UI CONFIG
     *
     */


    //checks and disables certain buttons when needed
    public void disableCheck() {
        if(historyManager.isFirstState()) {
            this.backwardHistoryButton.setDisable(true);
        }else{
            this.backwardHistoryButton.setDisable(false);
        }

        if(historyManager.isLastState()) {
            this.forwardHistoryButton.setDisable(true);
        }else{
            this.forwardHistoryButton.setDisable(false);
        }
    }


    /**
     *
     * UI FILL
     *
     */


    //changes the content being shown by the UI
    public void updateUIBasedOnNewState(UIState uiState) {
        if(uiState != null) {

            if(historyManager.isPresent()) {
                this.currentStatePane.setText("Current State");

            }else{
                this.currentStatePane.setText("Viewing History State");
            }

            //SET SUMMARY
            this.timer.setText("Timer: " + String.valueOf(uiState.getTimerValue()));
            this.totalAmountOfWritesToRamLabel.setText(" #Writes To Ram: " + String.valueOf(uiState.getTotalAmountOfWritesToRam()));
            this.totalAmountsOfWritesToPercistentLabel.setText("#Writes to Percitent: " + String.valueOf(uiState.getTotalAmountOfWritesToPercistent()));

            //current instruction
            this.currentInstructionOperationLabel.setText(uiState.getCurrentInstructionOperation());
            this.currentInstructionVirtualAdressLabel.setText(uiState.getCurrentInstructionVirtualAdress());
            this.currentInstructionPhysicalAdressLabel.setText(uiState.getCurrentInstructionPhysicalAdress());
            
            this.currentInstructionProcessIdLabel.setText("Id: " + String.valueOf(uiState.getCurrentInstructionProcessId()));
            this.currentInstructionProcessWritesToRamLabel.setText("#Writes To Ram: " + String.valueOf(uiState.getCurrentInsructionProcessNumberOfWritesToRam()));
            this.currentInstructionProcessWritesToPercistentLabel.setText("#Writes To Percistent: " + String.valueOf(uiState.getCurrentInstructionProcessNumberOfWritesToPercistent()));
            

            //previous insruction

            //check hisotryManager if there is a previous state
            if(this.historyManager.accessPreviousState() != null) {
                this.previousInstructionOperationLabel.setText(this.historyManager.accessPreviousState().getCurrentInstructionOperation());
                this.previousInstructionVirtualAdressLabel.setText(this.historyManager.accessPreviousState().getCurrentInstructionVirtualAdress());
                this.previousInstructionPhysicalAdressLabel.setText(this.historyManager.accessPreviousState().getCurrentInstructionPhysicalAdress());

                this.previousInstructionProcessIdLabel.setText("Id: " +
                        String.valueOf(this.historyManager.accessPreviousState().getCurrentInstructionProcessId()));
                this.previousInstructionProcessWritesToRamLabel.setText("#Writes To Ram: " +
                        String.valueOf(this.historyManager.accessPreviousState().getCurrentInsructionProcessNumberOfWritesToRam()));
                this.previousInstructionProcessWritesToPercistentLabel.setText("#Writes To Percistent: " +
                         String.valueOf(this.historyManager.accessPreviousState().getCurrentInstructionProcessNumberOfWritesToPercistent()));

            }else{
                this.previousInstructionOperationLabel.setText("");
                this.previousInstructionPhysicalAdressLabel.setText("");
                this.previousInstructionVirtualAdressLabel.setText("");

                this.previousInstructionProcessIdLabel.setText("Id: ");
                this.previousInstructionProcessWritesToPercistentLabel.setText("#Writes To Percistent: ");
                this.previousInstructionProcessWritesToRamLabel.setText("#Writes To Ram: ");

            }

            //pagetable
            this.currentPageTablePane.setText("Page Table Of Executed Process " + "<" + String.valueOf(uiState.getCurrentInstructionProcessId()) + ">");
            this.pageTableView.setProcessId(uiState.getCurrentInstructionProcessId());

            this.pageTableView.fillWithData(uiState.getPageTableCells());

            //ram representation
            this.ramView.fillWithData(uiState.getRamTableCells());

            //leaving progress
            this.lastRemovedProcessIdLabel.setText(uiState.getLastProcessRemovedFromRamId());
            this.lastRemovedProcessAmountOfWritesToRam.setText(uiState.getLastProcessRemovedFromRamNumberOfWritesToRam());
            this.lastRemovedProcessAmountOfWritesToPercistent.setText(uiState.getLastProcessRemovedFromRamNumberOfWritesToPercistent());

        }else{
            basicFill();
        }


        this.highlightForCurrentOption();

    }

    public void highlightForCurrentOption() {
        String oldDef = this.highlightManager.getaDefault();
        this.highlightManager = new HighlightManager(this.pageTableView,this.ramView);
        this.highlightManager.setDefault(oldDef);
        this.highlightManager.highlightDefault();
    }

    public void basicFill() {
        //when no data is present-
        this.currentStatePane.setText("No Instruction executed");
    }



    /**
     *
     * UI CHANGES
     *
     *
     */



    //prevent user from using History, commonly used while running algorithm
    public void freezeUI() {
        //TODO
        this.forwardHistoryButton.setDisable(true);
        this.backwardHistoryButton.setDisable(true);
        this.executeNextInstructionButton.setDisable(true);
        this.executeAllInstructionsButton.setDisable(true);
        this.topToolBar.setDisable(true);
        this.bottomToolBar.setDisable(true);


    }

    //allow user to use History again
    public void unFreezeUI() {
        this.forwardHistoryButton.setDisable(false);
        this.backwardHistoryButton.setDisable(false);
        this.executeNextInstructionButton.setDisable(false);
        this.executeAllInstructionsButton.setDisable(false);
        this.topToolBar.setDisable(false);
        this.bottomToolBar.setDisable(false);
    }


    /**
     *  Alerts
     */


    //display info messages in the History while running the app
    public void displayInfoMessage(String message) {
        alertInfoLabel.setText("Info: " + message);
    }


}
