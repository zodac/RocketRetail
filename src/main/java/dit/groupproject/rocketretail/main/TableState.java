package dit.groupproject.rocketretail.main;

public enum TableState {
    NONE(""), HOMESCREEN("HomeScreen"), CUSTOMER("Customer"), ORDER("Order"), PRODUCT("Product"), STAFF("Staff"), SUPPLIER("Supplier");

    final String title;

    private TableState(final String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
