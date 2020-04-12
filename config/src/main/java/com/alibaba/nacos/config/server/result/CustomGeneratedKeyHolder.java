package com.alibaba.nacos.config.server.result;

import com.alibaba.nacos.config.server.utils.PropertyUtil;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CustomGeneratedKeyHolder
 * @Description: 主键获取
 * @author gonghb
 * @Date 2020/4/11
 * @Version V1.0
 **/
public class CustomGeneratedKeyHolder extends GeneratedKeyHolder {
    private String pkName ;

    public CustomGeneratedKeyHolder(String pkName) {
        this.pkName = pkName;
    }

    @Override
    public Number getKey() throws InvalidDataAccessApiUsageException, DataRetrievalFailureException {
        if(PropertyUtil.isStandaloneUsePostgresql()){
            List<Map<String, Object>> keyList = getKeyList();
            if (keyList.isEmpty()) {
                return null;
            }
            Map<String, Object> res =  keyList.get(0);
            Iterator<String> keyIter = res.keySet().iterator();
            while (keyIter.hasNext()) {
                String name = keyIter.next();
                Object val = res.get(name);
                if ((val instanceof Number) && pkName.equals(name)) {
                    return (Number) val;
                }
            }
            throw new DataRetrievalFailureException("Unable to retrieve the generated key. " +
                "Check that the table has an identity column enabled.");

        }else {
            return super.getKey();
        }
    }
}
