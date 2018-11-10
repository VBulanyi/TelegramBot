import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class Weather {

    public static String getWeather(String location, Model model) throws IOException {

//        System.out.println(location);

        //exp. Location{longitude=30.43329, latitude=59.966244}


        String tmp = null;
        String longitude = location.substring(location.indexOf("="), location.indexOf(","));

        String latitude = location.substring(location.indexOf("latitude="));

        tmp = latitude.substring(latitude.indexOf("="), latitude.indexOf("}"));
        latitude = tmp;


        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?lat" + latitude + "&lon" + longitude +"&units=metric&appid=e43d516a21ac849b44e97a4381ce5c9a");

        Scanner in = new Scanner((InputStream) url.getContent());
        String result = " ";
        while (in.hasNext()) {
            result += in.nextLine();
        }

        JSONObject object = new JSONObject(result);
        model.setName(object.getString("name"));

        JSONObject main = object.getJSONObject("main");
        model.setTemp(main.getDouble("temp"));
        model.setHumidity(main.getDouble("humidity"));
        model.setPressure(main.getDouble("pressure"));

        JSONArray getArray = object.getJSONArray("weather");
        for (int i = 0; i < getArray.length(); i++) {
            JSONObject obj = getArray.getJSONObject(i);
            model.setIcon((String) obj.get("icon"));
            model.setMain((String) obj.get("main"));
        }

        return "Город: " + model.getName() + "\n" +
                "Температура: " + model.getTemp() + " C" + "\n" +
                "Влажность: " + model.getHumidity() + " %" + "\n" +
                "Давление: " + model.getPressure() + " мм рт.ст." + "\n" +
                "Описание погоды: " + model.getMain() + "\n" +
                "https://openweathermap.org/img/w/" + model.getIcon() + ".png";


    }
}
