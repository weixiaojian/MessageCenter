package com.imwj.msg.support.utils;

import cn.hutool.core.map.MapUtil;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.PropertyPlaceholderHelper;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author langao_q
 * 内容占位符 替换
 * 占位符格式{$var}
 */
public class ContentHolderUtil {

	/**
	 * 占位符前缀
	 */
	private static final String PLACE_HOLDER_PREFIX = "{$";

	/**
	 * 占位符后缀
	 */
	private static final String PLACE_HOLDER_ENDFIX = "}";

	private static final StandardEvaluationContext EVALUTION_CONTEXT;

	private static final PropertyPlaceholderHelper PROPERTY_PLACEHOLDER_HELPER = new PropertyPlaceholderHelper(
			PLACE_HOLDER_PREFIX, PLACE_HOLDER_ENDFIX);

	static {
		EVALUTION_CONTEXT = new StandardEvaluationContext();
		EVALUTION_CONTEXT.addPropertyAccessor(new MapAccessor());
	}

	public static String replacePlaceHolder(final String template, final Map<String, String> paramMap) {
		String replacedPushContent = PROPERTY_PLACEHOLDER_HELPER.replacePlaceholders(template,
				new CustomPlaceholderResolver(template, paramMap));
		return replacedPushContent;
	}

	private static class CustomPlaceholderResolver implements PropertyPlaceholderHelper.PlaceholderResolver {
		private final String template;
		private final Map<String, String> paramMap;

		public CustomPlaceholderResolver(String template, Map<String, String> paramMap) {
			super();
			this.template = template;
			this.paramMap = paramMap;
		}

		@Override
		public String resolvePlaceholder(String placeholderName) {
			String value = paramMap.get(placeholderName);
			if (null == value) {
				String errorStr = MessageFormat.format("template:{} require param:{},but not exist! paramMap:{}",
						template, placeholderName, paramMap.toString());
				throw new IllegalArgumentException(errorStr);
			}
			return value;
		}
	}

	public static void main(String[] args) {
		Map<String, String> params = MapUtil.newHashMap();
		params.put("content", "test");
        params.put("url", "123");
		String content = ContentHolderUtil.replacePlaceHolder("{\"content\":\"{$content}\",\"url\":\"{$url}\",\"title\":\"\"}", params);
		System.out.println(content);
	}

}
