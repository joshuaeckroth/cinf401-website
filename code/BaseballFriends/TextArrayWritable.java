// from: https://github.com/alexholmes/hiped2/blob/master/src/main/java/hip/ch3/TextArrayWritable.java

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.Text;

public class TextArrayWritable extends ArrayWritable {
  public TextArrayWritable() {
    super(Text.class);
  }

  public TextArrayWritable(Text[] strings) {
    super(Text.class, strings);
  }
}
