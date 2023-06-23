package pro.sky.java.socksstorage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.java.socksstorage.entity.Socks;

import java.util.List;
import java.util.Optional;

public interface SocksRepository extends JpaRepository<Socks, Long> {
    Optional<Socks> findByColorAndCottonPart(String color, int cottonPart);
    List<Socks> findAllByColorAndCottonPartGreaterThan(String color, int cottonPart);
    List<Socks> findAllByColorAndCottonPartLessThan(String color, int cottonPart);
}
