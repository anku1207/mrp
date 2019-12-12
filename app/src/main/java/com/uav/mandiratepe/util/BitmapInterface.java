package com.uav.mandiratepe.util;



import com.uav.mandiratepe.vo.BitmapVO;

import java.util.List;

public interface BitmapInterface {

    void downloadComplete(List<BitmapVO> bitmapVOs);
    void error(String error);
}
