package bitmap.trainning.google.com.displaybitmapsefficiently.util.extra;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * from "http://android-developers.blogspot.com/2010/07/multithreading-for-performance.html#uds-search-results"
 */
public class FlushedInputStream extends FilterInputStream {
    public FlushedInputStream(InputStream inputStream) {
        super(inputStream);
    }

    @Override
    public long skip(long n) throws IOException {
        long totalBytesSkipped = 0L;
        while (totalBytesSkipped < n) {
            long bytesSkipped = in.skip(n - totalBytesSkipped);
            if (bytesSkipped == 0L) {
                int len = read();
                if (len < 0) {
                    break;  // we reached EOF
                } else {
                    bytesSkipped = 1; // we read one byte
                }
            }
            totalBytesSkipped += bytesSkipped;
        }
        return totalBytesSkipped;
    }
}