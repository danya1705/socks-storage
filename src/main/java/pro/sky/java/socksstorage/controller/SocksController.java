package pro.sky.java.socksstorage.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.java.socksstorage.entity.Socks;
import pro.sky.java.socksstorage.service.SocksService;

import java.util.List;

@RestController
@RequestMapping("/api/socks")
@AllArgsConstructor
public class SocksController {

    private final SocksService socksService;

    @PostMapping("/income")
    public ResponseEntity<Socks> applyIncome(@Valid @RequestBody Socks socks) {
        return ResponseEntity.ok(socksService.applyIncome(socks));
    }

    @PostMapping("/outcome")
    public ResponseEntity<Socks> applyOutcome(@Valid @RequestBody Socks socks) {
        return socksService.applyOutcome(socks)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("")
    public ResponseEntity<Integer> getSocksQuantity(@RequestParam String color,
                                                    @RequestParam String operation,
                                                    @RequestParam int cottonPart) {
        return socksService.getSocksQuantity(color.toLowerCase(), operation.toLowerCase(), cottonPart)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/all")
    public ResponseEntity<List<Socks>> getAllSocks() {
        return ResponseEntity.ok(socksService.getAllSocks());
    }
}
