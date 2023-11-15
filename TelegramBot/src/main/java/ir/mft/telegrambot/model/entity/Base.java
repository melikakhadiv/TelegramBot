package ir.mft.telegrambot.model.entity;

import com.google.gson.Gson;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@NoArgsConstructor
@Data
@SuperBuilder


@MappedSuperclass
public class Base implements Serializable {
//    @Column(name = "b_deleted", length = 1)
    @JsonbTransient
    private boolean deleted;

//    @Version
//    @Column(name = "b_version_id", length = 1)
//    @JsonbTransient
//    private long versionId;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
