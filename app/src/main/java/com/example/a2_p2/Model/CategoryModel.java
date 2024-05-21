package com.example.a2_p2.Model;
import com.google.gson.annotations.SerializedName;

public class CategoryModel {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String categoryName;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public CategoryModel(String categoryName) {
        this.categoryName = categoryName;
    }
}
