package com.github.pgcomb.course;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Title: ConfigStore <br>
 * Description: ConfigStore <br>
 * Date: 2018年08月15日
 *
 * @author 王东旭
 * @version 1.0.0
 * @since jdk8
 */
public class ConfigStore {

    private Map<KeyEnum,ConfigCache> map = new HashMap<>();

    public ConfigStore(Function<String,String> functions, List<KeyEnum> keyEnums) {
        keyEnums.forEach(keyEnum1 -> map.put(keyEnum1,new ConfigCache(functions,keyEnum1)));
    }

    public String getStr(KeyEnum keyEnum){
        return map.get(keyEnum).obtain();
    }

    public Boolean getBool(KeyEnum keyEnum){
        return Boolean.valueOf(getStr(keyEnum));
    }

    public Integer getInt(KeyEnum keyEnum){
        return Integer.valueOf(getStr(keyEnum));
    }

    public Float getFloat(KeyEnum keyEnum){
        return Float.valueOf(getStr(keyEnum));
    }

    private class ConfigCache extends MemoryDelayCache<String>{

        private ConfigCache(Function<String,String> function,KeyEnum keyEnum){
            super(keyEnum.getDef(),() ->function.apply(keyEnum.getKey()));
        }
    }
}
