package examples.avro;

import java.io.File;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;

public class AvroWithoutCodegen {

  public static void main(String[] args) {
    try {
      // use a Parser to read our schema definition and create a Schema object
      Schema schema = new Schema.Parser().parse(new File("rt-pipeline/src/test/avro/user.avsc"));

      GenericRecord user1 = new GenericData.Record(schema);
      user1.put("name", "Alyssa");
      user1.put("favorite_number", 256);
      // Leave favorite color null

      GenericRecord user2 = new GenericData.Record(schema);
      user2.put("name", "Ben");
      user2.put("favorite_number", 7);
      user2.put("favorite_color", "red");

      // Serialize user1 and user2 to disk
      File file = new File("target/users-2.avro");
      DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<>(schema);
      DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<>(datumWriter);
      dataFileWriter.create(schema, file);
      dataFileWriter.append(user1);
      dataFileWriter.append(user2);
      dataFileWriter.close();

      // Deserialize users from disk
      DatumReader<GenericRecord> datumReader = new GenericDatumReader<>(schema);
      DataFileReader<GenericRecord> dataFileReader = new DataFileReader<>(file, datumReader);
      GenericRecord user = null;
      while (dataFileReader.hasNext()) {
        // Reuse user object by passing it to next(). This saves us from
        // allocating and garbage collecting many objects for files with
        // many items.
        user = dataFileReader.next(user);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
