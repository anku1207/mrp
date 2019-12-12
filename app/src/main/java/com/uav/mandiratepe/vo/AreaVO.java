package com.uav.mandiratepe.vo;

import java.io.Serializable;

public class AreaVO  implements Serializable {
    private Integer areaId;
    private String areaName;

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    @Override
    public  String toString(){
        return areaName;
    }
}
