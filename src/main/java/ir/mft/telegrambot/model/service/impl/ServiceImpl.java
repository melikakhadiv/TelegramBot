package ir.mft.telegrambot.model.service.impl;

import java.util.List;

public interface ServiceImpl<T,I>{
    public T save(T t) throws Exception;
    public T edit(T t) throws Exception;
    public T remove(I id) throws Exception;
    public List<T> findAll() throws Exception;
    public List<T> findAllPaging(int pageNumber, int pageSize) throws Exception;
    public T findById(I id) throws Exception;
    public Long getRecordCount() throws Exception;
}
