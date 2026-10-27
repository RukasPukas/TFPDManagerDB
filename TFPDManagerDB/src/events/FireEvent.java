package events;

import java.sql.SQLException;
import java.time.LocalDate;

import application.UserOptionsHub;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import universal.PopulateLists;
import universal.Styling;

public class FireEvent
{

	public static void fireEventInitalizer(String enteredUsername, int userSecurityLevel, VBox mainArea, VBox navBar, Group root, Scene scene) throws SQLException
	{
		mainArea.getChildren().clear();
		UserOptionsHub.setUpUserOptionVBox(mainArea, navBar, root, scene);
		
		
		
		//main VBox
	    VBox callInformationArea = new VBox();
	    callInformationArea.setSpacing(20);
	    callInformationArea.setPadding(new Insets(10));
	    
	    //Fire Event Header Text
		Text fireHeaderText = new Text("Add Fire Event");
		Styling.fireGradient();
		fireHeaderText.setFill(Styling.fireGradient());
		fireHeaderText.setFont(Font.font("Menlo",FontWeight.BOLD,24));
		
		//Dispatch Information VBox Section
		GridPane dispatchSection = new GridPane();
		//------------------------------------------------------row 1 -----------------------------------------

        Text runNumberLabel = new Text("Run Number:");
        runNumberLabel.setFill(Styling.fireGradient());
        runNumberLabel.setFont(Font.font("Menlo",FontWeight.BOLD,18));
        GridPane.setMargin(runNumberLabel, new Insets(0, 15, 50, 0));
        
        
        String getYear = String.valueOf(LocalDate.now().getYear());
        TextField runYearTF = new TextField(getYear);
        runYearTF.setPromptText("Year");
        runYearTF.setPrefWidth(50);
        runYearTF.setFont(Font.font("Menlo",FontWeight.BOLD,14));
        GridPane.setMargin(runYearTF, new Insets(0, 25, 0, 0));
        runYearTF.setTextFormatter(new TextFormatter<String>(change ->
        {
            if (change.getControlNewText().matches("[0-9]*"))
            {
                return change;
            }

            return null;
        }));
        


        TextField runNumberTF = new TextField();
        runNumberTF.setPromptText("Run Number");
        GridPane.setMargin(runNumberTF, new Insets(0, 10, 0, 0));
        runNumberTF.setFont(Font.font("Menlo",FontWeight.BOLD,14));
        runNumberTF.setTextFormatter(new TextFormatter<String>(change ->
        {
            if (change.getControlNewText().matches("[0-9]*"))
            {
                return change;
            }

            return null;
        }));

       
        
        Text noLeadingPrompt = new Text("(No leading 0s, only the run number itself.)");
        noLeadingPrompt.setFill(Styling.fireGradient());
        noLeadingPrompt.setFont(Font.font("Menlo",FontWeight.BOLD,12));
        GridPane.setMargin(noLeadingPrompt, new Insets(0,10,0,0));

        
        // column -> Row
        dispatchSection.add(runNumberLabel, 0, 0);
        dispatchSection.add(runYearTF, 1, 0);
        dispatchSection.add(runNumberTF, 2, 0);
        dispatchSection.add(noLeadingPrompt, 3, 0);

        
        Platform.runLater(() -> runNumberTF.requestFocus());
        
        

        //-----------------------------------------------end row 1 -----------------------------------------------------
        
        //-----------------------------------------------Row 2----------------------------------------------------------
        Text datePickerLabel = new Text("Select Date of Run:");
        datePickerLabel.setFill(Styling.fireGradient());
        datePickerLabel.setFont(Font.font("Menlo",FontWeight.BOLD,18));
        GridPane.setMargin(datePickerLabel, new Insets(0, 15, 50, 0));
        
        DatePicker eventDatePicker = new DatePicker();
        eventDatePicker.getEditor().setFont(Font.font("Menlo", FontWeight.BOLD, 14));

		// column -> Row
        dispatchSection.add(datePickerLabel, 0, 1);
        dispatchSection.add(eventDatePicker, 1, 1);
        
        //-------------------------------------------end Row 2-----------------------------------------------------------
        
        //-------------------------------------------Row 3--------------------------------------------------------------
        Text timeCallLabel = new Text("Time Call Was Generated:");
        timeCallLabel.setFill(Styling.fireGradient());
        timeCallLabel.setFont(Font.font("Menlo",FontWeight.BOLD,18));
        GridPane.setMargin(timeCallLabel, new Insets(0, 15, 50, 0));
        
        TextField hourTF = new TextField();
        hourTF.setPromptText("Time  in 24 hour format");
        hourTF.setPrefWidth(50);
        hourTF.setFont(Font.font("Menlo",FontWeight.BOLD,14));
        GridPane.setMargin(hourTF, new Insets(0, 25, 0, 0));
        hourTF.setTextFormatter(new TextFormatter<String>(change ->
        {
            String newText = change.getControlNewText();
            
            if (change.getControlNewText().matches("[0-9]*") && newText.length() <= 4)
            {
                return change;
            }

            return null;
        }));
        
        Text hourExamplePrompt = new Text("(Example: 0630)");
        hourExamplePrompt.setFill(Styling.fireGradient());
        hourExamplePrompt.setFont(Font.font("Menlo",FontWeight.BOLD,12));
        GridPane.setMargin(hourExamplePrompt, new Insets(0,25,0,0));
        
        dispatchSection.add(timeCallLabel, 0, 2);
        dispatchSection.add(hourTF, 1, 2);
        dispatchSection.add(hourExamplePrompt, 2, 2);
        
        Text uitilzedAssets = new Text("Utilized Apparatus:");
        uitilzedAssets.setFill(Styling.logoGradient());
        uitilzedAssets.setFont(Font.font("Menlo",FontWeight.BOLD,18));
        GridPane.setMargin(uitilzedAssets, new Insets(0, 15, 50, 0));
        
        
        ObservableList<String> allApparati =
                FXCollections.observableArrayList(PopulateLists.departmentVehicles());
        

        ListView<String> allApparatiList =new ListView<>(allApparati);

        allApparatiList.setMinSize(300, 150);
        allApparatiList.setPrefSize(300, 150);
        allApparatiList.setMaxSize(300, 150);
        allApparatiList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        allApparatiList.setStyle(
                "-fx-selection-bar: #39414d;" +
                "-fx-selection-bar-non-focused: #d4000e;" +
                "-fx-selection-bar-text: white;"
        );
        GridPane.setMargin(allApparatiList, new Insets(75, 30, 0, 25));
        allApparatiList.setCellFactory(listView -> //for selection of multiple list cells members
        {
            ListCell<String> cell = new ListCell<>()
            {
                protected void updateItem(String item, boolean empty)
                {
                    super.updateItem(item, empty);

                    if (empty || item == null)
                    {
                        setText(null);
                    }
                    else
                    {
                        setText(item);
                    }
                }
            };

            cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event ->
            {
                if (!cell.isEmpty())
                {
                    int index = cell.getIndex();

                    if (allApparatiList.getSelectionModel().isSelected(index))
                    {
                    	allApparatiList.getSelectionModel().clearSelection(index);
                    }
                    else
                    {
                    	allApparatiList.getSelectionModel().select(index);
                    }

                    event.consume();
                }
            });

            return cell;
        });
        
        
        
        
        
