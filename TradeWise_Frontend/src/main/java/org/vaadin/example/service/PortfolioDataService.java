// package org.vaadin.example.service;

// import org.springframework.stereotype.Service;
// import org.springframework.web.client.RestTemplate;
// import org.springframework.http.ResponseEntity;
// import org.springframework.beans.factory.annotation.Autowired;
// import com.StockTrading.demo.model.UserStock;  // Import from the correct package
// import java.util.Arrays;
// import java.util.List;
// import java.util.Collections;

// @Service
// public class PortfolioDataService {
    
//     private final RestTemplate restTemplate;
    
//     @Autowired
//     public PortfolioDataService(RestTemplate restTemplate) {
//         this.restTemplate = restTemplate;
//     }
    
//     public List<UserStock> fetchUserStocks(Integer userId) {
//         try {
//             String url = "http://localhost:8080/addstocks/" + userId;
//             ResponseEntity<UserStock[]> response = restTemplate.getForEntity(url, UserStock[].class);
//             if (response.getBody() != null) {
//                 return Arrays.asList(response.getBody());
//             }
//             return Collections.emptyList();
//         } catch (Exception e) {
//             e.printStackTrace();
//             return Collections.emptyList();
//         }
//     }
// }

