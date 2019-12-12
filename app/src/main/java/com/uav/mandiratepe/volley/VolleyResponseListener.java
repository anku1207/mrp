package com.uav.mandiratepe.volley;

import org.json.JSONException;

public interface VolleyResponseListener {
    void onError(String message);

    void onResponse(Object response) throws JSONException;
}
