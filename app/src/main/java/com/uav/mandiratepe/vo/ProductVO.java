package com.uav.mandiratepe.vo;

import java.io.Serializable;

public class ProductVO implements Serializable {

    private Integer productID;
    private String productName;
    private String url;

    private String utfCode;
    private Integer categoryId;
    private String categoryName;

    private String unitTypeName;
    private String imagePath;
    private String thumbnailImagePath;
    private String smallImagePath;
    private String shortDescription;
    private String description;
    private Integer minimumQty;
    private Integer maximumQty;
    private Double mrp;
    private Double mop;
    private Integer qty;
    private String nutritionImage;

    private String total;
    private Double avgweight;



    public ProductVO() {
    }

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getUtfCode() {
        return utfCode;
    }

    public void setUtfCode(String utfCode) {
        this.utfCode = utfCode;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getUnitTypeName() {
        return unitTypeName;
    }

    public void setUnitTypeName(String unitTypeName) {
        this.unitTypeName = unitTypeName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getThumbnailImagePath() {
        return thumbnailImagePath;
    }

    public void setThumbnailImagePath(String thumbnailImagePath) {
        this.thumbnailImagePath = thumbnailImagePath;
    }

    public String getSmallImagePath() {
        return smallImagePath;
    }

    public void setSmallImagePath(String smallImagePath) {
        this.smallImagePath = smallImagePath;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMinimumQty() {
        return minimumQty;
    }

    public void setMinimumQty(Integer minimumQty) {
        this.minimumQty = minimumQty;
    }

    public Integer getMaximumQty() {
        return maximumQty;
    }

    public void setMaximumQty(Integer maximumQty) {
        this.maximumQty = maximumQty;
    }

    public Double getMrp() {
        return mrp;
    }

    public void setMrp(Double mrp) {
        this.mrp = mrp;
    }

    public Double getMop() {
        return mop;
    }

    public void setMop(Double mop) {
        this.mop = mop;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getNutritionImage() {
        return nutritionImage;
    }

    public void setNutritionImage(String nutritionImage) {
        this.nutritionImage = nutritionImage;
    }

    public Double getAvgweight() {
        return avgweight;
    }

    public void setAvgweight(Double avgweight) {
        this.avgweight = avgweight;
    }
}
