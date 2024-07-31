import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
public class GUI implements ActionListener {
    private static JFrame frame;
    private static JPanel panel;
    private static JLabel inputLabel, outputLabel, amountLabel, resultLabel, resultMessageLabel;
    private static JTextField amountTextField, resultTextField;
    private static JComboBox inputDropdown, outputDropdown;
    private static JButton convertButton;
    private static ArrayList<Currency> currencies;
    static {
        try {
            currencies = Converter.initialize();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static String[] currencyCodes;
    public static void main(String[] arguments) {
        frame = new JFrame("Currency Converter");
        panel = new JPanel();
        inputLabel = new JLabel("Input Currency: ");
        outputLabel = new JLabel("Output Currency: ");
        amountLabel = new JLabel("Amount: ");
        resultLabel = new JLabel("Result: ");
        resultMessageLabel = new JLabel();
        amountTextField = new JTextField();
        resultTextField = new JTextField();
        convertButton = new JButton("Convert");
        /*Configure the Frame and Panel*/
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.add(panel);
        frame.setVisible(true);
        panel.setLayout(null);
        /*Configure the Labels*/
        inputLabel.setBounds(10, 20, 160, 25);
        outputLabel.setBounds(10, 50, 160, 25);
        amountLabel.setBounds(10, 80, 160, 25);
        resultLabel.setBounds(10, 110, 160, 25);
        resultMessageLabel.setBounds(10, 190, 300, 25);
        panel.add(inputLabel);
        panel.add(outputLabel);
        panel.add(amountLabel);
        panel.add(resultLabel);
        panel.add(resultMessageLabel);
        /*Configure the Text Boxes*/
        amountTextField.setBounds(60, 80, 165, 25);
        resultTextField.setBounds(60, 110, 165, 25);
        resultTextField.setEditable(false);
        panel.add(amountTextField);
        panel.add(resultTextField);
        /*Set up the dropdowns*/
        currencyCodes = initializeCodes();
        inputDropdown = new JComboBox<>(currencyCodes);
        inputDropdown.setBounds(100, 20, 165, 25);
        outputDropdown = new JComboBox<>(currencyCodes);
        outputDropdown.setBounds(110, 50, 165, 25);
        panel.add(inputDropdown);
        panel.add(outputDropdown);
        /*Configure the Button*/
        convertButton.setBounds(145, 150, 80, 25);
        convertButton.addActionListener(new GUI());
        panel.add(convertButton);
    }
    @Override
    public void actionPerformed(ActionEvent e) { //convertButton
        double result = 0.0;
        String resultText = "";
        String input = inputDropdown.getSelectedItem().toString().toUpperCase();
        String output = outputDropdown.getSelectedItem().toString().toUpperCase();
        double amount = Double.parseDouble(amountTextField.getText());
        if(amount > 10000000){
            resultMessageLabel.setText("Amount must be less than 10000000");
        }
        try {
            result = Converter.convert(input, output, amount);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        if(result == -3.0){
            resultMessageLabel.setText("Amount has to be greater than 0!");
            resultTextField.setText(" ");
        }
        else if(result == -2.0){
            resultMessageLabel.setText("Currency has to be 3 characters long!");
            resultTextField.setText(" ");
        }
        else if(result == -1.0){
            resultMessageLabel.setText("Invalid currency!");
            resultTextField.setText(" ");
        }
        else{
            resultMessageLabel.setText("Success!");
            resultText = Double.toString(result);
            resultTextField.setText(resultText);
        }
    }
    public static String[] initializeCodes(){
        String[] codes = new String[currencies.size()];
        for(int i=0; i<currencies.size(); i++){
            codes[i] = currencies.get(i).id();
        }
        return codes;
    }
}
/*
https://stackoverflow.com/questions/20318748/read-in-double-from-java-jtextfield
https://www.youtube.com/watch?v=iE8tZ0hn2Ws
https://stackoverflow.com/questions/14186955/create-a-autocompleting-textbox-in-java-with-a-dropdown-list
https://stackoverflow.com/questions/3496532/retrieve-text-from-jcombobox
https://docs.oracle.com/javase/7/docs/api/javax/swing/JComboBox.html
 */