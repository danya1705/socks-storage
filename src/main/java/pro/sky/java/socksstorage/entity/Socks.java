package pro.sky.java.socksstorage.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
public class Socks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String color;

    @Min(0)
    @Max(100)
    private int cottonPart;

    @Min(1)
    private int quantity;

    public void setColor(String color) {
        this.color = color.toLowerCase();
    }
}
