package AdressBook;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Optional;

class Contact {
    private String name;
    private String phoneNumber;
    private String email;
    private String address;

    public Contact(String name, String phoneNumber, String email, String address) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "\t\t\tName: " + name + "\n\t\t\tPhone: " + phoneNumber + "\n\t\t\tEmail: " + email + "\n\t\t\tAddress: " + address +"\t\t\t\t";
    }
}

interface ContactAction {
    void execute();
}

class AddContactAction implements ContactAction {
    private final ArrayList<Contact> contacts;
    private final TextField nameField;
    private final TextField phoneField;
    private final TextField emailField;
    private final TextField addressField;
    private final VBox contactListView;
    private final Application application;

    public AddContactAction(ArrayList<Contact> contacts, TextField nameField, TextField phoneField, TextField emailField, TextField addressField, VBox contactListView, Application application) {
        this.contacts = contacts;
        this.nameField = nameField;
        this.phoneField = phoneField;
        this.emailField = emailField;
        this.addressField = addressField;
        this.contactListView = contactListView;
        this.application = application;
    }

    @Override
    public void execute() {
        try {
            String name = nameField.getText();
            String phone = phoneField.getText();
            String email = emailField.getText();
            String address = addressField.getText();

            // Check for empty fields
            if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Empty Fields", "Please fill in all fields.");
                return;
            }

            // Check if the phone number has exactly 11 digits
            if (!phone.matches("\\d{11}")) {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid Phone Number", "The phone number must contain exactly 11 digits.");
                return;
            }

            Contact contact = new Contact(name, phone, email, address);
            contacts.add(contact);
            updateContactListView();
            clearFields();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Exception", "An error occurred: " + e.getMessage());
        }
    }

    private void updateContactListView() {
        contactListView.getChildren().clear();
        for (Contact contact : contacts) {
            Label contactLabel = new Label(contact.toString());

            // Add edit and remove buttons for each contact
            Button editButton = new Button("Edit");
            editButton.setOnAction(e -> new EditContactAction(contacts, contact, contactListView, application).execute());
            Button removeButton = new Button("Remove");
            removeButton.setOnAction(e -> new RemoveContactAction(contacts, contact, contactListView).execute());

            HBox hbox = new HBox(5);
            hbox.getChildren().addAll(contactLabel, editButton, removeButton);

            contactListView.getChildren().add(hbox);
        }
    }

    private void clearFields() {
        nameField.clear();
        phoneField.clear();
        emailField.clear();
        addressField.clear();
    }

    // Utility method to show an Alert dialog
    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}


class EditContactAction implements ContactAction {
    private final ArrayList<Contact> contacts;
    private final Contact contact;
    private final VBox contactListView;
    private final Application application;

    public EditContactAction(ArrayList<Contact> contacts, Contact contact, VBox contactListView, Application application) {
        this.contacts = contacts;
        this.contact = contact;
        this.contactListView = contactListView;
        this.application = application;
    }

    @Override
    public void execute() {
        Dialog<Contact> dialog = new Dialog<>();
        dialog.setTitle("Edit Contact");

        // Set the button types
        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        // Create and configure the name, phone, email, and address fields
        TextField editNameField = new TextField(contact.getName());
        TextField editPhoneField = new TextField(contact.getPhoneNumber());
        TextField editEmailField = new TextField(contact.getEmail());
        TextField editAddressField = new TextField(contact.getAddress());

        GridPane editGrid = new GridPane();
        editGrid.setHgap(10);
        editGrid.setVgap(10);
        editGrid.add(new Label("Name:"), 0, 0);
        editGrid.add(editNameField, 1, 0);
        editGrid.add(new Label("Phone:"), 0, 1);
        editGrid.add(editPhoneField, 1, 1);
        editGrid.add(new Label("Email:"), 0, 2);
        editGrid.add(editEmailField, 1, 2);
        editGrid.add(new Label("Address:"), 0, 3);
        editGrid.add(editAddressField, 1, 3);

        dialog.getDialogPane().setContent(editGrid);

        // Convert the result to a contact object when the update button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                contact.setName(editNameField.getText());
                contact.setPhoneNumber(editPhoneField.getText());
                contact.setEmail(editEmailField.getText());
                contact.setAddress(editAddressField.getText());
                updateContactListView();
                return contact;
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void updateContactListView() {
        contactListView.getChildren().clear();
        for (Contact contact : contacts) {
            Label contactLabel = new Label(contact.toString());

            // Add edit and remove buttons for each contact
            Button editButton = new Button("Edit");
            editButton.setOnAction(e -> new EditContactAction(contacts, contact, contactListView, application).execute());
            Button removeButton = new Button("Remove");
            removeButton.setOnAction(e -> new RemoveContactAction(contacts, contact, contactListView).execute());

            HBox hbox = new HBox(5);
            hbox.getChildren().addAll(contactLabel, editButton, removeButton);

            contactListView.getChildren().add(hbox);
        }
    }
}

class RemoveContactAction implements ContactAction {
    private final ArrayList<Contact> contacts;
    private final Contact contact;
    private final VBox contactListView;

