package holymagic.typeservice.repository;

import holymagic.typeservice.model.user.PersonalBest;
import holymagic.typeservice.model.user.PersonalBestId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalBestRepository extends JpaRepository<PersonalBest, PersonalBestId> {

    @Modifying
    @Query(value = """
        INSERT INTO personal_bests
        (language, mode, mode2, wpm, acc, consistency, punctuation, numbers, timestamp) 
        VALUES (?#{#pb.language}, ?#{#pb.mode}, ?#{#pb.mode2}, ?#{#pb.wpm}, 
                ?#{#pb.acc}, ?#{#pb.consistency}, ?#{#pb.punctuation}, 
                ?#{#pb.numbers}, ?#{#pb.timestamp})
        ON CONFLICT (language, mode, mode2, punctuation, numbers) 
        DO UPDATE SET 
            wpm = EXCLUDED.wpm, 
            acc = EXCLUDED.acc, 
            consistency = EXCLUDED.consistency, 
            timestamp = EXCLUDED.timestamp
        """, nativeQuery = true)
    void upsert(@Param("pb") PersonalBest pb);

}
