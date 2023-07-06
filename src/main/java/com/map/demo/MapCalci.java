package com.map.demo;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class MapCalci {
    private String pincode;

    private String office;

    private double latitude;
    private double longitude;

    public MapCalci(){};
    public MapCalci(String pincode, String office, double latitude, double longitude) {
        this.pincode = pincode;
        this.office = office;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double[] mapCalci(String pin) throws IOException, InterruptedException, URISyntaxException {
        HttpRequest get = HttpRequest.newBuilder()
                .uri(new URI("https://api.data.gov.in/resource/5c2f62fe-5afa-4119-a499-fec9d604d5bd?api-key=579b464db66ec23bdd000001cdd3946e44ce4aad7209ff7b23ac571b&format=json&filters%5Bpincode%5D=" + pin))
                .GET()
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpResponse response = httpClient.send(get, HttpResponse.BodyHandlers.ofString());
        JSONObject jsonObject=new JSONObject(response.body().toString());
        JSONArray jsonArray=new JSONArray(jsonObject.getJSONArray("records"));

        for(int i=0;i<jsonArray.length();i++) {
            String str= jsonObject.getJSONArray("records").getJSONObject(i).get("officename").toString();
            System.out.println(str);
        }

        double[] doubles=new double[2];
        doubles[0]= Double.parseDouble(jsonObject.getJSONArray("records").getJSONObject(2).get("latitude").toString());
        doubles[1]= Double.parseDouble(jsonObject.getJSONArray("records").getJSONObject(1).get("longitude").toString());
        return doubles;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
