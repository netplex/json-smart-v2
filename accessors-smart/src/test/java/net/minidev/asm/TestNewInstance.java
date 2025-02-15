package net.minidev.asm;

import java.util.TreeMap;
import org.junit.jupiter.api.Test;

public class TestNewInstance {
  @Test
  public void testLangUtilPkg() {
    @SuppressWarnings("rawtypes")
    BeansAccess<TreeMap> acTm = BeansAccess.get(TreeMap.class);
    acTm.newInstance();
  }
}
