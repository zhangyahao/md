###net.sf.json办法
```$xslt
 JsonConfig jc = new JsonConfig();
 jc.setExcludes(new String[]{"属性"});
```
###jackson 办法
jackson可以直接在类上注解 直接在加载时忽略掉
```$xslt
@JsonIgnoreProperties({"忽略的属性"})
```
或者当需要忽视某些属性时
```$xslt
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.itshidu.jeelite.app.entity.Category;

public class JacksonUtil {
	/**
	 * 将对象序列化成JSON字符串
	 * @param obj
	 * @param inc
	 * @param excluds 排除对象中的哪些属性
	 * @return
	 * @throws JsonProcessingException
	 */
	public static String write(Object obj,Include inc,String... excluds) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(inc);
		SimpleBeanPropertyFilter theFilter = SimpleBeanPropertyFilter.serializeAllExcept(excluds);  
        FilterProvider filters = new SimpleFilterProvider().addFilter("ExcludesFilter", theFilter);  
		mapper.addMixIn(Category.class, ExcludesFilteMixIn.class);
        return mapper.writer(filters).writeValueAsString(obj);
	}
	
	public static String write(Object obj,String... excluds) throws JsonProcessingException {
		return write(obj, Include.ALWAYS, excluds);
	}
	
	public static String write(Object obj) throws JsonProcessingException {
		return write(obj, Include.ALWAYS);
	}
}
```