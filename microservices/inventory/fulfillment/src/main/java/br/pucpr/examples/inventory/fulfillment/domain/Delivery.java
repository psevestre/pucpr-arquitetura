package br.pucpr.examples.inventory.fulfillment.domain;

import br.pucpr.examples.inventory.fulfillment.domain.enumeration.DeliveryStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Delivery.
 */
@Entity
@Table(name = "delivery")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Delivery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "order_id", nullable = false)
    private String orderId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DeliveryStatus status;

    @NotNull
    @Size(max = 128)
    @Column(name = "customer_id", length = 128, nullable = false)
    private String customerId;

    @NotNull
    @Size(max = 128)
    @Column(name = "name", length = 128, nullable = false)
    private String name;

    @NotNull
    @Size(max = 128)
    @Column(name = "email", length = 128, nullable = false)
    private String email;

    @NotNull
    @Size(max = 128)
    @Column(name = "address_line_1", length = 128, nullable = false)
    private String addressLine1;

    @NotNull
    @Size(max = 128)
    @Column(name = "address_line_2", length = 128, nullable = false)
    private String addressLine2;

    @NotNull
    @Size(max = 64)
    @Column(name = "zip_code", length = 64, nullable = false)
    private String zipCode;

    @Size(max = 128)
    @Column(name = "city", length = 128)
    private String city;

    @Size(max = 64)
    @Column(name = "country", length = 64)
    private String country;

    @Size(max = 512)
    @Column(name = "delivery_instructions", length = 512)
    private String deliveryInstructions;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "delivery")
    @JsonIgnoreProperties(value = { "delivery" }, allowSetters = true)
    private Set<DeliveryItem> items = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Delivery id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return this.orderId;
    }

    public Delivery orderId(String orderId) {
        this.setOrderId(orderId);
        return this;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public DeliveryStatus getStatus() {
        return this.status;
    }

    public Delivery status(DeliveryStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public String getCustomerId() {
        return this.customerId;
    }

    public Delivery customerId(String customerId) {
        this.setCustomerId(customerId);
        return this;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return this.name;
    }

    public Delivery name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public Delivery email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddressLine1() {
        return this.addressLine1;
    }

    public Delivery addressLine1(String addressLine1) {
        this.setAddressLine1(addressLine1);
        return this;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return this.addressLine2;
    }

    public Delivery addressLine2(String addressLine2) {
        this.setAddressLine2(addressLine2);
        return this;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    public Delivery zipCode(String zipCode) {
        this.setZipCode(zipCode);
        return this;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return this.city;
    }

    public Delivery city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return this.country;
    }

    public Delivery country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDeliveryInstructions() {
        return this.deliveryInstructions;
    }

    public Delivery deliveryInstructions(String deliveryInstructions) {
        this.setDeliveryInstructions(deliveryInstructions);
        return this;
    }

    public void setDeliveryInstructions(String deliveryInstructions) {
        this.deliveryInstructions = deliveryInstructions;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Delivery createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Delivery updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<DeliveryItem> getItems() {
        return this.items;
    }

    public void setItems(Set<DeliveryItem> deliveryItems) {
        if (this.items != null) {
            this.items.forEach(i -> i.setDelivery(null));
        }
        if (deliveryItems != null) {
            deliveryItems.forEach(i -> i.setDelivery(this));
        }
        this.items = deliveryItems;
    }

    public Delivery items(Set<DeliveryItem> deliveryItems) {
        this.setItems(deliveryItems);
        return this;
    }

    public Delivery addItems(DeliveryItem deliveryItem) {
        this.items.add(deliveryItem);
        deliveryItem.setDelivery(this);
        return this;
    }

    public Delivery removeItems(DeliveryItem deliveryItem) {
        this.items.remove(deliveryItem);
        deliveryItem.setDelivery(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Delivery)) {
            return false;
        }
        return getId() != null && getId().equals(((Delivery) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Delivery{" +
            "id=" + getId() +
            ", orderId='" + getOrderId() + "'" +
            ", status='" + getStatus() + "'" +
            ", customerId='" + getCustomerId() + "'" +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", addressLine1='" + getAddressLine1() + "'" +
            ", addressLine2='" + getAddressLine2() + "'" +
            ", zipCode='" + getZipCode() + "'" +
            ", city='" + getCity() + "'" +
            ", country='" + getCountry() + "'" +
            ", deliveryInstructions='" + getDeliveryInstructions() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
