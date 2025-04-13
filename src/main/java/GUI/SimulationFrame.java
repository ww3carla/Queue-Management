package GUI;

import BusinessLogic.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimulationFrame extends JFrame {
    private JTextField timeLimitField, numberOfClientsField, getNumberOfServersField;
    private JTextField minArrivalField, maxArrivalField, minServiceTimeField, maxServiceTimeField;
    private JComboBox<String> strategyBox;
    private JTextArea outputArea;
    private JButton startButton;

    public SimulationFrame() {
        setTitle("Queue Simulation");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(9, 2, 10, 10));

        timeLimitField = new JTextField();
        numberOfClientsField = new JTextField();
        getNumberOfServersField = new JTextField();
        minArrivalField = new JTextField();
        maxArrivalField = new JTextField();
        minServiceTimeField = new JTextField();
        maxServiceTimeField = new JTextField();

        strategyBox = new JComboBox<>(new String[]{"SHORTEST_QUEUE", "SHORTEST_TIME"});

        inputPanel.add(new JLabel("Time Limit:"));
        inputPanel.add(timeLimitField);
        inputPanel.add(new JLabel("Number of Clients:"));
        inputPanel.add(numberOfClientsField);
        inputPanel.add(new JLabel("Number of Servers:"));
        inputPanel.add(getNumberOfServersField);
        inputPanel.add(new JLabel("Min Arrival Time:"));
        inputPanel.add(minArrivalField);
        inputPanel.add(new JLabel("Max Arrival Time:"));
        inputPanel.add(maxArrivalField);
        inputPanel.add(new JLabel("Min Service Time:"));
        inputPanel.add(minServiceTimeField);
        inputPanel.add(new JLabel("Max Service Time:"));
        inputPanel.add(maxServiceTimeField);
        inputPanel.add(new JLabel("Strategy:"));
        inputPanel.add(strategyBox);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane outputScrollPane = new JScrollPane(outputArea);

        startButton = new JButton("Start Simulation");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    int timeLimit = Integer.parseInt(timeLimitField.getText());
                    int clients = Integer.parseInt(numberOfClientsField.getText());
                    int servers = Integer.parseInt(getNumberOfServersField.getText());
                    int minArr = Integer.parseInt(minArrivalField.getText());
                    int maxArr = Integer.parseInt(maxArrivalField.getText());
                    int minSerTime = Integer.parseInt(minServiceTimeField.getText());
                    int maxSerTime = Integer.parseInt(maxServiceTimeField.getText());
                    String strategy = (String) strategyBox.getSelectedItem();
                    SelectionPolicy policy = strategy.equals("SHORTES_QUEUE") ? SelectionPolicy.SHORTEST_QUEUE : SelectionPolicy.SHORTEST_TIME;

                    outputArea.setText("");

                    SimulationManager manager = new SimulationManager();
                    manager.setFrame(SimulationFrame.this);
                    manager.setParams(timeLimit, minArr, maxArr, minSerTime, maxSerTime, servers, clients, policy);
                    Thread thread = new Thread(manager);
                    thread.start();
                }catch(NumberFormatException ex){
                    JOptionPane.showMessageDialog(null, ex, "Please enter valid inputs", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(inputPanel, BorderLayout.NORTH);
        add(outputScrollPane, BorderLayout.CENTER);
        add(startButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    public JTextField getTimeLimitField() {
        return timeLimitField;
    }

    public void setTimeLimitField(JTextField timeLimitField) {
        this.timeLimitField = timeLimitField;
    }

    public JTextField getNumberOfClientsField() {
        return numberOfClientsField;
    }

    public void setNumberOfClientsField(JTextField numberOfClientsField) {
        this.numberOfClientsField = numberOfClientsField;
    }

    public JTextField getGetNumberOfServersField() {
        return getNumberOfServersField;
    }

    public void setGetNumberOfServersField(JTextField getNumberOfServersField) {
        this.getNumberOfServersField = getNumberOfServersField;
    }

    public JTextField getMinArrivalField() {
        return minArrivalField;
    }

    public void setMinArrivalField(JTextField minArrivalField) {
        this.minArrivalField = minArrivalField;
    }

    public JTextField getMaxArrivalField() {
        return maxArrivalField;
    }

    public void setMaxArrivalField(JTextField maxArrivalField) {
        this.maxArrivalField = maxArrivalField;
    }

    public JTextField getMinServiceTimeField() {
        return minServiceTimeField;
    }

    public void setMinServiceTimeField(JTextField minServiceTimeField) {
        this.minServiceTimeField = minServiceTimeField;
    }

    public JTextField getMaxServiceTimeField() {
        return maxServiceTimeField;
    }

    public void setMaxServiceTimeField(JTextField maxServiceTimeField) {
        this.maxServiceTimeField = maxServiceTimeField;
    }

    public JComboBox<String> getStrategyBox() {
        return strategyBox;
    }

    public void setStrategyBox(JComboBox<String> strategyBox) {
        this.strategyBox = strategyBox;
    }

    public JButton getStartButton() {
        return startButton;
    }

    public void setStartButton(JButton startButton) {
        this.startButton = startButton;
    }

    public JTextArea getOutputArea() {
        return outputArea;
    }

    public void setOutputArea(JTextArea outputArea) {
        this.outputArea = outputArea;
    }

    public void appendToOutput(String output){
        SwingUtilities.invokeLater(() -> {
            outputArea.append(output + "\n");
        });
    }

    public static void main(String[] args) {
        SimulationFrame frame = new SimulationFrame();
    }
}
