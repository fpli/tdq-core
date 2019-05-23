package examples.avro;

public class AvroWithCodegen {

    public static void main(String[] args) {
        // Create user1
//        User user1 = new User();
//        user1.setName("Alyssa");
//        user1.setFavoriteNumber(256);
//        // Leave favorite color null
//
//        // Create user2, Alternate constructor
//        User user2 = new User("Ben", 7, "red");
//
//        // Create user3, Construct via builder
//        User user3 = User.newBuilder()
//                .setName("Charlie")
//                .setFavoriteColor("blue")
//                .setFavoriteNumber(null)
//                .build();
//
//        try {
//            File file = new File("target/users-1.avro");
//
//            // Serialize user1, user2 and user3 to disk
//            DatumWriter<User> userDatumWriter = new SpecificDatumWriter<>(User.class);
//            DataFileWriter<User> dataFileWriter = new DataFileWriter<>(userDatumWriter);
//            dataFileWriter.create(user1.getSchema(), file);
//            dataFileWriter.append(user1);
//            dataFileWriter.append(user2);
//            dataFileWriter.append(user3);
//            dataFileWriter.close();
//
//            // Deserialize Users from disk
//            DatumReader<User> userDatumReader = new SpecificDatumReader<>(User.class);
//            DataFileReader<User> dataFileReader = new DataFileReader<>(file, userDatumReader);
//            User user = null;
//            while (dataFileReader.hasNext()) {
//                // Reuse user object by passing it to next(). This saves us from
//                // allocating and garbage collecting many objects for files with
//                // many items.
//                user = dataFileReader.next(user);
//                System.out.println(user);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