        dispatchSection.add(uitilzedAssets, 0, 3);
        dispatchSection.add(allApparatiList, 1, 3);
        
        //begin call lead section
		GridPane fireCommandGPaneSection = new GridPane();
		
        Text commandCallLeadPrompt = new Text("Command:");
        commandCallLeadPrompt.setFill(Styling.fireGradient());
        commandCallLeadPrompt.setFont(Font.font("Menlo",FontWeight.BOLD,18));
        GridPane.setMargin(commandCallLeadPrompt, new Insets(0, 15, 50, 0));
        
		ObservableList<String> selectableUsers = PopulateLists.allPersonell(); 
		ComboBox<String> commandComboBox = new ComboBox<>(selectableUsers);
		commandComboBox.getSelectionModel().clearSelection();
		commandComboBox.setPromptText("Select Call Command:");
		commandComboBox.setMaxWidth(300);
		commandComboBox.setCursor(Cursor.HAND);
        GridPane.setMargin(commandComboBox, new Insets(0, 15, 0, 10));
        commandComboBox.setStyle("-fx-font-family: 'Menlo';" +"-fx-font-weight: bold;" +"-fx-font-size: 14px;");
        
        Text additionalResponderPrompt = new Text("Additional Members:");
        additionalResponderPrompt.setFill(Styling.fireGradient());
        additionalResponderPrompt.setFont(Font.font("Menlo",FontWeight.BOLD,18));
        GridPane.setMargin(additionalResponderPrompt, new Insets(0, 15, 50, 25));
        
       
        ListView<String> allPersonellList =new ListView<>(selectableUsers);

