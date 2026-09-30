package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PopulatePositionService 
{
	public static ObservableList<String> poopulatePositionsList(int userSecurityLevel, ObservableList<String> positionOptions)
	{
		
		if(userSecurityLevel == 3)
		{
			
			positionOptions = FXCollections.observableArrayList
		        (
		                "Part-Time EMS",
		                "POC-Fire",
		                "Full-Time"

		                
		        );
		}
		
		if(userSecurityLevel == 4)
		{
			
			positionOptions = FXCollections.observableArrayList
		        (
		                "Part-Time EMS",
		                "POC-Fire",
		                "Full-Time",
		                "POC-Fire Lieutenant",
		                "Full-Time Lieutenant"

		        );
		}
		if(userSecurityLevel == 5)
		{
			
			positionOptions = FXCollections.observableArrayList
		        (
		                "Part-Time EMS",
		                "POC-Fire",
		                "Full-Time",
		                "POC-Fire Lieutenant",
		                "Full-Time Lieutenant",
		                "POC-Fire Captain",
		                "Full-Time Captain"
		        );
		}
		if(userSecurityLevel == 6)
		{
			
			positionOptions = FXCollections.observableArrayList
		        (
		                "Part-Time EMS",
		                "POC-Fire",
		                "Full-Time",
		                "POC-Fire Lieutenant",
		                "Full-Time Lieutenant",
		                "POC-Fire Captain",
		                "Full-Time Captain",
		                "Assistant Chief"
		        );
		}
			if(userSecurityLevel == 7)
			{
				
				positionOptions = FXCollections.observableArrayList
			        (
		                "Part-Time EMS",
		                "POC-Fire",
		                "Full-Time",
		                "POC-Fire Lieutenant",
		                "Full-Time Lieutenant",
		                "POC-Fire Captain",
		                "Full-Time Captain",
		                "Assistant Chief",
		                "Deputy Chief"
			        );
		}
			if(userSecurityLevel > 7)
			{
				positionOptions = FXCollections.observableArrayList
				(
				        "Part-Time EMS",
				        "POC-Fire",
				        "POC-Fire Lieutenant",
				        "POC-Fire Captain",
				        "Full-Time",
				        "Full-Time Lieutenant",
				        "Full-Time Captain",
				        "Assistant Chief",
				        "Deputy Chief",
				        "Chief"
				);
			}
		
		
		
		
		return positionOptions;

	}
}
