package com.map.demo;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Scanner;

@Controller
public class logic {


    private static JSONArray array;
    private static ArrayList list;


    /*
        Info class has private variable which store api key
        and api key should be hidden :)
        so make an java class and initial and set a getter to get the api key
     */
    @Autowired
    private Info info;


    @GetMapping("/")
    public String getHome(){
        return "input";
    }

    @GetMapping("/input")
    public String getInput(){
        return "input";
    }

    @PostMapping("/input")
    public String getting(@RequestParam("pincode") String pincode,@RequestParam("officeSelect") String office,Model model) throws IOException, URISyntaxException, InterruptedException {
        model.addAttribute("pin",pincode);
        model.addAttribute("offices",list);
        double[] doubles=new double[2];
        doubles[0]= Double.parseDouble(array.getJSONObject(list.indexOf(office)).get("latitude").toString());
        doubles[1]= Double.parseDouble(array.getJSONObject(list.indexOf(office)).get("longitude").toString());
        model.addAttribute("lat",doubles[0]);
        model.addAttribute("lon",doubles[1]);
        return "input";
    }

    @GetMapping("/offices")
    @ResponseBody
    public ArrayList getUpdate(@RequestParam("pincode") String get) throws IOException, URISyntaxException, InterruptedException {
        list=new ArrayList<>();
        array=mapCalci(get,list);
        for(int i=0;i<array.length();i++) {
            String str= array.getJSONObject(i).get("officename").toString();
            System.out.println(str);
            list.add(str);
        }
        return list;
    }

//    @GetMapping("/input")
//    @PostMapping("/input")
//    @ResponseBody
//    public ModelAndView getPost(@RequestParam("pincode") int pin,@RequestParam("office") String str){
//        System.out.println(
//                pin+" "+str
//        );
//        return new ModelAndView("input");
//    }


    /*
        This method gets data from api response(json),
        we send request api using pincode(we need to authorize our self)
        and we get Json in response we double down to office and get latitude and longitude
     */
    public JSONArray mapCalci(String pin,ArrayList<String> list) throws IOException, InterruptedException, URISyntaxException {

        //ArrayList<String> list=new ArrayList<>();
        HttpRequest get = HttpRequest.newBuilder()
                .uri(new URI("https://api.data.gov.in/resource/5c2f62fe-5afa-4119-a499-fec9d604d5bd?api-key="+info.getApi()+"&format=json&filters%5Bpincode%5D=" + pin))
                .GET()
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpResponse response = httpClient.send(get, HttpResponse.BodyHandlers.ofString());
        JSONObject jsonObject=new JSONObject(response.body().toString());
        JSONArray jsonArray=new JSONArray(jsonObject.getJSONArray("records"));

//        for(int i=0;i<jsonArray.length();i++) {
//            String str= jsonObject.getJSONArray("records").getJSONObject(i).get("officename").toString();
//            System.out.println(str);
//            list.add(str);
//        }

//        double[] doubles=new double[2];
//        doubles[0]= Double.parseDouble(jsonObject.getJSONArray("records").getJSONObject(1).get("latitude").toString());
//        doubles[1]= Double.parseDouble(jsonObject.getJSONArray("records").getJSONObject(1).get("longitude").toString());

        return jsonArray;
    }

}
