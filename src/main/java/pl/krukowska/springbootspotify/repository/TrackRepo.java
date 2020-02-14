
package pl.krukowska.springbootspotify.repository;
import pl.krukowska.springbootspotify.entity.Track;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackRepo extends MongoRepository<Track, String> {


}
