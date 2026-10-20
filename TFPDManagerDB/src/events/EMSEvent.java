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
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import universal.PopulateLists;
import universal.Styling;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.Priority;

public class EMSEvent 
{
	public static void emsEventInitalizer(String enteredUsername, int userSecurityLevel, VBox mainArea, VBox navBar, Group root, Scene scene) throws SQLException
	{
		mainArea.getChildren().clear();
		UserOptionsHub.setUpUserOptionVBox(mainArea, navBar, root, scene);
		
		
		
		//main VBox
	    VBox callInformationArea = new VBox();
	    callInformationArea.setSpacing(20);
	    callInformationArea.setPadding(new Insets(10));
	    
	    //EMS Event Header Text
		Text emsHeaderText = new Text("Add EMS Event");
		Styling.logoGradient();
		emsHeaderText.setFill(Styling.logoGradient());
		emsHeaderText.setFont(Font.font("Menlo",FontWeight.BOLD,24));
	
		//Dispatch Information VBox Section
		GridPane dispatchSection = new GridPane();
		//------------------------------------------------------row 1 -----------------------------------------

        Text runNumberLabel = new Text("Run Number:");
        runNumberLabel.setFill(Styling.logoGradient());
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
        noLeadingPrompt.setFill(Styling.logoGradient());
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
        datePickerLabel.setFill(Styling.logoGradient());
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
        timeCallLabel.setFill(Styling.logoGradient());
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
        hourExamplePrompt.setFill(Styling.logoGradient());
        hourExamplePrompt.setFont(Font.font("Menlo",FontWeight.BOLD,12));
        GridPane.setMargin(hourExamplePrompt, new Insets(0,25,0,0));
        
        
        
        dispatchSection.add(timeCallLabel, 0, 2);
        dispatchSection.add(hourTF, 1, 2);
        dispatchSection.add(hourExamplePrompt, 2, 2);
        
        
        //begin call lead section
		GridPane EMSCallLeadSectionGP = new GridPane();
		
        Text emsCallLeadPrompt = new Text("Call Lead:");
        emsCallLeadPrompt.setFill(Styling.logoGradient());
        emsCallLeadPrompt.setFont(Font.font("Menlo",FontWeight.BOLD,18));
        GridPane.setMargin(emsCallLeadPrompt, new Insets(0, 15, 50, 0));
        
		ObservableList<String> selectableUsers = PopulateLists.emsUsers();
		ComboBox<String> userComboBox = new ComboBox<>(selectableUsers);
		userComboBox.getSelectionModel().clearSelection();
		userComboBox.setPromptText("Select Call Lead:");
		userComboBox.setMaxWidth(300);
		userComboBox.setCursor(Cursor.HAND);
        GridPane.setMargin(userComboBox, new Insets(0, 15, 0, 10));
        userComboBox.setStyle("-fx-font-family: 'Menlo';" +"-fx-font-weight: bold;" +"-fx-font-size: 14px;");
        
        Text additionalEMSPrompt = new Text("Additional Members:");
        additionalEMSPrompt.setFill(Styling.logoGradient());
        additionalEMSPrompt.setFont(Font.font("Menlo",FontWeight.BOLD,18));
        GridPane.setMargin(additionalEMSPrompt, new Insets(0, 15, 50, 25));
        
        
        ObservableList<String> allPersonell =
                FXCollections.observableArrayList(PopulateLists.allPersonell());
        

        ListView<String> allPersonellList =new ListView<>(allPersonell);

        allPersonellList.setMinSize(300, 150);
        allPersonellList.setPrefSize(300, 150);
        allPersonellList.setMaxSize(300, 150);
        allPersonellList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        allPersonellList.setStyle(
                "-fx-selection-bar: #39414d;" +
                "-fx-selection-bar-non-focused: #22d4b0;" +
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
        mutualAidPrompt.setFill(Styling.logoGradient());
        mutualAidPrompt.setFont(Font.font("Menlo",FontWeight.BOLD,18));
        GridPane.setMargin(mutualAidPrompt, new Insets(0, 15, 25, 0));
        
		CheckBox mutualAidCB = new CheckBox("Mutual aid associated with this run?");
		mutualAidCB.setSelected(false);


    	ObservableList<String> selectableEMSMA = PopulateLists.populateEMSMAService();
		ComboBox<String> mutualAidComboBox = new ComboBox<>(selectableEMSMA);
		mutualAidComboBox.getSelectionModel().clearSelection();
		mutualAidComboBox.setPromptText("Select Mutual Aid Department:");
		mutualAidComboBox.setPrefWidth(300);
		mutualAidComboBox.setCursor(Cursor.HAND);
        GridPane.setMargin(mutualAidComboBox, new Insets(0, 0, 0, 0));
        mutualAidComboBox.setStyle("-fx-font-family: 'Menlo';" +"-fx-font-weight: bold;" +"-fx-font-size: 14px;");
		mutualAidComboBox.disableProperty().bind(mutualAidCB.selectedProperty().not());
		
		
        Text pdRequestPrompt = new Text("Requested by PD:");
        pdRequestPrompt.setFill(Styling.logoGradient());
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
        EMSCallLeadSectionGP.add(emsCallLeadPrompt, 0, 0);
        EMSCallLeadSectionGP.add(userComboBox, 1, 0);
        EMSCallLeadSectionGP.add(additionalEMSPrompt, 2, 0);
        EMSCallLeadSectionGP.add(allPersonellList, 3, 0);
        EMSCallLeadSectionGP.add(mutualAidPrompt, 0, 1);
        EMSCallLeadSectionGP.add(mutualAidCB, 1, 1);
        EMSCallLeadSectionGP.add(mutualAidComboBox, 1, 2);
        EMSCallLeadSectionGP.add(pdRequestPrompt, 3, 2);
        EMSCallLeadSectionGP.add(pdCB, 3, 3);
        EMSCallLeadSectionGP.add(pdComboBox, 3, 4);
        
        //disposition Section
		GridPane EMSDispositionGP = new GridPane();
		
        Text dispositionPrompt = new Text("Disposition:");
        dispositionPrompt.setFill(Styling.logoGradient());
        dispositionPrompt.setFont(Font.font("Menlo",FontWeight.BOLD,18));
        GridPane.setMargin(dispositionPrompt, new Insets(0, 15, 50, 0));
     

    	ObservableList<String> selectableDispositions = PopulateLists.selectDispositionsService();
		ComboBox<String> dispositionComboBox = new ComboBox<>(selectableDispositions);
		dispositionComboBox.getSelectionModel().clearSelection();
		dispositionComboBox.setPromptText("Disposition:");
		dispositionComboBox.setPrefWidth(225);
		dispositionComboBox.setCursor(Cursor.HAND);
        GridPane.setMargin(dispositionComboBox, new Insets(0, 0, 0, 0));
        dispositionComboBox.setStyle("-fx-font-family: 'Menlo';" +"-fx-font-weight: bold;" +"-fx-font-size: 14px;");


    	
        Text destinationPrompt = new Text("Destination:");
        destinationPrompt.setFill(Styling.logoGradient());
        destinationPrompt.setFont(Font.font("Menlo",FontWeight.BOLD,18));
        GridPane.setMargin(destinationPrompt, new Insets(0, 15, 50, 25));
        

    	ObservableList<String> selectableDestinations = PopulateLists.selectableDestinationsService();
		ComboBox<String> destinationComboBox = new ComboBox<>(selectableDestinations);
		destinationComboBox.getSelectionModel().clearSelection();
		destinationComboBox.setPromptText("Destination:");
		destinationComboBox.setPrefWidth(300);
		destinationComboBox.setCursor(Cursor.HAND);
        GridPane.setMargin(destinationComboBox, new Insets(0, 0, 0, 150));
        destinationComboBox.setStyle("-fx-font-family: 'Menlo';" +"-fx-font-weight: bold;" +"-fx-font-size: 14px;");
        destinationComboBox.disableProperty().bind(dispositionComboBox.valueProperty().isNotEqualTo("Treat/Transport"));
		
        
        EMSDispositionGP.add(dispositionPrompt, 0, 0);
        EMSDispositionGP.add(dispositionComboBox, 1, 0);
        EMSDispositionGP.add(destinationPrompt, 2, 0);
        EMSDispositionGP.add(destinationComboBox, 2, 0);

        
		//Submit button VBox Area
		VBox emsSubmitVBox = new VBox();
		emsSubmitVBox.setAlignment(Pos.CENTER);
		
		
		Button emsSubmitButton = new Button("Submit"); 
		emsSubmitVBox.setMargin(emsSubmitButton, new Insets(0, 0, 20, 0));
 		
		Button goBackButton = new Button("Go Back");
		emsSubmitVBox.setMargin(goBackButton, new Insets(20, 0, 20, 0));

 		goBackButton.setOnMouseClicked(event -> 
		{
			 
			
				System.out.println(enteredUsername + " backed out of add EMS Event.");
				EventHub.eventHubInitializer(enteredUsername, userSecurityLevel, mainArea, navBar, root, scene);
		});
 		
		emsSubmitVBox.getChildren().add(emsSubmitButton);
		emsSubmitVBox.getChildren().add(goBackButton);
		
		//add everything to main VBox Here
		callInformationArea.getChildren().add(emsHeaderText);
		callInformationArea.getChildren().add(dispatchSection);
		callInformationArea.getChildren().add(EMSCallLeadSectionGP);
		callInformationArea.getChildren().add(EMSDispositionGP);
		callInformationArea.getChildren().add(emsSubmitVBox);
		
		
		for (javafx.scene.Node node : emsSubmitVBox.getChildren()) 
		{
			
		    if (node instanceof Button button) 
		    {
		        button.setPrefHeight(50);
		        button.setPrefWidth(300);
		        button.setFont(Font.font("Menlo", FontWeight.BOLD, 16));
		        button.setStyle("-fx-background-color: #ebeced;");
		        button.setMaxWidth(300);
		        button.setMaxHeight(50);
		        button.setOnMouseEntered(event -> {button.setStyle("-fx-background-color: #4699f2");});
		        button.setOnMouseExited(event -> {button.setStyle("-fx-background-color: #ebeced;");});
		        button.setOnMousePressed(event -> {button.setStyle("-fx-background-color: #316dad;");});
		        button.setOnMouseReleased(event ->{button.setStyle("-fx-background-color:  #4699f2;");});
		        button.setCursor(Cursor.HAND);
	    	}
		    
		}
		
	    ScrollPane callInformationScroll = new ScrollPane(callInformationArea);
	    callInformationScroll.setFitToWidth(true);

	    VBox.setVgrow(callInformationScroll,Priority.ALWAYS);
	    
	    mainArea.getChildren().add(callInformationScroll);
		
		

	}
	

	
}
