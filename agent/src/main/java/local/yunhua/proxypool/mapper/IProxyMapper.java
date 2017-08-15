package local.yunhua.proxypool.mapper;

import local.yunhua.proxypool.domain.Proxy;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface IProxyMapper {

    int create(Proxy proxy);

    Proxy retrieveOne(Proxy proxy);

    List<Proxy> retrieveMany();

    int update(Proxy proxy);

    int delete(Proxy proxy);
}
