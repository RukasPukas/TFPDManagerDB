package events;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import universal.PopulateLists;
import universal.Styling;

public class EMSCallLeadSection {
	
	public static GridPane EMSCallLeadSection() throws SQLException
	{
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



		
		return EMSCallLeadSectionGP;
		
	}
	
	

	

	

}
