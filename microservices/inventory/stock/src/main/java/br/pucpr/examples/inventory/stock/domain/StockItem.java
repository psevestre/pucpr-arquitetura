package br.pucpr.examples.inventory.stock.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A StockItem.
 */
@Entity
@Table(name = "stock_item")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StockItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 128)
    @Column(name = "sku", length = 128, nullable = false)
    private String sku;

    @NotNull
    @Size(max = 256)
    @Column(name = "description", length = 256, nullable = false)
    private String description;

    @NotNull
    @Column(name = "available", nullable = false)
    private Integer available;

    @NotNull
    @Min(value = 0)
    @Column(name = "reserved", nullable = false)
    private Integer reserved;

    @NotNull
    @Min(value = 0)
    @Column(name = "min_stock", nullable = false)
    private Integer minStock;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StockItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSku() {
        return this.sku;
    }

    public StockItem sku(String sku) {
        this.setSku(sku);
        return this;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDescription() {
        return this.description;
    }

    public StockItem description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAvailable() {
        return this.available;
    }

    public StockItem available(Integer available) {
        this.setAvailable(available);
        return this;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public Integer getReserved() {
        return this.reserved;
    }

    public StockItem reserved(Integer reserved) {
        this.setReserved(reserved);
        return this;
    }

    public void setReserved(Integer reserved) {
        this.reserved = reserved;
    }

    public Integer getMinStock() {
        return this.minStock;
    }

    public StockItem minStock(Integer minStock) {
        this.setMinStock(minStock);
        return this;
    }

    public void setMinStock(Integer minStock) {
        this.minStock = minStock;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StockItem)) {
            return false;
        }
        return getId() != null && getId().equals(((StockItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StockItem{" +
            "id=" + getId() +
            ", sku='" + getSku() + "'" +
            ", description='" + getDescription() + "'" +
            ", available=" + getAvailable() +
            ", reserved=" + getReserved() +
            ", minStock=" + getMinStock() +
            "}";
    }
}
