package com.loniquiz.comment.lobby.service;

import com.loniquiz.comment.lobby.dto.request.CreateImageRequestDTO;
import com.loniquiz.comment.lobby.dto.request.LobbyChatCreateRequestDTO;
import com.loniquiz.comment.lobby.dto.response.CreatedImageResponseDTO;
import com.loniquiz.comment.lobby.dto.response.LobbyChatListResponseDTO;
import com.loniquiz.comment.lobby.dto.response.LobbyChatResponseDTO;
import com.loniquiz.comment.lobby.entity.LobbyChat;
import com.loniquiz.comment.lobby.repository.LobbyChatRepository;
import com.loniquiz.users.entity.User;
import com.loniquiz.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class LobbyChatService {

    private final LobbyChatRepository lobbyChatRepository;
    private final UserRepository userRepository;

    public LobbyChatListResponseDTO create(LobbyChatCreateRequestDTO dto) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow();
        System.out.println("user = " + user);
        lobbyChatRepository.save(dto.toEntity(user));
        log.info("댓글 저장");
        return getChats();
    }

    public LobbyChatListResponseDTO getChats() {
        int pageSize = 3;
        List<LobbyChat> chat = lobbyChatRepository.findByOrderByCmDateDesc(PageRequest.of(0, pageSize));
        List<LobbyChatResponseDTO> collect = chat.stream()
                .map(LobbyChatResponseDTO::new)
                .collect(Collectors.toList());
        return LobbyChatListResponseDTO.builder()
                .chats(collect)
                .build();
    }
    public CreatedImageResponseDTO createImage(CreateImageRequestDTO dto) {
        // Replace with your actual REST_API_KEY
        String restApiKey = "1c714fc7245f4ab5c55533330a3b0073";

        // API endpoint URL
        String apiUrl = "https://api.kakaobrain.com/v2/inference/karlo/t2i";
        String prompt = dto.getPrompt();
        System.out.println("prompt = " + prompt);

        // Request payload
        String requestBody = "{ \"prompt\": \"" + prompt + "\", \"samples\": \"4\"}";
        System.out.println("requestBody = " + requestBody);

        // Create an HTTP client
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // Create an HTTP POST request
            HttpPost httpPost = new HttpPost(apiUrl);

            // Set headers
            httpPost.addHeader("Authorization", "KakaoAK " + restApiKey);
            httpPost.addHeader("Content-Type", "application/json");

            // Set request body
            httpPost.setEntity(new StringEntity(requestBody));

            // Execute the request and get the response
            HttpResponse response = httpClient.execute(httpPost);

            // Get the response entity
            HttpEntity entity = response.getEntity();

            // Print the response status code
            System.out.println("Response Status Code: " + response.getStatusLine().getStatusCode());

            // Print the response body
            if (entity != null) {
                String responseString = EntityUtils.toString(entity);
                System.out.println("Response Body: " + responseString);

                // Parse the JSON response
                JSONObject jsonResponse = new JSONObject(responseString);

                // Extract the "images" array
                JSONArray imagesArray = jsonResponse.getJSONArray("images");

                // Extract URLs from each image object in the array
                List<String> imageURLs = new ArrayList<>();
                for (int i = 0; i < imagesArray.length(); i++) {
                    JSONObject imageObject = imagesArray.getJSONObject(i);
                    String imageURL = imageObject.getString("image");
                    imageURLs.add(imageURL);
                }

                // Return the list of image URLs
                return CreatedImageResponseDTO.builder()
                        .image(imageURLs)
                        .build();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
