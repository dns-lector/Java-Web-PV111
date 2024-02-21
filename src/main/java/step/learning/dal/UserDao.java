package step.learning.dal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.services.kdf.KdfService;

import java.sql.*;
import java.util.UUID;

@Singleton
public class UserDao {
    private final KdfService kdfService ;
    private final Connection dbConnection ;

     @Inject
    public UserDao(KdfService kdfService, Connection dbConnection) {
        this.kdfService = kdfService;
        this.dbConnection = dbConnection;
    }

    public boolean signupUser(String userName, String userPhone, String userPassword, String userEmail, String savedFilename) {
        String sql = "INSERT INTO Users(name,phone,salt,dk,email,avatar) VALUES(?,?,?,?,?,?)" ;
        try(PreparedStatement prep = dbConnection.prepareStatement(sql)) {
            String salt = kdfService.dk(UUID.randomUUID().toString(), UUID.randomUUID().toString());

            prep.setString(1, userName);
            prep.setString(2, userPhone);
            prep.setString(3, salt);
            prep.setString(4, kdfService.dk(userPassword,salt));
            prep.setString(5, userEmail);
            prep.setString(6, savedFilename);

            prep.executeUpdate() ;
            return true ;
        }
        catch (SQLException ex) {
            System.err.println(ex.getMessage());
            System.out.println(sql);
            return false ;
        }
    }

    public boolean installTable() {
        String sql = "CREATE TABLE Users(" +
                "id     CHAR(36)     PRIMARY KEY DEFAULT( UUID() )," +
                "name   VARCHAR(64)  NOT NULL," +
                "phone  VARCHAR(16)  NOT NULL," +
                "salt   CHAR(32)     NOT NULL," +
                "dk     CHAR(32)     NOT NULL," +
                "email  VARCHAR(128) NOT NULL," +
                "avatar VARCHAR(64)  NULL" +
            ")ENGINE = InnoDB, DEFAULT CHARSET = utf8mb4";
        // try {
        //     Driver mySqlDriver = new com.mysql.cj.jdbc.Driver() ;
        //     DriverManager.registerDriver( mySqlDriver ) ;
        //     String connectionString = "jdbc:mysql://localhost:3306/java_111" +
        //             "?useUnicode=true&characterEncoding=UTF-8" ;
        //     dbConnection = DriverManager.getConnection( connectionString, "root", "" ) ;
        // }
        // catch (SQLException e) {
        //     throw new RuntimeException(e);
        // }
        try(Statement statement = dbConnection.createStatement()) {
            statement.executeUpdate( sql ) ;
            return true ;
        }
        catch (SQLException ex) {
            System.err.println(ex.getMessage());
            System.out.println(sql);
            return false ;
        }
    }
}