    public RemoveContactAction(ArrayList<Contact> contacts, Contact contact, VBox contactListView) {
        this.contacts = contacts;
        this.contact = contact;
        this.contactListView = contactListView;
    }

    @Override
    public void execute() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Remove Contact");
        alert.setContentText("Are you sure you want to remove this contact?\n\n" + contact.toString());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            contacts.remove(contact);
            updateContactListView();
        }
    }

    private void updateContactListView() {
        contactListView.getChildren().clear();
        for (Contact contact : contacts) {
            Label contactLabel = new Label(contact.toString());

            // Add edit and remove buttons for each contact
            Button editButton = new Button("Edit");
            editButton.setOnAction(e -> new EditContactAction(contacts, contact, contactListView, null).execute());
            Button removeButton = new Button("Remove");
            removeButton.setOnAction(e -> new RemoveContactAction(contacts, contact, contactListView).execute());

            HBox hbox = new HBox(5);
            hbox.getChildren().addAll(contactLabel, editButton, removeButton);

            contactListView.getChildren().add(hbox);
        }
    }
}

class SearchContactAction implements ContactAction {
    private final ArrayList<Contact> contacts;

    public SearchContactAction(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }

    @Override
    public void execute() {
        try {
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Search by Phone Number");

            // Set the button types
            ButtonType searchButtonType = new ButtonType("Search", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(searchButtonType, ButtonType.CANCEL);

            // Create and configure the phone number field
            TextField searchPhoneField = new TextField();
            searchPhoneField.setPromptText("Enter phone number");

            GridPane searchGrid = new GridPane();
            searchGrid.setHgap(10);
            searchGrid.setVgap(10);
            searchGrid.add(new Label("Phone Number:"), 0, 0);
            searchGrid.add(searchPhoneField, 1, 0);

            dialog.getDialogPane().setContent(searchGrid);

            // Convert the result to a phone number when the search button is clicked
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == searchButtonType) {
                    return searchPhoneField.getText();
                }
                return null;
            });

            // Show the dialog and handle the result
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(this::displaySearchResult);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Exception", "An error occurred: " + e.getMessage());
        }
    }

    private void displaySearchResult(String phoneNumber) {
        boolean found = false;
        for (Contact contact : contacts) {
            if (contact.getPhoneNumber().equals(phoneNumber)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Search Result");
                alert.setHeaderText("Contact Found");
                alert.setContentText("Name: " + contact.getName() + "\nPhone: " + contact.getPhoneNumber() + "\nEmail: " + contact.getEmail() + "\nAddress: " + contact.getAddress());
                alert.showAndWait();
                found = true;
                break;
            }
        }
        if (!found) {
            showAlert(Alert.AlertType.INFORMATION, "Search Result", "Contact Not Found", "No contact found with the provided phone number.");
        }
    }

    // Utility method to show an Alert dialog
    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}

public class AddressBookGUI extends Application {
    private final ArrayList<Contact> contacts = new ArrayList<>();
    private TextField nameField;
    private TextField phoneField;
    private TextField emailField;
    private TextField addressField;
    private VBox contactListView;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Address Book");

        // Input fields
        Label nameLabel = new Label("Name:");
        nameField = new TextField();
        Label phoneLabel = new Label("Phone:");
        phoneField = new TextField();
        Label emailLabel = new Label("Email:");
        emailField = new TextField();
        Label addressLabel = new Label("Address:");
        addressField = new TextField();
        Button addButton = new Button("Add Contact");
        addButton.setOnAction(e -> new AddContactAction(contacts, nameField, phoneField, emailField, addressField, contactListView, this).execute());

        // Create a GridPane for input fields
        GridPane inputGrid = new GridPane();
        inputGrid.setPadding(new Insets(10));
        inputGrid.setVgap(8);
        inputGrid.setHgap(10);
        inputGrid.setAlignment(Pos.CENTER);

        // Add input fields to the grid
        inputGrid.addRow(0, nameLabel, nameField);
        inputGrid.addRow(1, phoneLabel, phoneField);
        inputGrid.addRow(2, emailLabel, emailField);
        inputGrid.addRow(3, addressLabel, addressField);
        inputGrid.add(addButton, 1, 4);

        // Contact list view
        contactListView = new VBox();
        contactListView.setSpacing(5);

        Button searchButton = new Button("Search by Phone");
        searchButton.setOnAction(e -> new SearchContactAction(contacts).execute());

        HBox searchBox = new HBox(10, searchButton);
        searchBox.setAlignment(Pos.BOTTOM_CENTER);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(inputGrid, contactListView, searchBox);

        Label madeByLabel = new Label("Made By: Syntax Squad");
        madeByLabel.setStyle("-fx-font-style: italic;");
        BorderPane.setAlignment(madeByLabel, Pos.BOTTOM_CENTER);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(layout);
        borderPane.setBottom(madeByLabel);

        Scene scene = new Scene(borderPane, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
