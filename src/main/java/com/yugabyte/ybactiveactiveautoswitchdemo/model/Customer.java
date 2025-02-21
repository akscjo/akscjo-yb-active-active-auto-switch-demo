package com.yugabyte.ybactiveactiveautoswitchdemo.model;

import java.util.List;
import java.util.UUID;

public class Customer {
    private UUID customerId;
    private String companyName;
    private String customerState;
    private List<Order> orders;

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCustomerState() {
        return customerState;
    }

    public void setCustomerState(String customerState) {
        this.customerState = customerState;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Customer {")
                .append("customerId=").append(customerId)
                .append(", companyName='").append(companyName).append('\'')
                .append(", customerState='").append(customerState).append('\'');

        if (orders != null && !orders.isEmpty()) {
            stringBuilder.append(", orders=[");
            for (int i = 0; i < orders.size(); i++) {
                if (i > 0) {
                    stringBuilder.append(", ");
                }
                Order order = orders.get(i);
                stringBuilder.append("Order{")
                        .append("orderId=").append(order.getOrderId())
                        .append(", orderDetails='").append(order.getOrderDetails()).append('\'')
                        .append(", productName='").append(order.getProductName()).append('\'')
                        .append(", orderQty=").append(order.getOrderQty())
                        .append('}');
            }
            stringBuilder.append("]");
        }

        stringBuilder.append('}');
        return stringBuilder.toString();
    }


}
