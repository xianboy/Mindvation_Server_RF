import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestUtil {

    @Test
    public void TestStringUtil() {
        Long id1 = 1L;
        List<Long> list1 = new ArrayList<Long>();
        list1.add(3L);
        list1.add(4L);
        list1.add(29L);
        list1.add(43l);
        Long id2 = 2L;
        List<Long> list2 = new ArrayList<Long>();
        list2.add(29L);
        list2.add(43l);
        Map<Long, List<Long>> map = new HashMap<>();
        map.put(id1, list1);
        map.put(id2, list2);
        System.out.print(map.toString());
    }


}
