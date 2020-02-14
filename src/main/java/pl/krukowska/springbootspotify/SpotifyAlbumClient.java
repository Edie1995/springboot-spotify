package pl.krukowska.springbootspotify;


import pl.krukowska.springbootspotify.entity.Track;
import pl.krukowska.springbootspotify.model.Item;
import pl.krukowska.springbootspotify.model.SpotifyAlbum;
import pl.krukowska.springbootspotify.model.dto.SpotifyAlbumDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import pl.krukowska.springbootspotify.repository.TrackRepo;


import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SpotifyAlbumClient {

    private TrackRepo trackRepo;

    @Autowired
    public SpotifyAlbumClient(TrackRepo trackRepo) {
        this.trackRepo = trackRepo;
    }

    @GetMapping("/album/{authorName}")
    public List<SpotifyAlbumDTO> getAlbumsByAuthor(OAuth2Authentication details, @PathVariable String authorName) {

        String jwt = ((OAuth2AuthenticationDetails) details.getDetails()).getTokenValue();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwt);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);


        ResponseEntity<SpotifyAlbum> exchange = restTemplate.exchange("https://api.spotify.com/v1/search?q=" + authorName + "&type=track&market=US&limit=10&offset=5",
                HttpMethod.GET,
                httpEntity,
                SpotifyAlbum.class);

        List<SpotifyAlbumDTO> spotifyAlbumDTOs =
                exchange.getBody()
                        .getTracks().getItems().stream()
                        .map(item -> new SpotifyAlbumDTO(item.getName(), item.getAlbum().getImages().get(0).getUrl()))
                        .collect(Collectors.toList());


        for (Item item : exchange.getBody().getTracks().getItems()) {
            System.out.println(item.getName());
            System.out.println(item.getAlbum().getImages().get(0).getUrl());
        }

        return spotifyAlbumDTOs;
    }

    @PostMapping("/add-track")
    public void addTrack(@RequestBody Track track) {
        trackRepo.save(track);

    }

}
