package it.dominick.dp.utils;

import org.json.JSONObject;

public class HTTPCallbacks {
    public interface JSONResponseCallback {
        void handle(JSONObject response);
    }

    public interface UUIDResponseCallback {
        void handle(String uuid);
    }

    public interface GetTextureResponse {
        void handle(String texture, String signature);
    }
}
