package uz.cherry.cherry_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.cherry.cherry_server.entity.Color;

import java.util.List;
import java.util.Optional;

public interface ColorRepository extends JpaRepository<Color, Integer> {

    boolean existsByNameAndActive(String name, boolean active);
    List<Color> findAllByActive(boolean active);
    Optional<Color> findByIdAndActive(Integer id, boolean active);

}
