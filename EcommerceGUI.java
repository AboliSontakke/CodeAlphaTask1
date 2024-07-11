import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EcommerceGUI extends JFrame {
   
	private static final long serialVersionUID = 1L;
	private JTextField productIdField, productNameField, priceField;
    private JButton addButton, updateButton, deleteButton, viewButton;
    private JTextArea displayArea;
    private Connection connection;

    public EcommerceGUI() {
        setTitle("Product Management System");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.add(new JLabel("Product ID:"));
        productIdField = new JTextField();
        inputPanel.add(productIdField);
        inputPanel.add(new JLabel("Product Name:"));
        productNameField = new JTextField();
        inputPanel.add(productNameField);
        inputPanel.add(new JLabel("Price:"));
        priceField = new JTextField();
        inputPanel.add(priceField);

        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        viewButton = new JButton("View All");
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);

        displayArea = new JTextArea(10, 30);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProduct();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteProduct();
            }
        });

        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayProducts();
            }
        });

        try {
            connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "system");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setVisible(true);
    }

    private void addProduct() {
        String query = "INSERT INTO products (product_id, product_name, price) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            int productId = Integer.parseInt(productIdField.getText());
            String productName = productNameField.getText();
            double price = Double.parseDouble(priceField.getText());
            statement.setInt(1, productId);
            statement.setString(2, productName);
            statement.setDouble(3, price);
            statement.executeUpdate();
            displayArea.setText("Product added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            displayArea.setText("Error adding product.");
        }
    }

    private void updateProduct() {
        String query = "UPDATE products SET product_name=?, price=? WHERE product_id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            int productId = Integer.parseInt(productIdField.getText());
            String productName = productNameField.getText();
            double price = Double.parseDouble(priceField.getText());
            statement.setString(1, productName);
            statement.setDouble(2, price);
            statement.setInt(3, productId);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                displayArea.setText("Product updated successfully.");
            } else {
                displayArea.setText("No product found with that ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            displayArea.setText("Error updating product.");
        }
    }

    private void deleteProduct() {
        String query = "DELETE FROM products WHERE product_id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            int productId = Integer.parseInt(productIdField.getText());
            statement.setInt(1, productId);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                displayArea.setText("Product deleted successfully.");
            } else {
                displayArea.setText("No product found with that ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            displayArea.setText("Error deleting product.");
        }
    }

    private void displayProducts() {
        StringBuilder result = new StringBuilder();
        String query = "SELECT * FROM products";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                String productName = resultSet.getString("product_name");
                double price = resultSet.getDouble("price");
                result.append("Product ID: ").append(productId).append(", Product Name: ").append(productName)
                        .append(", Price: ").append(price).append("\n");
            }
            displayArea.setText(result.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            displayArea.setText("Error fetching products.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new EcommerceGUI();
            }
        });
    }
}