        allPersonellList.setMinSize(300, 150);
        allPersonellList.setPrefSize(300, 150);
        allPersonellList.setMaxSize(300, 150);
        allPersonellList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        allPersonellList.setStyle(
                "-fx-selection-bar: #39414d;" +
                "-fx-selection-bar-non-focused: #d4000e;" +
                "-fx-selection-bar-text: white;"
        );
        GridPane.setMargin(allPersonellList, new Insets(75, 30, 0, 25));
        allPersonellList.setCellFactory(listView -> //for selection of multiple list cells members
        {
            ListCell<String> cell = new ListCell<>()
            {
                protected void updateItem(String item, boolean empty)
                {
                    super.updateItem(item, empty);

                    if (empty || item == null)
                    {
                        setText(null);
                    }
                    else
                    {
                        setText(item);
                    }
                }
            };

            cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event ->
            {
                if (!cell.isEmpty())
                {
                    int index = cell.getIndex();

                    if (allPersonellList.getSelectionModel().isSelected(index))
                    {
                    	allPersonellList.getSelectionModel().clearSelection(index);
                    }
                    else
                    {
                    	allPersonellList.getSelectionModel().select(index);
                    }

                    event.consume();
                }
            });

            return cell;
        });
        
        Text mutualAidPrompt = new Text("Mutual Aid:");
        mutualAidPrompt.setFill(Styling.fireGradient());
        mutualAidPrompt.setFont(Font.font("Menlo",FontWeight.BOLD,18));
        GridPane.setMargin(mutualAidPrompt, new Insets(0, 15, 25, 0));
        
		CheckBox mutualAidCB = new CheckBox("Mutual aid associated with this run?");
		mutualAidCB.setSelected(false);


    	ObservableList<String> selectableEMSMA = PopulateLists.fdListsService();
		ComboBox<String> mutualAidComboBox = new ComboBox<>(selectableEMSMA);
		mutualAidComboBox.getSelectionModel().clearSelection();
		mutualAidComboBox.setPromptText("Select Mutual Aid Department:");
		mutualAidComboBox.setPrefWidth(300);
		mutualAidComboBox.setCursor(Cursor.HAND);
        GridPane.setMargin(mutualAidComboBox, new Insets(0, 0, 0, 0));
        mutualAidComboBox.setStyle("-fx-font-family: 'Menlo';" +"-fx-font-weight: bold;" +"-fx-font-size: 14px;");
		mutualAidComboBox.disableProperty().bind(mutualAidCB.selectedProperty().not());
		
		
        Text pdRequestPrompt = new Text("Requested by PD:");
        pdRequestPrompt.setFill(Styling.fireGradient());
        pdRequestPrompt.setFont(Font.font("Menlo",FontWeight.BOLD,18));
        GridPane.setMargin(pdRequestPrompt, new Insets(0, 15, 15, 0));
        
		CheckBox pdCB = new CheckBox("Requested by PD?");
		pdCB.setSelected(false);
		
		
    	ObservableList<String> selectablePDs = PopulateLists.policeDepartmentsService();
		ComboBox<String> pdComboBox = new ComboBox<>(selectablePDs);
		pdComboBox.getSelectionModel().clearSelection();
		pdComboBox.setPromptText("Department:");
		pdComboBox.setMaxWidth(300);
		pdComboBox.setCursor(Cursor.HAND);
        GridPane.setMargin(pdComboBox, new Insets(15, 0, 0, 0));
        pdComboBox.setStyle("-fx-font-family: 'Menlo';" +"-fx-font-weight: bold;" +"-fx-font-size: 14px;");
        pdComboBox.disableProperty().bind(pdCB.selectedProperty().not());
        
        
        
        
        //column -> Row
        fireCommandGPaneSection.add(commandCallLeadPrompt, 0, 0);
        fireCommandGPaneSection.add(commandComboBox, 1, 0);
        fireCommandGPaneSection.add(additionalResponderPrompt, 2, 0);
        fireCommandGPaneSection.add(allPersonellList, 3, 0);
        fireCommandGPaneSection.add(mutualAidPrompt, 0, 1);
        fireCommandGPaneSection.add(mutualAidCB, 1, 1);
        fireCommandGPaneSection.add(mutualAidComboBox, 1, 2);
        fireCommandGPaneSection.add(pdRequestPrompt, 3, 2);
        fireCommandGPaneSection.add(pdCB, 3, 3);
        fireCommandGPaneSection.add(pdComboBox, 3, 4);
        
        //disposition Section
 		GridPane FireDispositionGP = new GridPane();
 		
         Text dispositionPrompt = new Text("Disposition:");
         dispositionPrompt.setFill(Styling.fireGradient());
         dispositionPrompt.setFont(Font.font("Menlo",FontWeight.BOLD,18));
         GridPane.setMargin(dispositionPrompt, new Insets(0, 15, 50, 0));
      

     	ObservableList<String> selectableDispositions = PopulateLists.fireDisposition();
 		ComboBox<String> dispositionComboBox = new ComboBox<>(selectableDispositions);
 		dispositionComboBox.getSelectionModel().clearSelection();
 		dispositionComboBox.setPromptText("Disposition:");
 		dispositionComboBox.setPrefWidth(225);
 		dispositionComboBox.setCursor(Cursor.HAND);
         GridPane.setMargin(dispositionComboBox, new Insets(0, 0, 0, 0));
         dispositionComboBox.setStyle("-fx-font-family: 'Menlo';" +"-fx-font-weight: bold;" +"-fx-font-size: 14px;");

        
        
         FireDispositionGP.add(dispositionPrompt, 0, 0);
         FireDispositionGP.add(dispositionComboBox, 1, 0);
		
		//Fire Submission area
 		VBox fireSubmitBox = new VBox();
 		fireSubmitBox.setAlignment(Pos.CENTER);
 		
 		Button fireSubmitButton = new Button("Submit");
 		fireSubmitBox.setMargin(fireSubmitButton, new Insets(0, 0, 20, 0));
 		Button goBackButton = new Button("Go Back");
 		fireSubmitBox.setMargin(goBackButton, new Insets(20, 0, 20, 0));
 		goBackButton.setOnMouseClicked(event -> 
		{
			 
			
				System.out.println(enteredUsername + " backed out of add Fire Event.");
				EventHub.eventHubInitializer(enteredUsername, userSecurityLevel, mainArea, navBar, root, scene);
		});
 		
 		
 		
 		fireSubmitBox.getChildren().add(fireSubmitButton);
 		fireSubmitBox.getChildren().add(goBackButton);
 		
 		
		
		
		
		
		callInformationArea.getChildren().add(fireHeaderText);
		callInformationArea.getChildren().add(dispatchSection);
		callInformationArea.getChildren().add(fireCommandGPaneSection);
		callInformationArea.getChildren().add(FireDispositionGP);
		callInformationArea.getChildren().add(fireSubmitBox);
		
		for (javafx.scene.Node node : fireSubmitBox.getChildren()) 
		{
			
		    if (node instanceof Button button) 
		    {
		        button.setPrefHeight(50);
		        button.setPrefWidth(300);
		        button.setFont(Font.font("Menlo", FontWeight.BOLD, 16));
		        button.setStyle("-fx-background-color: #ebeced;");
		        button.setMaxWidth(300);
		        button.setMaxHeight(50);
		        button.setOnMouseEntered(event -> {button.setStyle("-fx-background-color: #fa0724");});
		        button.setOnMouseExited(event -> {button.setStyle("-fx-background-color: #ebeced;");});
		        button.setOnMousePressed(event -> {button.setStyle("-fx-background-color: #a31c2c;");});
		        button.setOnMouseReleased(event ->{button.setStyle("-fx-background-color:  #fa0724;");});
		        button.setCursor(Cursor.HAND);
	    	}
		    
		}
		
		
	    ScrollPane callInformationScroll = new ScrollPane(callInformationArea);
	    callInformationScroll.setFitToWidth(true);

	    VBox.setVgrow(callInformationScroll,Priority.ALWAYS);
	    
	    mainArea.getChildren().add(callInformationScroll);
	}
	
}
