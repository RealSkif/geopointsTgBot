package geo.geopointsTgBot;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;


public class Json {


    private double x;
    private double y;
    private double radius;

    public Json(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public Json() {
    }

    public double[] getCoordinates() {
        return new double[]{this.x, this.y};
    }

    public void setCoordinates(String message) {
        String[] coord = message.split(",");
        this.x = Double.parseDouble(coord[0].trim());
        this.y = Double.parseDouble(coord[1].trim());
    }

    public String convertToJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

    public String sendJsonToUrl(String json, String url) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);
        return restTemplate.postForObject(url, requestEntity, String.class);

    }

}
