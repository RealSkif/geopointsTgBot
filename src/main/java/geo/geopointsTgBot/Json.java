package geo.geopointsTgBot;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;


public class Json {
    private static final String TARGET_URL = "http://localhost:8080/ggs";

    private double x;
    private double y;

    public Json(double x, double y) {
        this.x = x;
        this.y = y;
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

    public String sendJsonToUrl(String json) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);
        return restTemplate.postForObject(TARGET_URL, requestEntity, String.class);
    }
}
