package com.test.testweatherback.util;

import com.test.testweatherback.enumeration.ForecastSource;
import com.test.testweatherback.enumeration.Icon;
import java.util.EnumMap;
import java.util.Map;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="weather.icons")
@Setter
public class IconTranslator {

  private Map<ForecastSource, Map<String, Icon>> relations;

  public String getIconId(ForecastSource source, String icon) {

    String iconId;

    if(this.relations.get(source).containsKey(icon)) {
      iconId = this.relations.get(source).get(icon).getValue();
    } else {
      iconId = Icon.UNKNOWN.getValue();
    }

    return iconId;
  }

}
