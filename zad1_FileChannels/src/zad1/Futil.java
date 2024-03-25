package zad1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class Futil {
    public static void processDir(String source, String destination) {
        Path sourceDir = Path.of(source);
        Path resultFile = Path.of(destination);

        try (FileChannel resultChannel = FileChannel.open(resultFile, StandardOpenOption.CREATE,
                StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {

            Files.walkFileTree(sourceDir, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    ByteBuffer read = read(file);
                    resultChannel.write(read);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ByteBuffer read(Path file) {
        try (FileChannel currentFileChannel = FileChannel.open(file)) {
            ByteBuffer buffer = ByteBuffer.allocate((int) currentFileChannel.size());
            currentFileChannel.read(buffer);
            buffer.flip();

            ByteBuffer encoded = convertCp1250ToUtf8(buffer);
            return encoded;
        } catch (IOException e) {
            return null;
        }
    }

    private static ByteBuffer convertCp1250ToUtf8(ByteBuffer buffer) {
        Charset cp1250 = Charset.forName("Cp1250");
        Charset utf8 = StandardCharsets.UTF_8;
        CharBuffer charBuffer = cp1250.decode(buffer);
        return utf8.encode(charBuffer);
    }
}
