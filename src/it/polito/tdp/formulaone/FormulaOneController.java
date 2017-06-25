package it.polito.tdp.formulaone;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.formulaone.model.Driver;
import it.polito.tdp.formulaone.model.Model;
import it.polito.tdp.formulaone.model.Season;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FormulaOneController {
	
	Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Season> boxAnno;

    @FXML
    private TextField textInputK;

    @FXML
    private TextArea txtResult;

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	Season s=boxAnno.getValue();
    	if(s==null){
    		txtResult.setText("Errore, selezionare un anno");
    		return;
    	}else{
    		model.creaGrafo(boxAnno.getValue());
    		txtResult.appendText("Il pilota migliore della stagione "+s.getYear()+" e' \n"+ model.getBestDriver().toString()+"\n");
    	}

    }

    @FXML
    void doTrovaDreamTeam(ActionEvent event) {
    	
    	try{
    	int k= Integer.parseInt(textInputK.getText());
    	//oppure uso try catch
    
    	txtResult.appendText("Il miglior team composto da "+k+" componenti e' :\n");
    	for(Driver d: model.trovaTeam(k)){
    		txtResult.appendText(d.toString()+"\n");
    		
    	}}catch(NumberFormatException e){
    		txtResult.setText("Inserire un numero");
    		return;
    	}

    	
    }

    @FXML
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'FormulaOne.fxml'.";
        assert textInputK != null : "fx:id=\"textInputK\" was not injected: check your FXML file 'FormulaOne.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'FormulaOne.fxml'.";

    }
    
    public void setModel(Model model){
    	this.model = model;
    	boxAnno.getItems().addAll(model.getSeason());
    }
}
