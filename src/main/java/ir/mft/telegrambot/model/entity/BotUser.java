package ir.mft.telegrambot.model.entity;



import com.google.gson.Gson;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder


@Entity(name = "botUserEntity")
@Table(name = "bot_user_tbl")
@NamedQueries({
        @NamedQuery(name = "BotUser.FindByChatId", query = "select oo from botUserEntity  oo where oo.chatId=:chatId"),
        @NamedQuery(name = "BotUser.FindByUserId", query = "select oo from botUserEntity oo where oo.userId=:userId"),
})
public class BotUser extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Column(name = "b_username", length = 20, unique = true)
    private String userName;

    @Column(name = "b_chat_id")
    private String chatId;

    @Column(name = "b_user_id")
    private String userId;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
