package pkgApp.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.poi.ss.formula.functions.FinanceLib;

import com.sun.prism.paint.Color;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.text.FontWeight;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

import javafx.beans.value.*;

import pkgApp.RetirementApp;
import pkgCore.Retirement;

public class RetirementController implements Initializable {

	private RetirementApp mainApp = null;
	@FXML
	private TextField txtSaveEachMonth;
	@FXML
	private TextField txtYearsToWork;
	@FXML
	private TextField txtAnnualReturnWorking;
	@FXML
	private TextField txtWhatYouNeedToSave;
	@FXML
	private TextField txtYearsRetired;
	@FXML
	private TextField txtAnnualReturnRetired;
	@FXML
	private TextField txtRequiredIncome;
	@FXML
	private TextField txtMonthlySSI;

	private HashMap<TextField, String> hmTextFieldRegEx = new HashMap<TextField, String>();

	public RetirementApp getMainApp() {
		return mainApp;
		
	}

	public void setMainApp(RetirementApp mainApp) {
		this.mainApp = mainApp;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// Adding an entry in the hashmap for each TextField control I want to validate
		// with a regular expression
		// "\\d*?" - means any decimal number
		// "\\d*(\\.\\d*)?" means any decimal, then optionally a period (.), then
		// decmial
		hmTextFieldRegEx.put(txtYearsToWork, "[0-9]|1[0-9]|2[0-9]|3[0-9]|4[0]\\d*?");
		hmTextFieldRegEx.put(txtAnnualReturnWorking, "(\\d(?:\\.[0-9])?|10)(?!\\d)");
		hmTextFieldRegEx.put(txtYearsRetired, "[0-9]|1[0-9]|2[0]\\d*?");
		hmTextFieldRegEx.put(txtAnnualReturnRetired, "(\\d(?:\\.[0-9])?|10)(?!\\d)");
		hmTextFieldRegEx.put(txtRequiredIncome, "264[2-9]|26[5-9][0-9]|2[7-9][0-9][0-9]|[3-9][0-9][0-9][0-9]|10000\\d*?");
		hmTextFieldRegEx.put(txtMonthlySSI, "[0-9]|[0-9][0-9]|[0-9][0-9][0-9]|1[0-9][0-9][0-9]|2[0-5][0-9][0-9]|26[0-3][0-9]|264[0-2]\\d*?");

		// Check out these pages (how to validate controls):
		// https://stackoverflow.com/questions/30935279/javafx-input-validation-textfield
		// https://stackoverflow.com/questions/40485521/javafx-textfield-validation-decimal-value?rq=1
		// https://stackoverflow.com/questions/8381374/how-to-implement-a-numberfield-in-javafx-2-0
		// There are some examples on how to validate / check format

		Iterator it = hmTextFieldRegEx.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			TextField txtField = (TextField) pair.getKey();
			String strRegEx = (String) pair.getValue();

			txtField.focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
						Boolean newPropertyValue) {
					// If newPropertyValue = true, then the field HAS FOCUS
					// If newPropertyValue = false, then field HAS LOST FOCUS
					if (!newPropertyValue) {
						if (!txtField.getText().matches(strRegEx)) {
							txtField.setText("");
							txtField.requestFocus();
						}
					}
				}
			});
		}

	}

	@FXML
	public void btnClear(ActionEvent event) {
		System.out.println("Clear pressed");

		// disable read-only controls
		txtSaveEachMonth.setDisable(true);
		txtWhatYouNeedToSave.setDisable(true);
		
		txtSaveEachMonth.clear();
		txtWhatYouNeedToSave.clear();

		// Clear, enable txtYearsToWork
		txtYearsToWork.clear();
		txtYearsToWork.setDisable(false);
		
		txtAnnualReturnWorking.clear();
		txtAnnualReturnWorking.setDisable(false);
		
		txtYearsRetired.clear();
		txtYearsRetired.setDisable(false);
		
		txtAnnualReturnRetired.clear();
		txtAnnualReturnRetired.setDisable(false);

		txtRequiredIncome.clear();
		txtRequiredIncome.setDisable(false);
		
		txtMonthlySSI.clear();
		txtMonthlySSI.setDisable(false);
	}

	@FXML
	public void btnCalculate() {

		System.out.println("calculating");

		txtSaveEachMonth.setDisable(false);
		txtWhatYouNeedToSave.setDisable(false);
		Retirement calculation = new Retirement(Integer.parseInt(txtYearsToWork.getCharacters().toString()), 
				Double.parseDouble(txtAnnualReturnWorking.getCharacters().toString()),
				Integer.parseInt(txtYearsRetired.getCharacters().toString()),
				Double.parseDouble(txtAnnualReturnRetired.getCharacters().toString()),
				Integer.parseInt(txtRequiredIncome.getCharacters().toString()),
				Integer.parseInt(txtMonthlySSI.getCharacters().toString()));
		
		txtWhatYouNeedToSave.setText(Double.toString(calculation.TotalAmountToSave()));
		txtSaveEachMonth.setText(Double.toString(calculation.MonthlySavings()));
	}
}
