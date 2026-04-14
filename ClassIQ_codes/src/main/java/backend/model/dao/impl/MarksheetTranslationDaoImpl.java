package backend.model.dao.impl;

import backend.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MarksheetTranslationDaoImpl {

    public String getText(String key, String languageCode) {
        String sql = "SELECT translated_text FROM marksheet_translation WHERE translation_key = ? AND language_code = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, key);
            stmt.setString(2, languageCode);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("translated_text");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return key;
    }
}