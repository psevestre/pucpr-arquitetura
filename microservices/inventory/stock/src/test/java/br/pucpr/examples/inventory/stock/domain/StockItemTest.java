package br.pucpr.examples.inventory.stock.domain;

import static br.pucpr.examples.inventory.stock.domain.StockItemTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.pucpr.examples.inventory.stock.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StockItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockItem.class);
        StockItem stockItem1 = getStockItemSample1();
        StockItem stockItem2 = new StockItem();
        assertThat(stockItem1).isNotEqualTo(stockItem2);

        stockItem2.setId(stockItem1.getId());
        assertThat(stockItem1).isEqualTo(stockItem2);

        stockItem2 = getStockItemSample2();
        assertThat(stockItem1).isNotEqualTo(stockItem2);
    }
}
