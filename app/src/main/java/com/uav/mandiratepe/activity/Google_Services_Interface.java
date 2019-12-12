package com.uav.mandiratepe.activity;

import android.location.Location;

public interface Google_Services_Interface {
    void getLastLocation(Location location);
    void onFailure(Exception e);


}
