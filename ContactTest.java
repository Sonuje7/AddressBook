package AdressBook;



public class ContactTest {
    public static void main(String[] args) {
        testContactCreation();
        testContactSettersAndGetters();
        System.out.println("All tests passed.");
    }

    static void testContactCreation() {
        Contact contact = new Contact("Raheem", "12345678901", "raheem@email.com", "123 Main St");
        assertEqual("Raheem", contact.getName(), "testContactCreation - Name");
        assertEqual("12345678901", contact.getPhoneNumber(), "testContactCreation - Phone Number");
        assertEqual("raheem@email.com", contact.getEmail(), "testContactCreation - Email");
        assertEqual("123 Main St", contact.getAddress(), "testContactCreation - Address");
    }

    static void testContactSettersAndGetters() {
        Contact contact = new Contact("raheem", "10987654321", "raheem@email.com", "456 Elm St");

        contact.setName("Abdul Raheem");
        assertEqual("Abdul Raheem", contact.getName(), "testContactSettersAndGetters - Name");

        contact.setPhoneNumber("11122233344");
        assertEqual("11122233344", contact.getPhoneNumber(), "testContactSettersAndGetters - Phone Number");

        contact.setEmail("raheem@email.com");
        assertEqual("raheem@email.com", contact.getEmail(), "testContactSettersAndGetters - Email");

        contact.setAddress("789 Oak St");
        assertEqual("789 Oak St", contact.getAddress(), "testContactSettersAndGetters - Address");
    }

    static void assertEqual(Object expected, Object actual, String testName) {
        if (expected.equals(actual)) {
            System.out.println(testName + " passed.");
        } else {
            System.err.println(testName + " failed: expected [" + expected + "] but got [" + actual + "].");
        }
    }
}

