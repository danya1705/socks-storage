package pro.sky.java.socksstorage.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.java.socksstorage.entity.Socks;
import pro.sky.java.socksstorage.repository.SocksRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class SocksService {

    private final SocksRepository socksRepository;

    /**
     * Adds socks quantity to the database record with the same color and cotton part,
     * or creates the new record if socks with such parameters yet not exists in the DB.
     *
     * @return Updated database record
     */
    public Socks applyIncome(Socks socks) {

        return socksRepository
                .save(socksRepository.findByColorAndCottonPart(socks.getColor(), socks.getCottonPart())
                        .map(s -> {
                            socks.setId(s.getId());
                            socks.setQuantity(socks.getQuantity() + s.getQuantity());
                            return socks;
                        })
                        .orElse(socks));
    }

    /**
     * Subtracts socks quantity from the database record with the same color and cotton part.
     *
     * @return Updated database record, or empty Optional if record with such color and cotton part doesn't
     * exist in the base, or there is not enough quantity.
     */
    public Optional<Socks> applyOutcome(Socks socks) {

        Optional<Socks> socksInStorage = socksRepository.findByColorAndCottonPart(socks.getColor(), socks.getCottonPart());
        if (socksInStorage.isEmpty() || socksInStorage.get().getQuantity() < socks.getQuantity()) {
            log.warn("Not enough quantity for outcome");
            return Optional.empty();
        } else {
            socks.setId(socksInStorage.get().getId());
            socks.setQuantity(socksInStorage.get().getQuantity() - socks.getQuantity());
            return Optional.of(socksRepository.save(socks));
        }
    }

    /**
     * Finds socks with such color and cotton part greater, smaller oe equal to specified number
     * (depends on operation parameter).
     *
     * @param color socks color.
     * @param operation type of operation, should be "moreThan", "lessThan" or "equal" (in any case).
     * @param cottonPart level of cotton part to compare with values in database.
     * @return Number of socks that meets the given parameters, or empty Optional if operation parameter is not valid.
     */
    public Optional<Integer> getSocksQuantity(String color, String operation, int cottonPart) {

        return switch (operation) {
            case "morethan" ->
                    Optional.of(aggregateSocksQuantity(socksRepository.findAllByColorAndCottonPartGreaterThan(color, cottonPart)));
            case "lessthan" ->
                    Optional.of(aggregateSocksQuantity(socksRepository.findAllByColorAndCottonPartLessThan(color, cottonPart)));
            case "equal" -> Optional.of(socksRepository.findByColorAndCottonPart(color, cottonPart)
                    .map(Socks::getQuantity)
                    .orElse(0));
            default -> Optional.empty();
        };
    }

    public List<Socks> getAllSocks() {
        return socksRepository.findAll();
    }

    private int aggregateSocksQuantity(List<Socks> socksList) {
        return socksList.stream()
                .mapToInt(Socks::getQuantity)
                .sum();
    }
}
