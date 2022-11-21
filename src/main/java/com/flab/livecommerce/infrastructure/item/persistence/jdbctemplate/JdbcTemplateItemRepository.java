package com.flab.livecommerce.infrastructure.item.persistence.jdbctemplate;

import com.flab.livecommerce.common.exception.EntityNotFoundException;
import com.flab.livecommerce.domain.image.ItemImage;
import com.flab.livecommerce.domain.item.Item;
import com.flab.livecommerce.domain.item.ItemOption;
import com.flab.livecommerce.domain.item.ItemOptionGroup;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class JdbcTemplateItemRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcTemplateItemRepository(DataSource dataSource) {
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("item") // item 테이블에 삽입
            .usingGeneratedKeyColumns("id"); // id 컬럼의 값을 key 로 반환
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public Item save(Item item) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(item);
        Long id = jdbcInsert.executeAndReturnKey(parameterSource).longValue();

        return item.setId(id);
    }

    public void deleteById(Long id) {
        SqlParameterSource param = new MapSqlParameterSource("id", id);
        String optionSql = "DELETE FROM item_option WHERE item_option.item_id = :id";
        jdbcTemplate.update(optionSql, param);
        String optionGroupSql = "DELETE FROM item_option_group WHERE item_option_group.item_id = :id";
        jdbcTemplate.update(optionGroupSql, param);
        String itemSql = "DELETE FROM item WHERE item.id = :id";
        jdbcTemplate.update(itemSql, param);
    }

    public Item update(Item item, Long id) {
        String sql = "UPDATE item "
            + "SET name=:name, description=:description, price=:price, sales_price=:salesPrice, stock_quantity=:stockQuantity "
            + "WHERE item.id=:id";
        SqlParameterSource param = new MapSqlParameterSource()
            .addValue("id", id)
            .addValue("name", item.getName())
            .addValue("description", item.getDescription())
            .addValue("price", item.getPrice())
            .addValue("salesPrice", item.getSalesPrice())
            .addValue("stockQuantity", item.getStockQuantity())
            .addValue("shopId", item.getShopId());

        jdbcTemplate.update(sql, param);
        log.info(String.valueOf(jdbcTemplate.update(sql, param)));
        return item.setId(id);
    }

    public Item findById(Long id) {
        String sql = "SELECT * FROM item i "
            + "JOIN item_option_group iog ON i.id = iog.item_id "
            + "JOIN item_option io ON iog.id = io.item_option_group_id "
            + "JOIN item_image im ON i.id = im.item_id "
            + "WHERE i.id = :id";

        Map<String, Object> param = Map.of("id", id);

        Item item = jdbcTemplate.query(sql, param, resultSetExtractor());

        if (item == null) {
            throw new EntityNotFoundException();
        }
        return item;
    }


    private ResultSetExtractor<Item> resultSetExtractor() {

        return (rs -> {
            Item item = null;
            ItemImage itemImage = null;
            ItemOptionGroup itemOptionGroup = null;
            Map<Long, ItemOptionGroup> itemOptionGroupMap = new HashMap<>();

            while (rs.next()) {
                if (item == null) {
                    item = new Item(
                        rs.getLong("shop_id"),
                        rs.getString("i.name"),
                        rs.getString("description"),
                        rs.getLong("price"),
                        rs.getLong("sales_price"),
                        rs.getInt("stock_quantity"));
                    item.setId(rs.getLong("id"));
                }

                long itemImageId = rs.getLong("im.id");
                itemImage = new ItemImage(
                    rs.getLong("item_id"),
                    rs.getInt("ordering"),
                    rs.getString("name"),
                    rs.getString("url")
                );
                itemImage.setId(itemImageId);
                item.addItemImage(itemImage);


                long itemOptionGroupId = rs.getLong("iog.id");
                if (!itemOptionGroupMap.containsKey(itemOptionGroupId)) {
                    itemOptionGroup = new ItemOptionGroup(
                        rs.getLong("item_id"),
                        rs.getString("iog.name"),
                        rs.getInt("ordering"),
                        rs.getBoolean("basic"),
                        rs.getBoolean("exclusive"),
                        rs.getInt("minimum_choice"),
                        rs.getInt("maximum_choice")
                    );
                    itemOptionGroup.setId(rs.getLong("iog.id"));
                    item.addItemOptionGroup(itemOptionGroup);

                    itemOptionGroupMap.put(itemOptionGroupId, itemOptionGroup);
                }

                ItemOption itemOption = new ItemOption(
                    rs.getLong("iog.id"),
                    rs.getString("io.name"),
                    rs.getInt("io.ordering"),
                    rs.getLong("io.price"),
                    rs.getLong("i.id")
                );

                itemOption.setId(rs.getLong("io.id"));
                itemOptionGroup.addItemOption(itemOption);
            }
            return item;
        });
    }
}
