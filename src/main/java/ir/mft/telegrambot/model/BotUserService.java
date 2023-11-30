package ir.mft.telegrambot.model;



import ir.mft.telegrambot.contoller.exception.NoContentException;
import ir.mft.telegrambot.model.entity.BotUser;
import ir.mft.telegrambot.model.repository.CrudRepository;
import ir.mft.telegrambot.model.service.impl.ServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BotUserService implements ServiceImpl<BotUser, Long> {
    private static BotUserService botUserService = new BotUserService();

    private BotUserService() {
    }

    public static BotUserService getService() {
        return botUserService;
    }


    @Override
    public BotUser save(BotUser botUser) throws Exception {
        try (CrudRepository<BotUser, Long> crudRepository = new CrudRepository<>()) {
            botUser.setDeleted(false);
            crudRepository.insert(botUser);
            return botUser;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public BotUser edit(BotUser botUser) throws Exception {
        try (CrudRepository<BotUser, Long> crudRepository = new CrudRepository<>()) {
            crudRepository.update(botUser);
            return botUser;
        }
    }

    @Override
    public BotUser remove(Long id) throws Exception {
        try (CrudRepository<BotUser, Long> crudRepository = new CrudRepository<>()) {
            BotUser botUser = findById(id);
            botUser.setDeleted(true);
            crudRepository.update(botUser);
            return botUser;
        }
    }

    @Override
    public List<BotUser> findAll() throws Exception {
        try (CrudRepository<BotUser, Long> repository = new CrudRepository<>()) {
            List<BotUser> botUsers = repository.selectAll(BotUser.class);
            if (botUsers.size() == 0) {
                return null;
            }
            return botUsers;
        }
    }

    @Override
    public List<BotUser> findAllPaging(int pageNumber, int pageSize) throws Exception {
        return null;
    }


    @Override
    public BotUser findById(Long id) throws Exception {
        try (CrudRepository<BotUser, Long> repository = new CrudRepository<>()) {
            BotUser botUser = repository.selectById(BotUser.class, id);
            if (botUser == null) {
                return null;
            }
            return botUser;
        }
    }

    @Override
    public Long getRecordCount() throws Exception {
        return null;
    }

    public BotUser findByUserId(String userId) throws Exception {
        try (CrudRepository<BotUser, Long> repository = new CrudRepository<>()) {
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            BotUser botUser = repository.getSingleResult("BotUser.FindByUserId", params);
            if (botUser == null) {
               return null;
            } else {
                return botUser;
            }
        }

    }

    public BotUser findByChatId(String chatId) throws Exception {
        try (CrudRepository<BotUser, Long> repository = new CrudRepository<>()) {

            Map<String, Object> params = new HashMap<>();
            params.put("chatId", chatId);
            BotUser botUser = repository.getSingleResult("BotUser.FindByChatId", params);
            if (botUser == null) {
                throw new NoContentException();
            } else {
                return botUser;
            }
        }
    }
}
