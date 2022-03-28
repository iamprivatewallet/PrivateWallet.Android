package xc.common.framework.net;


import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import xc.common.tool.utils.CheckedUtilsKt;

final class MyGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    MyGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String resultStr = value.string();
        if (resultStr == null || resultStr.length() == 0) {
            resultStr = "{}";
        }
//        Log.e("reuslt 111", reuslt);
        try {
            JSONObject jsonObject = new JSONObject(resultStr);

            if (jsonObject.has("data")) {
                try {
                    String a = jsonObject.getString("data");
                    if (CheckedUtilsKt.checkIsEmpty(a)) {
                        jsonObject.remove("data");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (jsonObject.has("errorData")) {
                jsonObject.remove("errorData");
            }

            InputStreamReader reder = new InputStreamReader(new ByteArrayInputStream(jsonObject.toString().getBytes()), "UTF-8");

            JsonReader jsonReader = gson.newJsonReader(reder);

            T result = adapter.read(jsonReader);
            if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw new JsonIOException("JSON document was not fully consumed.");
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new JsonIOException("JSON document was not fully consumed.");
        } finally {
            value.close();
        }
    }
}