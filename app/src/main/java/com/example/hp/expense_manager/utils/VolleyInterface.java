package com.example.hp.expense_manager.utils;

import com.android.volley.VolleyError;

/**
 * Created by hp on 22-Mar-17.
 */

public interface VolleyInterface {
    void requestStarted(int requestCode);

    void requestCompleted(int requestCode, String response);

    void requestEndedWithError(int requestCode, VolleyError error);

}
