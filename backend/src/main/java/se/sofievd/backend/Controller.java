package se.sofievd.backend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("/api/weather/clothes")
    public ResponseEntity<String> getClothes(){
        return ResponseEntity.ok().body("clothes to wear: ");
    }
}
