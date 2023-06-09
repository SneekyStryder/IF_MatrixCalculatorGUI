import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MatrixCalculatorGUI extends JFrame {
    private JTextField[][] matrix1Fields;
    private JTextField[][] matrix2Fields;
    private JTextField[][] resultFields;

    public MatrixCalculatorGUI() {
        setTitle("Matrix Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = createInputPanel();
        JPanel buttonPanel = createButtonPanel();
        JPanel resultPanel = createResultPanel();

        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(resultPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridLayout(1, 2));
        JPanel matrix1Panel = new JPanel(new GridLayout(3, 3));
        JPanel matrix2Panel = new JPanel(new GridLayout(3, 3));

        JLabel matrix1Label = new JLabel("Matrix 1");
        JLabel matrix2Label = new JLabel("Matrix 2");

        matrix1Fields = new JTextField[3][3];
        matrix2Fields = new JTextField[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                matrix1Fields[i][j] = new JTextField();
                matrix2Fields[i][j] = new JTextField();

                matrix1Panel.add(matrix1Fields[i][j]);
                matrix2Panel.add(matrix2Fields[i][j]);
            }
        }

        inputPanel.add(matrix1Panel);
        inputPanel.add(matrix2Panel);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(matrix1Label, BorderLayout.WEST);
        panel.add(matrix2Label, BorderLayout.EAST);
        panel.add(inputPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton multiplyButton = new JButton("Multiply");
        JButton addButton = new JButton("Add");
        JButton rowEchelonFormButton = new JButton("Row Echelon Form");

        multiplyButton.addActionListener(new MatrixOperationListener(MatrixOperation.MULTIPLY));
        addButton.addActionListener(new MatrixOperationListener(MatrixOperation.ADD));
        rowEchelonFormButton.addActionListener(new MatrixOperationListener(MatrixOperation.ROW_ECHELON_FORM));

        buttonPanel.add(multiplyButton);
        buttonPanel.add(addButton);
        buttonPanel.add(rowEchelonFormButton);

        return buttonPanel;
    }

    private JPanel createResultPanel() {
        JPanel resultPanel = new JPanel(new GridLayout(1, 1));
        JPanel matrixPanel = new JPanel(new GridLayout(3, 3));

        JLabel resultLabel = new JLabel("Result");
        resultFields = new JTextField[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                resultFields[i][j] = new JTextField();
                resultFields[i][j].setEditable(false);
                matrixPanel.add(resultFields[i][j]);
            }
        }

        resultPanel.add(resultLabel);
        resultPanel.add(matrixPanel);

        return resultPanel;
    }

    private int[][] getMatrix1() {
        return getMatrixFromFields(matrix1Fields);
    }

    private int[][] getMatrix2() {
        return getMatrixFromFields(matrix2Fields);
    }

    private int[][] getMatrixFromFields(JTextField[][] fields) {
        int[][] matrix = new int[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                try {
                    matrix[i][j] = Integer.parseInt(fields[i][j].getText());
                } catch (NumberFormatException e) {
                    matrix[i][j] = 0;
                }
            }
        }

        return matrix;
    }

    private void setMatrixToFields(int[][] matrix, JTextField[][] fields) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                fields[i][j].setText(String.valueOf(matrix[i][j]));
            }
        }
    }

    private int[][] multiplyMatrices(int[][] matrix1, int[][] matrix2) {
        int[][] product = new int[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    product[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }

        return product;
    }

    private int[][] addMatrices(int[][] matrix1, int[][] matrix2) {
        int[][] sum = new int[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                sum[i][j] = matrix1[i][j] + matrix2[i][j];
            }
        }

        return sum;
    }

    private int[][] convertToRowEchelonForm(int[][] matrix) {
        int[][] rowEchelonForm = new int[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(matrix[i], 0, rowEchelonForm[i], 0, 3);
        }

        for (int i = 0; i < 3; i++) {
            int pivot = rowEchelonForm[i][i];
            if (pivot == 0) {
                int j = i + 1;
                while (j < 3 && rowEchelonForm[j][i] == 0) {
                    j++;
                }
                if (j == 3) {
                    continue;
                }
                int[] temp = rowEchelonForm[i];
                rowEchelonForm[i] = rowEchelonForm[j];
                rowEchelonForm[j] = temp;
                pivot = rowEchelonForm[i][i];
            }

            for (int j = i; j < 3; j++) {
                rowEchelonForm[i][j] /= pivot;
            }

            for (int j = i + 1; j < 3; j++) {
                int factor = rowEchelonForm[j][i];
                for (int k = i; k < 3; k++) {
                    rowEchelonForm[j][k] -= factor * rowEchelonForm[i][k];
                }
            }
        }

        return rowEchelonForm;
    }

    private enum MatrixOperation {
        MULTIPLY,
        ADD,
        ROW_ECHELON_FORM
    }

    private class MatrixOperationListener implements ActionListener {
        private MatrixOperation operation;

        public MatrixOperationListener(MatrixOperation operation) {
            this.operation = operation;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int[][] matrix1 = getMatrix1();
            int[][] matrix2 = getMatrix2();
            int[][] result;

            switch (operation) {
                case MULTIPLY:
                    result = multiplyMatrices(matrix1, matrix2);
                    break;
                case ADD:
                    result = addMatrices(matrix1, matrix2);
                    break;
                case ROW_ECHELON_FORM:
                    result = convertToRowEchelonForm(matrix1);
                    break;
                default:
                    result = new int[3][3];
            }

            setMatrixToFields(result, resultFields);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MatrixCalculatorGUI();
            }
        });
    }
}
