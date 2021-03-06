package com.example.igor.projetopoo.entities;

import android.util.Pair;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Product extends Entity implements Serializable {
    private Number averagePrice;
    private Pair<Number, Number> priceRange;

    public Product(String id, String name, String parentCategory, Number backgroundCategory, Number averagePrice, Pair<Number, Number> priceRange) {
        super(id, name, parentCategory, backgroundCategory);
        this.averagePrice = averagePrice;
        this.priceRange = priceRange;
    }

    @SuppressWarnings("unchecked")
    public Product(String id, Map<String, Object> map) {
        this(id, (String) map.get("name"), (String) map.get("parent_category"), (Number) map.get("background_category"), (Number) map.get("average_price"), null);

        Map<String, Object> range = (Map<String, Object>) map.get("price_range");

        Number min = (Number) range.get("minimum_price");
        Number max = (Number) range.get("maximum_price");
        this.priceRange = new Pair<>(min, max);
    }

    public Number getAveragePrice() { return averagePrice; }

    public void setAveragePrice(Number averagePrice) { this.averagePrice = averagePrice; }

    public Pair<Number, Number> getPriceRange() { return priceRange; }

    public void setPriceRange(Pair<Number, Number> priceRange) { this.priceRange = priceRange; }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Product) {
            Product product = (Product) obj;

            return this.getName().equals(product.getName()) && this.getParentCategory().equals(product.getParentCategory());
        } else return super.equals(obj);
    }

    public JSONObject toJSON() {
        JSONObject object = new JSONObject();

        try {
            object.put("id", this.getId());
            object.put("name", this.getName());
            object.put("parent_category", this.getParentCategory());
            object.put("background_category", this.getBackgroundCategory().doubleValue());
            object.put("average_price", this.getAveragePrice().doubleValue());

            JSONObject range = new JSONObject();
            range.put("minimum_price", this.getPriceRange().first.doubleValue());
            range.put("maximum_price", this.getPriceRange().second.doubleValue());
            object.put("price_range", range);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    public static Product toObject(String json) {
        try {
            JSONObject object = new JSONObject(json);

            JSONObject range = object.getJSONObject("price_range");
            Pair<Number, Number> priceRange = new Pair<>( (Number) range.get("minimum_price"), (Number) range.get("maximum_price") );

            return new Product(
                    (String) object.get("id"),
                    (String) object.get("name"),
                    (String) object.get("parent_category"),
                    (Number) object.get("background_category"),
                    (Number) object.get("average_price"),
                    priceRange
            );

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", this.getId());
        map.put("name", this.getName());
        map.put("parent_category", this.getParentCategory());
        map.put("background_category", this.getBackgroundCategory());
        map.put("average_price", this.getAveragePrice().doubleValue());

        Map<String, Object> range = new HashMap<>();
        range.put("minimum_price", this.getPriceRange().first);
        range.put("maximum_price", this.getPriceRange().second);

        map.put("price_range", range);

        return map;
    }

}